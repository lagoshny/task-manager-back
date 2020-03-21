package ru.lagoshny.task.manager.web.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.lagoshny.task.manager.security.AuthenticatedUser;

/**
 * Authentication user service, for convenient work with {@link SecurityContextHolder}.
 */
@Service
public class AuthenticationService {

    /**
     * Get current {@link Authentication}.
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Get current authorized user {@link AuthenticatedUser}.
     */
    public AuthenticatedUser getAuthenticatedUser() {
        return (AuthenticatedUser) getAuthentication().getPrincipal();
    }

}
