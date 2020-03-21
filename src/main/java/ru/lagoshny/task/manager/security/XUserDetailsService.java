package ru.lagoshny.task.manager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.lagoshny.task.manager.domain.entity.User;
import ru.lagoshny.task.manager.domain.entity.UserRole;
import ru.lagoshny.task.manager.domain.repository.UserRepository;
import ru.lagoshny.task.manager.domain.repository.UserRoleRepository;

import java.util.List;

@Service
public class XUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public XUserDetailsService(UserRepository userRepository,
                               UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username);
        final List<UserRole> userRoles = userRoleRepository.findAllByUser(user);
        final String[] rolesAsArray =
                userRoles.stream()
                        .map(userRole -> userRole.getRole().getName())
                        .toArray(String[]::new);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user " + username);
        }

        return new AuthenticatedUser(user, AuthorityUtils.createAuthorityList(rolesAsArray));
    }

}
