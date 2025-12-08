package com.shop.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        System.out.println("Authenticated user: " + authentication.getName());
        System.out.println("Roles: " + roles);

        if (roles.contains("ROLE_ADMIN")) {
            System.out.println("Redirecting to /admin/dashboard");
            response.sendRedirect("/admin/dashboard");
        } else if (roles.contains("ROLE_USER")) {
            System.out.println("Redirecting to /user/dashboard");
            response.sendRedirect("/user/dashboard");
        } else {
            System.out.println("No recognized role found, redirecting to /");
            response.sendRedirect("/");
        }
    }
}
