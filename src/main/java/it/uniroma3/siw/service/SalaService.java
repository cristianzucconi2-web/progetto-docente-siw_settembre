package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Sala;
import it.uniroma3.siw.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    @Transactional(readOnly = true)
    public List<Sala> findAll() {
        return salaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Sala> findById(Long id) {
        return salaRepository.findById(id);
    }

    @Transactional
    public Sala save(Sala sala) {
        return salaRepository.save(sala);
    }

    @Transactional
    public void deleteById(Long id) {
        salaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long count() {
        return salaRepository.count();
    }
}
