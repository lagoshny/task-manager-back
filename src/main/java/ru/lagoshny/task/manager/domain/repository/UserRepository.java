package ru.lagoshny.task.manager.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.lagoshny.task.manager.domain.entity.User;

@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<User, Long> {
}
