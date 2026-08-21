package io.virinchi.mangak.controller;

import io.virinchi.mangak.model.User;
import io.virinchi.mangak.service.EmailService;
import io.virinchi.mangak.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;


    public AuthController(
            UserService userService,
            EmailService emailService
    ) {
        this.userService = userService;
        this.emailService = emailService;
    }


    // =========================================
    // LOGIN PAGE
    // =========================================

    @GetMapping("/login")
    public String loginPage(
            HttpSession session
    ) {

        // If already logged in, send user
        // to the correct page
        if (session.getAttribute("userId") != null) {

            Object role =
                    session.getAttribute("role");


            if (role != null &&
                    "ADMIN".equalsIgnoreCase(
                            role.toString()
                    )) {

                return "redirect:/admin/manga/1";
            }


            return "redirect:/mangak";
        }


        return "login";
    }


    // =========================================
    // LOGIN
    // =========================================

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {

        User user =
                userService.findByUsername(
                                username
                        )
                        .orElse(null);


        // =========================================
        // USER NOT FOUND
        // =========================================

        if (user == null) {

            model.addAttribute(
                    "error",
                    "Username or password is incorrect."
            );

            return "login";
        }


        // =========================================
        // CHECK PASSWORD
        // =========================================

        if (!userService.checkPassword(
                password,
                user.getPassword()
        )) {

            model.addAttribute(
                    "error",
                    "Username or password is incorrect."
            );

            return "login";
        }


        // =========================================
        // CREATE LOGIN SESSION
        // =========================================

        session.setAttribute(
                "userId",
                user.getId()
        );


        session.setAttribute(
                "username",
                user.getUsername()
        );


        session.setAttribute(
                "role",
                user.getRole()
        );


        // =========================================
        // ADMIN LOGIN
        // =========================================

        if ("ADMIN".equalsIgnoreCase(
                user.getRole()
        )) {

            return "redirect:/admin/manga/1";
        }


        // =========================================
        // NORMAL USER LOGIN
        // =========================================

        return "redirect:/mangak";
    }


    // =========================================
    // SIGNUP PAGE
    // =========================================

    @GetMapping("/signup")
    public String signupPage(
            HttpSession session
    ) {

        // Logged-in users do not need
        // the signup page
        if (session.getAttribute("userId") != null) {

            Object role =
                    session.getAttribute("role");


            if (role != null &&
                    "ADMIN".equalsIgnoreCase(
                            role.toString()
                    )) {

                return "redirect:/admin/manga/1";
            }


            return "redirect:/mangak";
        }


        return "signup";
    }


    // =========================================
    // SIGNUP
    // =========================================

    @PostMapping("/signup")
    public String signup(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model
    ) {


        // =========================================
        // CHECK EMPTY VALUES
        // =========================================

        if (username == null ||
                username.trim().isEmpty() ||
                email == null ||
                email.trim().isEmpty() ||
                password == null ||
                password.isEmpty()) {

            model.addAttribute(
                    "error",
                    "All fields are required."
            );

            return "signup";
        }


        // =========================================
        // CHECK PASSWORD MATCH
        // =========================================

        if (!password.equals(
                confirmPassword
        )) {

            model.addAttribute(
                    "error",
                    "Passwords do not match."
            );

            return "signup";
        }


        // =========================================
        // CHECK USERNAME
        // =========================================

        if (userService.usernameExists(
                username.trim()
        )) {

            model.addAttribute(
                    "error",
                    "Username already exists."
            );

            return "signup";
        }


        // =========================================
        // CHECK EMAIL
        // =========================================

        if (userService.emailExists(
                email.trim()
        )) {

            model.addAttribute(
                    "error",
                    "Email already exists."
            );

            return "signup";
        }


        // =========================================
        // PREVENT NORMAL SIGNUP FROM
        // CREATING ADMIN
        // =========================================

        if ("admin".equalsIgnoreCase(
                username.trim()
        )) {

            model.addAttribute(
                    "error",
                    "This username is reserved."
            );

            return "signup";
        }


        // =========================================
        // CREATE NORMAL USER
        // =========================================

        User user =
                new User();


        user.setUsername(
                username.trim()
        );


        user.setEmail(
                email.trim()
        );


        user.setPassword(
                password
        );


        user.setRole(
                "USER"
        );


        user.setEmailVerified(
                false
        );


        // =========================================
        // SAVE USER
        // =========================================

        userService.registerUser(
                user
        );


        // =========================================
        // SEND SMTP WELCOME EMAIL
        // =========================================

        try {

            emailService.sendWelcomeEmail(
                    user.getEmail(),
                    user.getUsername()
            );

        } catch (Exception e) {

            System.out.println(
                    "Could not send welcome email: "
                            + e.getMessage()
            );
        }


        // =========================================
        // SIGNUP SUCCESS
        // =========================================

        return "redirect:/login";
    }


    // =========================================
    // LOGOUT
    // =========================================

    @GetMapping("/logout")
    public String logout(
            HttpSession session
    ) {

        session.invalidate();

        return "redirect:/login";
    }
}