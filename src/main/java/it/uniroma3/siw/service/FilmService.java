package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Film;
import it.uniroma3.siw.repository.FilmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    private FilmRepository filmRepository;

    @Transactional(readOnly = true)
    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Film> findById(Long id) {
        return filmRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Film> findByAnno(Integer anno) {
        return filmRepository.findByAnno(anno);
    }

    @Transactional
    public Film save(Film film) {
        return filmRepository.save(film);
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Film> opt = filmRepository.findById(id);
        if (opt.isPresent()) {
            Film film = opt.get();
            if (film.getFestival() != null) {
                for (it.uniroma3.siw.model.Festival festival : film.getFestival()) {
                    festival.getFilm().remove(film);
                }
            }
            filmRepository.delete(film);
        }
    }

    @Transactional(readOnly = true)
    public List<Film> cercaPerKeyword(String keyword) {
        return filmRepository.findByTitoloOrGenereOrRegista(keyword);
    }

    @Transactional(readOnly = true)
    public List<Film> findByFestivalId(Long festivalId) {
        return filmRepository.findByFestivalId(festivalId);
    }

    @Transactional(readOnly = true)
    public long count() {
        return filmRepository.count();
    }

    // ================================================================
    // Metodi per l'analisi sperimentale delle strategie JPA
    // Caso d'uso: caricamento di tutti i film di un festival con registi
    // ================================================================

    @Transactional(readOnly = true)
    public List<Film> findFilmDiFestivalLazy(Long festivalId) {
        log.info("[PERF] Strategia LAZY: findByFestivalId senza JOIN FETCH");
        return filmRepository.findByFestivalId(festivalId);
    }

    @Transactional(readOnly = true)
    public List<Film> findFilmDiFestivalEagerLoad(Long festivalId) {
        log.info("[PERF] Strategia EAGER: caricamento e inizializzazione forzata (N+1)");
        List<Film> films = filmRepository.findByFestivalId(festivalId);
        for (Film f : films) {
            org.hibernate.Hibernate.initialize(f.getRegista());
        }
        return films;
    }

    @Transactional(readOnly = true)
    public List<Film> findFilmDiFestivalJoinFetch(Long festivalId) {
        log.info("[PERF] Strategia JOIN FETCH: query con JOIN FETCH su regista");
        return filmRepository.findByFestivalIdConRegistaJoinFetch(festivalId);
    }

    @Transactional(readOnly = true)
    public List<Film> findFilmDiFestivalEntityGraph(Long festivalId) {
        log.info("[PERF] Strategia @EntityGraph: query con EntityGraph");
        return filmRepository.findByFestivalIdConEntityGraph(festivalId);
    }
}
