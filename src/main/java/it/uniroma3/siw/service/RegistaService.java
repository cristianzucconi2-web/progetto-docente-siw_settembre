package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Regista;
import it.uniroma3.siw.repository.RegistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class RegistaService {

    @Autowired
    private RegistaRepository registaRepository;

    @Transactional(readOnly = true)
    public List<Regista> findAll() {
        return registaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Regista> findById(Long id) {
        return registaRepository.findById(id);
    }

    @Transactional
    public Regista save(Regista regista) {
        return registaRepository.save(regista);
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Regista> opt = registaRepository.findById(id);
        if (opt.isPresent()) {
            Regista regista = opt.get();
            if (regista.getFilm() != null) {
                for (it.uniroma3.siw.model.Film film : java.util.List.copyOf(regista.getFilm())) {
                    if (film.getFestival() != null) {
                        for (it.uniroma3.siw.model.Festival festival : film.getFestival()) {
                            festival.getFilm().remove(film);
                        }
                    }
                }
            }
            registaRepository.delete(regista);
        }
    }

    @Transactional(readOnly = true)
    public long count() {
        return registaRepository.count();
    }
}
