package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Regista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistaRepository extends JpaRepository<Regista, Long> {
    List<Regista> findByCognomeContainingIgnoreCase(String cognome);
}
