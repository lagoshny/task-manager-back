package ru.lagoshny.task.manager.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.entity.User;

@RepositoryRestResource(path = "task-categories")
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {

    /**
     * Find task category for specified prefix and user.
     *
     * @param prefix for category which need to find
     * @param user   who owner for this category
     * @return found {@link TaskCategory} or {@code null}
     */
    TaskCategory findByPrefixAndUser(String prefix, User user);

}
