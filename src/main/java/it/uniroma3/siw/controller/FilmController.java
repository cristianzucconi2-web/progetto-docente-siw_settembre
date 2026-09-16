package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Film;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

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
public class FilmController {

    @Autowired
    private FilmService filmService;

    @Autowired
    private RegistaService registaService;

    @Autowired
    private FestivalService festivalService;

    @Autowired
    private ProiezioneService proiezioneService;

    @Autowired
    private RecensioneService recensioneService;

    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/films")
    public String getFilms(Model model) {
        model.addAttribute("films", filmService.findAll());
        return "films.html";
    }

    @GetMapping("/film/{id}")
    public String getFilm(@PathVariable("id") Long id, Model model) {
        this.addCredentialsToModel(model);
        Film film = filmService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Film non trovato con ID: " + id));

        model.addAttribute("film", film);
        model.addAttribute("proiezioni", proiezioneService.findByFilmId(id));
        model.addAttribute("recensioni", recensioneService.findByFilmId(id));

        // Controllo se l'utente loggato ha già recensito questo film
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            if (credentials != null && credentials.getUser() != null) {
                boolean giaRecensito = recensioneService.haGiaRecensito(credentials.getUser(), film);
                model.addAttribute("giaRecensito", giaRecensito);
            }
        }
        
        
        List<Recensione> recensioni = recensioneService.findByFilmId(id);
        model.addAttribute("recensioni", recensioni);
        Double media = null;
        if (!recensioni.isEmpty()) {
            double somma = 0;
            for (Recensione r : recensioni) {
                somma += r.getVoto();
            }
            media = somma / recensioni.size();
        }
        model.addAttribute("media", media);
        
        
        
        
        
        
        /*double media=0;
        double somma=0;
        	if(!recensioneService.findByFilmId(id).isEmpty()) {
        		for (Recensione r : recensioneService.findByFilmId(id)) {
        			somma += r.getVoto();
        		}
        		media = somma/recensioneService.findByFilmId(id).size();
        	}
        	model.addAttribute("mediaVoti", media); */
        return "film.html";
    }

    @GetMapping("/cerca")
    public String cerca(Model model) {
        model.addAttribute("festivals", festivalService.findAll());
        return "cerca.html";
    }

    @GetMapping("/admin/nuovoFilm")
    public String formNuovoFilm(Model model) {
        model.addAttribute("film", new Film());
        model.addAttribute("registi", registaService.findAll());
        model.addAttribute("festivals", festivalService.findAll());
        return "admin/filmForm.html";
    }

    @PostMapping("/admin/nuovoFilm")
    public String salvaNuovoFilm(@Valid @ModelAttribute("film") Film film, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("registi", registaService.findAll());
            model.addAttribute("festivals", festivalService.findAll());
            return "admin/filmForm.html";
        }
        filmService.save(film);
        return "redirect:/films";
    }

    @GetMapping("/admin/modificaFilm/{id}")
    public String formModificaFilm(@PathVariable("id") Long id, Model model) {
        Film film = filmService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Film non trovato con ID: " + id));
        model.addAttribute("film", film);
        model.addAttribute("registi", registaService.findAll());
        model.addAttribute("festivals", festivalService.findAll());
        return "admin/modificaFilm.html";
    }

    @PostMapping("/admin/modificaFilm/{id}")
    public String salvaModificaFilm(@Valid @ModelAttribute("film") Film film,
                                    BindingResult result,
                                    @PathVariable("id") Long id,
                                    Model model) {
        if (result.hasErrors()) {
            model.addAttribute("registi", registaService.findAll());
            model.addAttribute("festivals", festivalService.findAll());
            return "admin/modificaFilm.html";
        }

        Film esistente = filmService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Film non trovato con ID: " + id));

        esistente.setTitolo(film.getTitolo());
        esistente.setAnno(film.getAnno());
        esistente.setDurata(film.getDurata());
        esistente.setGenere(film.getGenere());
        esistente.setPaeseProduzione(film.getPaeseProduzione());
        esistente.setTrama(film.getTrama());
        esistente.setRegista(film.getRegista());

        filmService.save(esistente);
        return "redirect:/films";
    }

    @PostMapping("/admin/eliminaFilm/{id}")
    public String eliminaFilm(@PathVariable("id") Long id) {
        filmService.deleteById(id);
        return "redirect:/films";
    }

    @GetMapping("/admin/films")
    public String getAdminFilms(Model model) {
        model.addAttribute("films", filmService.findAll());
        return "admin/films.html";
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
