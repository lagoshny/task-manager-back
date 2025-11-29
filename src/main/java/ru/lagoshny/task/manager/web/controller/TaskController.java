package ru.lagoshny.task.manager.web.controller;

import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
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
    public ResponseEntity<PersistentEntityResource> createTask(@RequestBody
                                                               @ValidResource
                                                               Task resourceTask,
                                                               PersistentEntityResourceAssembler
                                                                       entityResourceAssembler) {
        final Task savedTask = taskService.createTask(resourceTask);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(entityResourceAssembler.toModel(savedTask));
    }

    @PatchMapping("/tasks/{id}")
    public ResponseEntity<PersistentEntityResource> updateTask(@PathVariable("id") Long taskIdToUpdate,
                                                               @RequestBody
                                                               @ValidResource({Default.class, ChangeTaskGroup.class})
                                                               EntityModel<Task> resourceTask,
                                                               PersistentEntityResourceAssembler
                                                                       entityResourceAssembler) {
        final Task updatedTask = taskService.updateTask(taskIdToUpdate, resourceTask.getContent());

        return ResponseEntity.ok(entityResourceAssembler.toModel(updatedTask));
    }

    @PostMapping("/tasks/{id}/update/status")
    public ResponseEntity<PersistentEntityResource> updateTaskStatus(@PathVariable("id") Long taskIdToUpdate,
                                                                     @RequestBody TaskStatusUpdater statusUpdater,
                                                                     PersistentEntityResourceAssembler
                                                                             entityResourceAssembler) {
        Task updatedTask = taskService.updateTaskStatus(taskIdToUpdate, statusUpdater.getStatus());

        return ResponseEntity.ok(entityResourceAssembler.toModel(updatedTask));
    }

    static class TaskStatusUpdater {
        private TaskStatusEnum status;

        public TaskStatusUpdater() {
        }

        public TaskStatusUpdater(TaskStatusEnum status) {
            this.status = status;
        }

        public TaskStatusEnum getStatus() {
            return status;
        }

        public void setStatus(TaskStatusEnum status) {
            this.status = status;
        }
    }

}
