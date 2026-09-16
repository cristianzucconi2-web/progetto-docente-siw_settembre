package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Sala;
import it.uniroma3.siw.service.ProiezioneService;
import it.uniroma3.siw.service.SalaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class SalaController {

    @Autowired
    private SalaService salaService;
    @Autowired
    private ProiezioneService proiezioneService;
    @GetMapping("/sale")
    public String getSale(Model model) {
        model.addAttribute("sale", salaService.findAll());
        return "sale.html";
    }

    @GetMapping("/sala/{id}")
    public String getSala(@PathVariable("id") Long id, Model model) {
        Sala sala = salaService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sala non trovata con ID: " + id));
        model.addAttribute("sala", sala);
        model.addAttribute("proiezioni", sala.getProiezioni().size());
        model.addAttribute("totale_proiezioni", proiezioneService.countBySalaId(id));
        
        model.addAttribute("proiezioni_totali", proiezioneService.count());
        model.addAttribute("proiezioni_in_sala", proiezioneService.countBySalaId(id));

        return "sala.html";
    }

    @GetMapping("/admin/nuovaSala")
    public String formNuovaSala(Model model) {
        model.addAttribute("sala", new Sala());
        return "admin/salaForm.html";
    }

    @PostMapping("/admin/nuovaSala")
    public String salvaNuovaSala(@Valid @ModelAttribute("sala") Sala sala, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/salaForm.html";
        }
        salaService.save(sala);
        return "redirect:/sale";
    }

    @GetMapping("/admin/modificaSala/{id}")
    public String formModificaSala(@PathVariable("id") Long id, Model model) {
        Sala sala = salaService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sala non trovata con ID: " + id));
        model.addAttribute("sala", sala);
        return "admin/modificaSala.html";
    }

    @PostMapping("/admin/modificaSala/{id}")
    public String salvaModificaSala(@Valid @ModelAttribute("sala") Sala sala,
                                    BindingResult result,
                                    @PathVariable("id") Long id) {
        if (result.hasErrors()) {
            return "admin/modificaSala.html";
        }
        Sala esistente = salaService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sala non trovata con ID: " + id));

        esistente.setNome(sala.getNome());
        esistente.setIndirizzo(sala.getIndirizzo());
        esistente.setCapienza(sala.getCapienza());

        salaService.save(esistente);
        return "redirect:/sale";
    }

    @PostMapping("/admin/eliminaSala/{id}")
    public String eliminaSala(@PathVariable("id") Long id) {
        salaService.deleteById(id);
        return "redirect:/sale";
    }
}
