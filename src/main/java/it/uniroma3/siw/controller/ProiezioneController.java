package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.*;
import it.uniroma3.siw.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class ProiezioneController {

    @Autowired
    private ProiezioneService proiezioneService;

    @Autowired
    private FestivalService festivalService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private SalaService salaService;

    @GetMapping("/proiezioni")
    public String getProiezioni(Model model) {
        model.addAttribute("proiezioni", proiezioneService.findAll());
        return "proiezioni.html";
    }

    @GetMapping("/admin/nuovaProiezione")
    public String formNuovaProiezione(Model model) {
        model.addAttribute("proiezione", new Proiezione());
        model.addAttribute("festivals", festivalService.findAll());
        model.addAttribute("films", filmService.findAll());
        model.addAttribute("sale", salaService.findAll());
        return "admin/proiezioneForm.html";
    }

    @PostMapping("/admin/nuovaProiezione")
    public String programmaProiezione(@RequestParam("festivalId") Long festivalId,
                                      @RequestParam("filmId") Long filmId,
                                      @RequestParam("salaId") Long salaId,
                                      @RequestParam("data") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate data,
                                      @RequestParam("ora") @DateTimeFormat(pattern = "HH:mm") LocalTime ora,
                                      Model model) {
        try {
            proiezioneService.programmaNuovaProiezione(festivalId, filmId, salaId, data, ora);
            return "redirect:/proiezioni";
        } catch (IllegalStateException | IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("proiezione", new Proiezione());
            model.addAttribute("festivals", festivalService.findAll());
            model.addAttribute("films", filmService.findAll());
            model.addAttribute("sale", salaService.findAll());
            return "admin/proiezioneForm.html";
        }
    }

    @PostMapping("/admin/eliminaProiezione/{id}")
    public String eliminaProiezione(@PathVariable("id") Long id) {
        proiezioneService.deleteById(id);
        return "redirect:/proiezioni";
    }
}
