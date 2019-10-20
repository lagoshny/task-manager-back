package ru.lagoshny.task.manager.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.lagoshny.task.manager.domain.entity.Task;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.entity.User;

@RepositoryRestResource(path = "tasks")
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Count tasks for specified user and task category.
     *
     * @param user     {@link User} for whom need to count tasks
     * @param category {@link TaskCategory} in which there should be a task
     * @return number tasks for this user and task category
     */
    Long countByAuthorAndCategory(User user, TaskCategory category);

}
