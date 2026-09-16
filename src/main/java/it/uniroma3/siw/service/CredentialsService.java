package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class CredentialsService {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected CredentialsRepository credentialsRepository;

    @Transactional(readOnly = true)
    public Optional<Credentials> getCredentials(Long id) {
        return this.credentialsRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Credentials getCredentials(String username) {
        Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
        return result.orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return this.credentialsRepository.existsByUsername(username);
    }

    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        if (credentials.getRole() == null) {
            credentials.setRole(Credentials.DEFAULT_ROLE);
        }
        if (credentials.getId() == null) {
            credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        }
        return this.credentialsRepository.save(credentials);
    }
}
