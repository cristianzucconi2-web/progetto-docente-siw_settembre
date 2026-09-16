package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Film;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.FilmRepository;
import it.uniroma3.siw.repository.RecensioneRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RecensioneService {

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Transactional(readOnly = true)
    public List<Recensione> findByFilmId(Long filmId) {
        return recensioneRepository.findByFilmIdOrderByDataPubblicazioneDesc(filmId);
    }

    @Transactional(readOnly = true)
    public List<Recensione> findAll() {
        return recensioneRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Recensione> findById(Long id) {
        return recensioneRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public boolean haGiaRecensito(User utente, Film film) {
        return recensioneRepository.existsByUtenteAndFilm(utente, film);
    }

    @Transactional
    public Recensione salvaRecensione(Long filmId, String testo, Integer voto, User utente) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film non trovato con ID: " + filmId));

        if (recensioneRepository.existsByUtenteAndFilm(utente, film)) {
            throw new IllegalStateException("Hai già inserito una recensione per questo film.");
        }

        Recensione recensione = new Recensione();
        recensione.setFilm(film);
        recensione.setUtente(utente);
        recensione.setTesto(testo);
        recensione.setVoto(voto);
        recensione.setDataPubblicazione(LocalDate.now());

        return recensioneRepository.save(recensione);
    }

    @Transactional
    public Recensione modificaRecensione(Long recensioneId, String nuovoTesto, Integer nuovoVoto, User utenteLoggato) {
        Recensione recensione = recensioneRepository.findById(recensioneId)
                .orElseThrow(() -> new EntityNotFoundException("Recensione non trovata con ID: " + recensioneId));

        if (!recensione.getUtente().getId().equals(utenteLoggato.getId())) {
            throw new SecurityException("Non sei autorizzato a modificare questa recensione.");
        }

        recensione.setTesto(nuovoTesto);
        if (nuovoVoto != null) {
            recensione.setVoto(nuovoVoto);
        }
        recensione.setDataPubblicazione(LocalDate.now());

        return recensioneRepository.save(recensione);
    }

    @Transactional
    public void deleteById(Long id) {
        recensioneRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() {
        return recensioneRepository.count();
    }
}
