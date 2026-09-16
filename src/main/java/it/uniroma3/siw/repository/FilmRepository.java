package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Film;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    List<Film> findByTitoloContainingIgnoreCase(String titolo);

    List<Film> findByGenereContainingIgnoreCase(String genere);

    List<Film> findByRegistaId(Long registaId);

    @Query("SELECT DISTINCT f FROM Film f LEFT JOIN FETCH f.regista LEFT JOIN FETCH f.festival WHERE LOWER(f.titolo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(f.genere) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(f.regista.cognome) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Film> findByTitoloOrGenereOrRegista(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT f FROM Film f JOIN f.festival fest WHERE fest.id = :festivalId")
    List<Film> findByFestivalId(@Param("festivalId") Long festivalId);

    // ============================================================
    // Query per analisi sperimentale delle prestazioni JPA
    // Caso d'uso: caricamento di tutti i film di un festival con i registi
    // ============================================================

    @Query("SELECT DISTINCT f FROM Film f JOIN FETCH f.regista JOIN f.festival fest WHERE fest.id = :festivalId")
    List<Film> findByFestivalIdConRegistaJoinFetch(@Param("festivalId") Long festivalId);

    @EntityGraph(attributePaths = {"regista", "festival"})
    @Query("SELECT f FROM Film f JOIN f.festival fest WHERE fest.id = :festivalId")
    List<Film> findByFestivalIdConEntityGraph(@Param("festivalId") Long festivalId);

	List<Film> findByAnno(Integer anno);
}
