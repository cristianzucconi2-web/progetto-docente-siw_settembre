package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findByNomeContainingIgnoreCase(String nome);
}
