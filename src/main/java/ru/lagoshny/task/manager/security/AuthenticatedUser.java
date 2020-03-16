package ru.lagoshny.task.manager.security;

import org.springframework.security.core.GrantedAuthority;
import ru.lagoshny.task.manager.domain.entity.User;

import java.util.Collection;

/**
 * Adds additional information to user details.
 * Extends default Spring {@link org.springframework.security.core.userdetails.User}
 */
public class AuthenticatedUser extends org.springframework.security.core.userdetails.User {

    private Long id;

    public AuthenticatedUser(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.id = user.getId();
    }

    public Long getId() {
        return id;
    }

}
