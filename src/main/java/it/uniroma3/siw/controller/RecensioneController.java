package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.RecensioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RecensioneController {

    @Autowired
    private RecensioneService recensioneService;

    @Autowired
    private CredentialsService credentialsService;

    @PostMapping("/film/{id}/recensione")
    public String aggiungiRecensione(@PathVariable("id") Long id,
                                     @RequestParam("testo") String testo,
                                     @RequestParam(value = "voto", defaultValue = "5") Integer voto) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Credentials credentials = credentialsService.getCredentials(username);

        if (credentials != null && credentials.getUser() != null) {
            recensioneService.salvaRecensione(id, testo, voto, credentials.getUser());
        }

        return "redirect:/film/" + id;
    }

    @GetMapping("/recensione/modifica/{id}")
    public String formModificaRecensione(@PathVariable("id") Long id, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Credentials credentials = credentialsService.getCredentials(username);
        User utenteLoggato = credentials != null ? credentials.getUser() : null;

        Recensione recensione = recensioneService.findById(id).orElse(null);

        if (recensione != null && utenteLoggato != null && recensione.getUtente().getId().equals(utenteLoggato.getId())) {
            model.addAttribute("recensione", recensione);
            return "modificaRecensione.html";
        }

        return "redirect:/";
    }

    @PostMapping("/recensione/modifica/{id}")
    public String salvaModificaRecensione(@PathVariable("id") Long id,
                                          @RequestParam("testo") String testo,
                                          @RequestParam(value = "voto", required = false) Integer voto) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Credentials credentials = credentialsService.getCredentials(username);
        User utenteLoggato = credentials != null ? credentials.getUser() : null;

        Recensione recensione = recensioneService.findById(id).orElse(null);
        if (recensione != null && utenteLoggato != null && recensione.getUtente().getId().equals(utenteLoggato.getId())) {
            recensioneService.modificaRecensione(id, testo, voto, utenteLoggato);
            return "redirect:/film/" + recensione.getFilm().getId();
        }

        return "redirect:/";
    }

    @PostMapping("/recensione/elimina/{id}")
    public String eliminaRecensione(@PathVariable("id") Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Credentials credentials = credentialsService.getCredentials(username);
        User utenteLoggato = credentials != null ? credentials.getUser() : null;

        Recensione recensione = recensioneService.findById(id).orElse(null);
        if (recensione != null) {
            Long filmId = recensione.getFilm().getId();
            boolean isAdmin = credentials != null && Credentials.ADMIN_ROLE.equals(credentials.getRole());
            boolean isOwner = utenteLoggato != null && recensione.getUtente().getId().equals(utenteLoggato.getId());

            if (isAdmin || isOwner) {
                recensioneService.deleteById(id);
            }
            return "redirect:/film/" + filmId;
        }

        return "redirect:/films";
    }
}
