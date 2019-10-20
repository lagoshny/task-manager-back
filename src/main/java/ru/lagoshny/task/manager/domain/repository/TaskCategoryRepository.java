package ru.lagoshny.task.manager.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;

@RepositoryRestResource(path = "task-categories")
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {
}
