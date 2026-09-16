package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Festival;
import it.uniroma3.siw.model.Film;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.service.FestivalService;
import it.uniroma3.siw.service.FilmService;
import it.uniroma3.siw.service.RecensioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FilmRestController {

    @Autowired
    private FilmService filmService;

    @Autowired
    private FestivalService festivalService;

    @Autowired
    private RecensioneService recensioneService;

    @GetMapping("/movies/ricerca")
    public List<Film> searchByKeyword(@RequestParam("q") String q) {
        return filmService.cercaPerKeyword(q);
    }
    
    /*

    @GetMapping("/festivals")
    public List<Festival> getAllFestivals() {
        return festivalService.findAll();
    }

    @GetMapping("/festivals/{id}/movies")
    public List<Film> getMoviesByFestival(@PathVariable("id") Long id) {
        return filmService.findByFestivalId(id);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Film> getMovieById(@PathVariable("id") Long id) {
        return filmService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/movies/{id}/reviews")
    public List<Recensione> getReviewsByMovie(@PathVariable("id") Long id) {
        return recensioneService.findByFilmId(id);
    }
    
    */
}
