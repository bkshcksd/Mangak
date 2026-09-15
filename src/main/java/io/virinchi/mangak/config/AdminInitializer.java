package io.virinchi.mangak.config;

import io.virinchi.mangak.model.User;
import io.virinchi.mangak.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserService userService;


    public AdminInitializer(
            UserService userService
    ) {

        this.userService = userService;
    }


    // =========================================
    // CREATE DEFAULT ADMIN
    // =========================================

    @Override
    public void run(
            String... args
    ) {

        // Check if admin already exists
        if (userService.usernameExists("admin")) {

            System.out.println(
                    "Admin account already exists."
            );

            return;
        }


        // =========================================
        // CREATE ADMIN USER
        // =========================================

        User admin =
                new User();


        admin.setUsername(
                "admin"
        );


        admin.setEmail(
                "admin@mangak.local"
        );


        admin.setPassword(
                "admin"
        );


        admin.setRole(
                "ADMIN"
        );


        admin.setEmailVerified(
                true
        );

        userService.registerUser(
                admin
        );

        System.out.println(
                "Default admin account created."
        );
    }
}