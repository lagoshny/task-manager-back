package ru.lagoshny.task.manager.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lagoshny.task.manager.domain.entity.Task;
import ru.lagoshny.task.manager.web.service.TaskService;
import ru.lagoshny.task.manager.web.validation.ValidResource;

import java.util.Optional;

/**
 * Overrides default rest endpoints and adding new ones for {@link Task} resource.
 */
@RepositoryRestController
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResourceSupport> createTask(@RequestBody @ValidResource Resource<Task> resourceTask,
                                                      PersistentEntityResourceAssembler entityResourceAssembler) {
        final Task savedTask = taskService.createTask(resourceTask.getContent());

        return ResponseEntity.of(Optional.of(entityResourceAssembler.toResource(savedTask)));
    }

    @PatchMapping("/tasks/{id}")
    public ResponseEntity<Resource<Task>> updateTask(@PathVariable("id") Long taskIdToUpdate,
                                                     @RequestBody @ValidResource Resource<Task> resourceTask,
                                                     PersistentEntityResourceAssembler entityResourceAssembler) {
        final Task updatedTask = taskService.updateTask(taskIdToUpdate, resourceTask.getContent());

        final Resource<Task> taskResource = new Resource<>(updatedTask);
        taskResource.add(entityResourceAssembler.toResource(updatedTask).getLinks());

        return ResponseEntity.ok(taskResource);
    }

}
