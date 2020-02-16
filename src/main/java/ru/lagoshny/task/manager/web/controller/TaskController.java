package ru.lagoshny.task.manager.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.lagoshny.task.manager.domain.entity.Task;
import ru.lagoshny.task.manager.domain.entity.enums.TaskStatusEnum;
import ru.lagoshny.task.manager.domain.validator.group.ChangeTaskGroup;
import ru.lagoshny.task.manager.web.service.TaskService;
import ru.lagoshny.task.manager.web.validation.ValidResource;

import javax.validation.groups.Default;

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
    public ResponseEntity<ResourceSupport> createTask(@RequestBody @ValidResource Resource<Task> resourceTask,
                                                      PersistentEntityResourceAssembler entityResourceAssembler) {
        final Task savedTask = taskService.createTask(resourceTask.getContent());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(entityResourceAssembler.toResource(savedTask));
    }

    @PatchMapping("/tasks/{id}")
    public ResponseEntity<Resource<Task>> updateTask(@PathVariable("id") Long taskIdToUpdate,
                                                     @RequestBody @ValidResource({Default.class, ChangeTaskGroup.class})
                                                     final Resource<Task> resourceTask,
                                                     PersistentEntityResourceAssembler entityResourceAssembler) {
        final Task updatedTask = taskService.updateTask(taskIdToUpdate, resourceTask.getContent());

        final Resource<Task> taskResource = new Resource<>(updatedTask);
        taskResource.add(entityResourceAssembler.toResource(updatedTask).getLinks());

        return ResponseEntity.ok(taskResource);
    }

    @PostMapping("/tasks/{id}/update/status")
    public ResponseEntity<Resource<?>> updateTaskStatus(@PathVariable("id") final Long taskIdToUpdate,
                                                        @RequestBody final TaskStatusUpdater statusUpdater,
                                                        final PersistentEntityResourceAssembler entityResourceAssembler) {
        taskService.updateTaskStatus(taskIdToUpdate, statusUpdater.getStatus());

        return ResponseEntity.ok().build();
    }

    static class TaskStatusUpdater {
        private TaskStatusEnum status;

        public TaskStatusUpdater() {
        }

        public TaskStatusEnum getStatus() {
            return status;
        }

        public void setStatus(TaskStatusEnum status) {
            this.status = status;
        }
    }

}
