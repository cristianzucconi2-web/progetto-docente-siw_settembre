package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserService userService;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("credentials", credentials);
        }
        return "index.html";
    }

    @GetMapping(value = "/login")
    public String showLoginForm(Model model) {
        return "login.html";
    }

    @GetMapping(value = "/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "register.html";
    }

    @PostMapping(value = "/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult userBindingResult,
                               @Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
                               Model model) {

        // Controllo unicità email
        if (userService.existsByEmail(user.getEmail())) {
            userBindingResult.rejectValue("email", "duplicate.email", "Questa email è già registrata.");
        }

        // Controllo unicità username
        if (credentialsService.existsByUsername(credentials.getUsername())) {
            credentialsBindingResult.rejectValue("username", "duplicate.username", "Questo username è già in uso.");
        }

        if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            userService.saveUser(user);
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            return "redirect:/login?registered=true";
        }

        return "register.html";
    }

    @GetMapping(value = "/success")
    public String defaultAfterLogin(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

        model.addAttribute("credentials", credentials);
        if (credentials != null && Credentials.ADMIN_ROLE.equals(credentials.getRole())) {
            model.addAttribute("isAdmin", true);
        } else {
            model.addAttribute("isAdmin", false);
        }

        return "index.html";
    }

    @GetMapping(value = "/403")
    public String accessDenied() {
        return "403.html";
    }
}
