package it.uniroma3.siw.controller;

import it.uniroma3.siw.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private FestivalService festivalService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private RegistaService registaService;

    @Autowired
    private SalaService salaService;

    @Autowired
    private ProiezioneService proiezioneService;

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String adminHome(Model model) {
        model.addAttribute("numFestivals", festivalService.count());
        model.addAttribute("numFilm", filmService.count());
        model.addAttribute("numRegisti", registaService.count());
        model.addAttribute("numSale", salaService.count());
        model.addAttribute("numProiezioni", proiezioneService.count());
        model.addAttribute("numUtenti", userService.count());
        return "admin/home.html";
    }
}
