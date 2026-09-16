package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Film;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RecensioneRepository extends JpaRepository<Recensione, Long> {

    List<Recensione> findByFilmIdOrderByDataPubblicazioneDesc(Long filmId);

    List<Recensione> findByUtenteId(Long utenteId);

    boolean existsByUtenteAndFilm(User utente, Film film);

    Optional<Recensione> findByUtenteAndFilm(User utente, Film film);
}
