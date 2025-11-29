package ru.lagoshny.task.manager.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.lagoshny.task.manager.domain.entity.Task;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.entity.User;

import java.util.Collection;
import java.util.List;

@RepositoryRestResource(path = "tasks")
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Retrieve max task number for specified category and user.
     *
     * @param user     {@link User} for whom need to count tasks
     * @param category {@link TaskCategory} in which there should be a task
     * @return max task number for passed user and task category
     */
    @Query("select max(t.number) from Task t where t.author = :user and t.category = :category")
    Long maxTaskNumberByCategoryAndAuthor(@Param("user") User user, @Param("category") TaskCategory category);

    /**
     * Find all tasks for passed author.
     *
     * @param user {@link User} for whom need to get tasks
     * @return page of {@link Task}'s
     */
    @RestResource(path = "allByAuthor", rel = "allByAuthor")
    Page<Task> findAllByAuthor(@Param("user") User user, Pageable pageable);

    /**
     * Find task for user with passed number and category prefix.
     *
     * @param user           {@link User} for whom need to get task
     * @param number         task number within task category
     * @param categoryPrefix to define with category contains this task
     * @return found {@link Task}
     */
    @Query("SELECT t FROM Task t INNER JOIN TaskCategory tc ON tc.id = t.category.id "
            + "WHERE t.author = :user AND t.number = :number AND upper(tc.prefix) = upper(:categoryPrefix)")
    @RestResource(path = "byNumberAndCategory", rel = "byNumberAndCategory")
    Task findTask(@Param("user") User user,
                  @Param("number") Long number,
                  @Param("categoryPrefix") String categoryPrefix);

    /**
     * Find all tasks in the specified list of categories to author.
     *
     * @param userId        author of the tasks
     * @param categoriesIds list of ids categories
     * @param pageable      result pagination settings
     * @return list of the tasks as {@link Page} object
     */
    @RestResource(path = "allByAuthorAndCategories", rel = "allByAuthorAndCategories")
    Page<Task> findAllByAuthorIdAndCategoryIdIn(@Param("userId") Long userId,
                                                  @Param("categoriesIds") Collection<Long> categoriesIds,
                                                  Pageable pageable);

    /**
     * Find all tasks for this user and task category.
     *
     * @param user     {@link User} for whom need to get tasks
     * @param category {@link TaskCategory} in which there should be a task
     * @return list of {@link Task} satisfying parameters
     */
    List<Task> findAllByAuthorAndCategory(User user, TaskCategory category);

}
