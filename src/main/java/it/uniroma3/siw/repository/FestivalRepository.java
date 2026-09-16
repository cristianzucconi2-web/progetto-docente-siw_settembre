package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
    List<Festival> findByNomeContainingIgnoreCase(String nome);
    List<Festival> findByAnno(Integer anno);
    List<Festival> findByCittaContainingIgnoreCase(String citta);
    
}
