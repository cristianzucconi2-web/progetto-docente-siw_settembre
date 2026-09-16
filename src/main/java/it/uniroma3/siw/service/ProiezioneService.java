package it.uniroma3.siw.service;

import it.uniroma3.siw.model.*;
import it.uniroma3.siw.repository.FestivalRepository;
import it.uniroma3.siw.repository.FilmRepository;
import it.uniroma3.siw.repository.ProiezioneRepository;
import it.uniroma3.siw.repository.SalaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProiezioneService {

    @Autowired
    private ProiezioneRepository proiezioneRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private SalaRepository salaRepository;

    @Transactional(readOnly = true)
    public List<Proiezione> findAll() {
        return proiezioneRepository.findAll();
    }

    @Transactional(readOnly = true)
    public long countBySalaId(Long salaId) {
        return proiezioneRepository.countBySalaId(salaId);
    }
    @Transactional(readOnly = true)
    public Optional<Proiezione> findById(Long id) {
        return proiezioneRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Proiezione> findByFestivalId(Long festivalId) {
        return proiezioneRepository.findByFestivalIdOrderByDataAscOraAsc(festivalId);
    }

    @Transactional(readOnly = true)
    public List<Proiezione> findByFilmId(Long filmId) {
        return proiezioneRepository.findByFilmIdOrderByDataAscOraAsc(filmId);
    }

    @Transactional(readOnly = true)
    public List<Proiezione> findBySalaId(Long salaId) {
        return proiezioneRepository.findBySalaIdOrderByDataAscOraAsc(salaId);
    }

    @Transactional
    public Proiezione save(Proiezione proiezione) {
        return proiezioneRepository.save(proiezione);
    }

    @Transactional
    public void deleteById(Long id) {
        proiezioneRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() {
        return proiezioneRepository.count();
    }

    /**
     * Operazione transazionale atomica per la programmazione di una proiezione
     * (Requisito Sezione 7 della traccia).
     *
     * 1. Recupero del festival
     * 2. Recupero del film
     * 3. Recupero della sala
     * 4. Verifica della disponibilità della sala (nessuna sovrapposizione oraria)
     * 5. Creazione della proiezione
     * 6. Aggiornamento e persistenza dello stato consistente
     */
    @Transactional
    public Proiezione programmaNuovaProiezione(Long festivalId, Long filmId, Long salaId,
                                                LocalDate data, LocalTime ora) {

        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new EntityNotFoundException("Festival non trovato con ID: " + festivalId));

        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new EntityNotFoundException("Film non trovato con ID: " + filmId));

        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new EntityNotFoundException("Sala non trovata con ID: " + salaId));

        // Controllo consistenza: la data della proiezione deve essere all'interno del periodo del festival se specificato
        if (festival.getDataInizio() != null && data.isBefore(festival.getDataInizio())) {
            throw new IllegalArgumentException("La data della proiezione non può essere antecedente all'inizio del festival (" + festival.getDataInizio() + ")");
        }
        if (festival.getDataFine() != null && data.isAfter(festival.getDataFine())) {
            throw new IllegalArgumentException("La data della proiezione non può essere successiva alla fine del festival (" + festival.getDataFine() + ")");
        }

        // Calcolo intervallo orario della nuova proiezione (inizio e fine stimata in base alla durata del film + 15 min pausa)
        int durataFilm = (film.getDurata() != null) ? film.getDurata() : 120;
        LocalTime oraFineNuova = ora.plusMinutes(durataFilm + 15);

        // Controllo sovrapposizioni nella stessa sala nella stessa data
        List<Proiezione> proiezioniInSala = proiezioneRepository.findAttiveInSalaEData(salaId, data);
        for (Proiezione p : proiezioniInSala) {
            int durataEsistente = (p.getFilm() != null && p.getFilm().getDurata() != null) ? p.getFilm().getDurata() : 120;
            LocalTime inizioEsistente = p.getOra();
            LocalTime fineEsistente = inizioEsistente.plusMinutes(durataEsistente + 15);

            // Verifica se gli intervalli [ora, oraFineNuova] e [inizioEsistente, fineEsistente] si sovrappongono
            boolean overlap = !ora.isAfter(fineEsistente) && !oraFineNuova.isBefore(inizioEsistente);
            if (overlap) {
                throw new IllegalStateException("Conflitto di programmazione: la sala '" + sala.getNome() +
                        "' è già occupata dalle " + inizioEsistente + " alle " + fineEsistente + " per il film '" +
                        (p.getFilm() != null ? p.getFilm().getTitolo() : "Altro") + "'.");
            }
        }

        // Creazione e salvataggio
        Proiezione nuovaProiezione = new Proiezione();
        nuovaProiezione.setData(data);
        nuovaProiezione.setOra(ora);
        nuovaProiezione.setStato(StatoProiezione.SCHEDULED);
        nuovaProiezione.setFestival(festival);
        nuovaProiezione.setFilm(film);
        nuovaProiezione.setSala(sala);

        // Assicura l'associazione film-festival
        if (!festival.getFilm().contains(film)) {
            festival.getFilm().add(film);
            festivalRepository.save(festival);
        }

        return proiezioneRepository.save(nuovaProiezione);
    }
}
