package ru.lagoshny.task.manager.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.lagoshny.task.manager.domain.entity.User;
import ru.lagoshny.task.manager.domain.validator.group.RegistrationGroup;
import ru.lagoshny.task.manager.web.service.UserService;
import ru.lagoshny.task.manager.web.validation.ValidResource;

import javax.validation.groups.Default;

/**
 * Overrides default rest endpoints and adding new ones for {@link User} resource.
 */
@RepositoryRestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<Resource<?>> createUser(
            @RequestBody @ValidResource({Default.class, RegistrationGroup.class}) final User user,
            final PersistentEntityResourceAssembler entityResourceAssembler) {
        final User savedUser = userService.saveUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(entityResourceAssembler.toResource(savedUser));
    }

}
