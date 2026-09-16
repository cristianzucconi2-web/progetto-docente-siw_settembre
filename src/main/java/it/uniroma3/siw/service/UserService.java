package it.uniroma3.siw.service;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    protected UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUser(Long id) {
        Optional<User> result = this.userRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        Iterable<User> iterable = this.userRepository.findAll();
        for (User user : iterable)
            result.add(user);
        return result;
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public long count() {
        return this.userRepository.count();
    }
}
