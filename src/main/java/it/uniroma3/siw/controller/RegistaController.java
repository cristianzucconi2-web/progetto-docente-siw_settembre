package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Regista;
import it.uniroma3.siw.service.FilmService;
import it.uniroma3.siw.service.RegistaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistaController {

    @Autowired
    private RegistaService registaService;
    @Autowired

    private FilmService filmService;


    @GetMapping("/registi")
    public String getRegisti(Model model) {
        model.addAttribute("registi", registaService.findAll());
        return "registi.html";
    }

    @GetMapping("/regista/{id}")
    public String getRegista(@PathVariable("id") Long id, Model model) {
        Regista regista = registaService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Regista non trovato con ID: " + id));
        model.addAttribute("regista", regista);
        
        model.addAttribute("tuttiFilm", filmService.count());
        return "regista.html";
    }

    @GetMapping("/admin/nuovoRegista")
    public String formNuovoRegista(Model model) {
        model.addAttribute("regista", new Regista());
        return "admin/registaForm.html";
    }

    @PostMapping("/admin/nuovoRegista")
    public String salvaNuovoRegista(@Valid @ModelAttribute("regista") Regista regista, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/registaForm.html";
        }
        registaService.save(regista);
        return "redirect:/registi";
    }

    @GetMapping("/admin/modificaRegista/{id}")
    public String formModificaRegista(@PathVariable("id") Long id, Model model) {
        Regista regista = registaService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Regista non trovato con ID: " + id));
        model.addAttribute("regista", regista);
        return "admin/modificaRegista.html";
    }

    @PostMapping("/admin/modificaRegista/{id}")
    public String salvaModificaRegista(@Valid @ModelAttribute("regista") Regista regista,
                                       BindingResult result,
                                       @PathVariable("id") Long id) {
        if (result.hasErrors()) {
            return "admin/modificaRegista.html";
        }
        Regista esistente = registaService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Regista non trovato con ID: " + id));

        esistente.setNome(regista.getNome());
        esistente.setCognome(regista.getCognome());
        esistente.setDataNascita(regista.getDataNascita());
        esistente.setNazionalita(regista.getNazionalita());
        esistente.setBiografia(regista.getBiografia());

        registaService.save(esistente);
        return "redirect:/registi";
    }

    @PostMapping("/admin/eliminaRegista/{id}")
    public String eliminaRegista(@PathVariable("id") Long id) {
        registaService.deleteById(id);
        return "redirect:/registi";
    }
}
