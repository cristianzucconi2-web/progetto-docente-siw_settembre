package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Proiezione;
import it.uniroma3.siw.model.Sala;
import it.uniroma3.siw.model.StatoProiezione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ProiezioneRepository extends JpaRepository<Proiezione, Long> {

    List<Proiezione> findByFestivalIdOrderByDataAscOraAsc(Long festivalId);

    List<Proiezione> findByFilmIdOrderByDataAscOraAsc(Long filmId);

    List<Proiezione> findBySalaIdOrderByDataAscOraAsc(Long salaId);

    List<Proiezione> findByDataOrderByOraAsc(LocalDate data);

    List<Proiezione> findByStato(StatoProiezione stato);
    long countBySalaId(Long salaId);


    List<Proiezione> findBySalaAndDataAndStatoNot(Sala sala, LocalDate data, StatoProiezione stato);

    @Query("SELECT p FROM Proiezione p WHERE p.sala.id = :salaId AND p.data = :data AND p.stato <> 'CANCELLED'")
    List<Proiezione> findAttiveInSalaEData(@Param("salaId") Long salaId, @Param("data") LocalDate data);
}
