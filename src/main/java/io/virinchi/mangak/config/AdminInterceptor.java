package io.virinchi.mangak.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        HttpSession session =
                request.getSession(false);

        if (session == null ||
                session.getAttribute("userId") == null) {

            response.sendRedirect("/login");

            return false;
        }

        Object roleObject =
                session.getAttribute("role");


        if (roleObject == null ||
                !"ADMIN".equalsIgnoreCase(
                        roleObject.toString()
                )) {

            response.sendRedirect("/mangak");

            return false;
        }
        return true;
    }
}