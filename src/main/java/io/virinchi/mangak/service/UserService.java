package io.virinchi.mangak.service;

import io.virinchi.mangak.model.User;
import io.virinchi.mangak.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();


    public UserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public Optional<User> getUserById(
            Long id
    ) {

        return userRepository.findById(
                id
        );
    }

    public Optional<User> findByUsername(
            String username
    ) {

        return userRepository.findByUsername(
                username
        );
    }

    public Optional<User> findByEmail(
            String email
    ) {

        return userRepository.findByEmail(
                email
        );
    }

    public boolean usernameExists(
            String username
    ) {

        return userRepository.existsByUsername(
                username
        );
    }

    public boolean emailExists(
            String email
    ) {

        return userRepository.existsByEmail(
                email
        );
    }

    public User registerUser(
            User user
    ) {

        String hashedPassword =
                passwordEncoder.encode(
                        user.getPassword()
                );


        user.setPassword(
                hashedPassword
        );


        return userRepository.save(
                user
        );
    }

    public boolean checkPassword(
            String rawPassword,
            String hashedPassword
    ) {

        return passwordEncoder.matches(
                rawPassword,
                hashedPassword
        );
    }


    // =========================================
    // SAVE EXISTING USER
    // =========================================

    public User saveUser(
            User user
    ) {

        return userRepository.save(
                user
        );
    }

    public void deleteUser(
            Long id
    ) {

        userRepository.deleteById(
                id
        );
    }
}