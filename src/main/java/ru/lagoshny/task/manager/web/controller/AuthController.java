package ru.lagoshny.task.manager.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import ru.lagoshny.task.manager.domain.entity.User;
import ru.lagoshny.task.manager.domain.repository.UserRepository;
import ru.lagoshny.task.manager.security.AuthenticatedUser;
import ru.lagoshny.task.manager.web.service.AuthenticationService;

/**
 * Controller contains all methods to user authorization management.
 */
@BasePathAwareController
public class AuthController {

    private final AuthenticationService authenticationService;

    private final UserRepository userRepository;

    @Autowired
    public AuthController(AuthenticationService authenticationService,
                          UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    /**
     * Get authorized user's data.
     */
    @PostMapping("/auth/user")
    public ResponseEntity<PersistentEntityResource> user(PersistentEntityResourceAssembler entityResourceAssembler) {
        final AuthenticatedUser authenticatedUser = authenticationService.getAuthenticatedUser();
        final User authUserData = userRepository.findByUsername(authenticatedUser.getUsername());

        return ResponseEntity.ok(entityResourceAssembler.toModel(authUserData));
    }

}
