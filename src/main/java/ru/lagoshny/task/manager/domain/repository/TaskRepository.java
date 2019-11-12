package ru.lagoshny.task.manager.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.lagoshny.task.manager.domain.entity.Task;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.entity.User;

import java.util.List;

@RepositoryRestResource(path = "tasks")
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Count tasks for specified user and task category.
     *
     * @param user     {@link User} for whom need to count tasks
     * @param category {@link TaskCategory} in which there should be a task
     * @return number tasks for passed user and task category
     */
    Long countByAuthorAndCategory(User user, TaskCategory category);

    /**
     * Find all tasks for passed author.
     *
     * @param userId {@link User} for whom need to get tasks
     * @return page of {@link Task}'s
     */
    @RestResource(path = "allByAuthor", rel = "allByAuthor")
    Page<Task> findAllByAuthor_Id(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find all tasks for this user and task category.
     *
     * @param user     {@link User} for whom need to get tasks
     * @param category {@link TaskCategory} in which there should be a task
     * @return list of {@link Task} satisfying parameters
     */
    List<Task> findAllByAuthorAndCategory(User user, TaskCategory category);

}
