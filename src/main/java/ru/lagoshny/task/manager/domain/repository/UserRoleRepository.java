package ru.lagoshny.task.manager.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.lagoshny.task.manager.domain.entity.User;
import ru.lagoshny.task.manager.domain.entity.UserRole;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    /**
     * Find all {@link UserRole}'s by user.
     *
     * @param user {@link User} object
     * @return list of {@link UserRole} for this user
     */
    List<UserRole> findAllByUser(User user);

}
