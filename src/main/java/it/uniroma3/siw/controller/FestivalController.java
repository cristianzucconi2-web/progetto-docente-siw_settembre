package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Festival;
import it.uniroma3.siw.model.Film;
import it.uniroma3.siw.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class FestivalController {

    @Autowired
    private FestivalService festivalService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private ProiezioneService proiezioneService;

    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/festivals")
    public String getFestivals(Model model) {
        model.addAttribute("festivals", festivalService.findAll());
        model.addAttribute("count", festivalService.count());
        return "festivals.html";
    }

    @GetMapping("/festival/{id}")
    public String getFestival(@PathVariable("id") Long id, Model model) {
        this.addCredentialsToModel(model);
        Festival festival = festivalService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Festival non trovato con ID: " + id));

        model.addAttribute("festival", festival);
        model.addAttribute("filmList", filmService.findByFestivalId(id));
        model.addAttribute("proiezioni", proiezioneService.findByFestivalId(id));
        model.addAttribute("tuttiFilm", filmService.findAll());
        
        
        model.addAttribute("filmTotali", filmService.count());
        model.addAttribute("filmFestival", filmService.findByFestivalId(id).size());
        return "festival.html";
    }

    @GetMapping("/admin/nuovoFestival")
    public String formNuovoFestival(Model model) {
        model.addAttribute("festival", new Festival());
        return "admin/festivalForm.html";
    }

    @PostMapping("/admin/nuovoFestival")
    public String salvaNuovoFestival(@Valid @ModelAttribute("festival") Festival festival, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/festivalForm.html";
        }
        festivalService.save(festival);
        return "redirect:/festivals";
    }

    @GetMapping("/admin/modificaFestival/{id}")
    public String formModificaFestival(@PathVariable("id") Long id, Model model) {
        Festival festival = festivalService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Festival non trovato con ID: " + id));
        model.addAttribute("festival", festival);
        return "admin/modificaFestival.html";
    }

    @PostMapping("/admin/modificaFestival/{id}")
    public String salvaModificaFestival(@Valid @ModelAttribute("festival") Festival festival,
                                        BindingResult result,
                                        @PathVariable("id") Long id) {
        if (result.hasErrors()) {
            return "admin/modificaFestival.html";
        }
        Festival esistente = festivalService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Festival non trovato con ID: " + id));

        esistente.setNome(festival.getNome());
        esistente.setAnno(festival.getAnno());
        esistente.setCitta(festival.getCitta());
        esistente.setDataInizio(festival.getDataInizio());
        esistente.setDataFine(festival.getDataFine());
        esistente.setDescrizione(festival.getDescrizione());

        festivalService.save(esistente);
        return "redirect:/festivals";
    }

    @PostMapping("/admin/eliminaFestival/{id}")
    public String eliminaFestival(@PathVariable("id") Long id) {
        festivalService.deleteById(id);
        return "redirect:/festivals";
    }

    @PostMapping("/admin/festival/{id}/aggiungiFilm/{filmId}")
    public String aggiungiFilmAFestival(@PathVariable("id") Long id, @PathVariable("filmId") Long filmId) {
        Film film = filmService.findById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film non trovato con ID: " + filmId));
        festivalService.aggiungiFilm(id, film);
        return "redirect:/festival/" + id;
    }

    @PostMapping("/admin/festival/{id}/rimuoviFilm/{filmId}")
    public String rimuoviFilmDaFestival(@PathVariable("id") Long id, @PathVariable("filmId") Long filmId) {
        Film film = filmService.findById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film non trovato con ID: " + filmId));
        festivalService.rimuoviFilm(id, film);
        return "redirect:/festival/" + id;
    }

    private void addCredentialsToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("credentials", credentials);
        }
    }
}
