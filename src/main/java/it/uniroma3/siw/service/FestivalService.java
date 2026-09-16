package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Festival;
import it.uniroma3.siw.model.Film;
import it.uniroma3.siw.repository.FestivalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class FestivalService {

    @Autowired
    private FestivalRepository festivalRepository;

    @Transactional(readOnly = true)
    public List<Festival> findAll() {
        return festivalRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Festival> findById(Long id) {
        return festivalRepository.findById(id);
    }
    
    
    @Transactional
    public List<Festival> findByAnno(Integer anno) {
        return festivalRepository.findByAnno(anno);
    }

    @Transactional
    public Festival save(Festival festival) {
        return festivalRepository.save(festival);
    }

    @Transactional
    public void deleteById(Long id) {
        festivalRepository.deleteById(id);
    }

    @Transactional
    public void aggiungiFilm(Long festivalId, Film film) {
        Festival festival = festivalRepository.findById(festivalId).orElseThrow();
        if (!festival.getFilm().contains(film)) {
            festival.getFilm().add(film);
            festivalRepository.save(festival);
        }
    }

    @Transactional
    public void rimuoviFilm(Long festivalId, Film film) {
        Festival festival = festivalRepository.findById(festivalId).orElseThrow();
        festival.getFilm().remove(film);
        if (festival.getProiezioni() != null) {
            festival.getProiezioni().removeIf(p -> p.getFilm() != null && p.getFilm().equals(film));
        }
        festivalRepository.save(festival);
    }

    @Transactional(readOnly = true)
    public long count() {
        return festivalRepository.count();
    }
}
