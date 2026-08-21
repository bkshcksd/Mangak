package io.virinchi.mangak.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(
            InterceptorRegistry registry
    ) {

        // =========================================
        // NORMAL LOGGED-IN USER PROTECTION
        // =========================================

        registry.addInterceptor(
                        new SessionInterceptor()
                )
                .addPathPatterns(
                        "/mangak",
                        "/manga/**",
                        "/chapter/**",
                        "/my-list/**",
                        "/profile/**"
                )
                .excludePathPatterns(
                        "/login",
                        "/signup",
                        "/logout",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/favicon.ico"
                );


        // =========================================
        // ADMIN-ONLY PROTECTION
        // =========================================

        registry.addInterceptor(
                        new AdminInterceptor()
                )
                .addPathPatterns(
                        "/admin/**"
                );
    }
}