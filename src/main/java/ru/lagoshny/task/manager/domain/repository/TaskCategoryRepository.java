package ru.lagoshny.task.manager.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.entity.User;

import java.util.List;

@RepositoryRestResource(path = "task-categories")
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {

    /**
     * Find task category for specified prefix and user.
     *
     * @param prefix for category which need to find
     * @param user who owner for this category
     * @return found {@link TaskCategory} or {@code null}
     */
    @RestResource(rel = "byPrefix", path = "byPrefix")
    TaskCategory findByPrefixIgnoreCaseAndUser(@Param("prefix") String prefix, @Param("user") User user);

    /**
     * Find all task categories to specified user.
     *
     * @param userId who owner of the categories
     * @return list of {@link TaskCategory} for user
     */
    @RestResource(rel = "allByUserId", path = "allByUserId")
    List<TaskCategory> findAllByUser_Id(@Param("userId") Long userId);

}
