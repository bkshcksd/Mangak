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


    // =========================================
    // GET ALL USERS
    // =========================================

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }


    // =========================================
    // GET USER BY ID
    // =========================================

    public Optional<User> getUserById(
            Long id
    ) {

        return userRepository.findById(
                id
        );
    }


    // =========================================
    // FIND USER BY USERNAME
    // =========================================

    public Optional<User> findByUsername(
            String username
    ) {

        return userRepository.findByUsername(
                username
        );
    }


    // =========================================
    // FIND USER BY EMAIL
    // =========================================

    public Optional<User> findByEmail(
            String email
    ) {

        return userRepository.findByEmail(
                email
        );
    }


    // =========================================
    // CHECK USERNAME EXISTS
    // =========================================

    public boolean usernameExists(
            String username
    ) {

        return userRepository.existsByUsername(
                username
        );
    }


    // =========================================
    // CHECK EMAIL EXISTS
    // =========================================

    public boolean emailExists(
            String email
    ) {

        return userRepository.existsByEmail(
                email
        );
    }


    // =========================================
    // REGISTER NEW USER
    // =========================================

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


    // =========================================
    // CHECK PASSWORD
    // =========================================

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


    // =========================================
    // DELETE USER
    // =========================================

    public void deleteUser(
            Long id
    ) {

        userRepository.deleteById(
                id
        );
    }
}