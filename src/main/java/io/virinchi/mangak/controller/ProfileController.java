package io.virinchi.mangak.controller;

import io.virinchi.mangak.model.User;
import io.virinchi.mangak.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class ProfileController {

    private final UserRepository userRepository;


    public ProfileController(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String profile(
            HttpSession session,
            Model model
    ) {

        Long userId =
                (Long) session.getAttribute("userId");


        if (userId == null) {
            return "redirect:/login";
        }


        Optional<User> userOptional =
                userRepository.findById(userId);


        if (userOptional.isEmpty()) {
            return "redirect:/login";
        }


        User user =
                userOptional.get();


        // =========================================
        // SEND USER TO THYMELEAF
        // =========================================

        model.addAttribute(
                "user",
                user
        );


        model.addAttribute(
                "username",
                user.getUsername()
        );


        model.addAttribute(
                "role",
                user.getRole()
        );


        return "profile";
    }
}