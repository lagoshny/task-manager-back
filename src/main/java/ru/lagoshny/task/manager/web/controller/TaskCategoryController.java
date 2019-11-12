package ru.lagoshny.task.manager.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.web.service.TaskCategoryService;

/**
 * Overrides default rest endpoints and adding new ones for {@link TaskCategory} resource.
 */
@RepositoryRestController
public class TaskCategoryController {

    private final TaskCategoryService taskCategoryService;

    @Autowired
    public TaskCategoryController(final TaskCategoryService taskCategoryService) {
        this.taskCategoryService = taskCategoryService;
    }

    @DeleteMapping("/task-categories/{id}")
    public ResponseEntity<?> delete(@PathVariable final Long id) {
        taskCategoryService.deleteCategoryById(id);

        return ResponseEntity.ok().build();
    }

}
