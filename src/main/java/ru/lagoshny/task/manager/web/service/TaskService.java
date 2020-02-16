package ru.lagoshny.task.manager.web.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import ru.lagoshny.task.manager.domain.entity.Task;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.entity.User;
import ru.lagoshny.task.manager.domain.entity.enums.TaskStatusEnum;
import ru.lagoshny.task.manager.domain.repository.TaskCategoryRepository;
import ru.lagoshny.task.manager.domain.repository.TaskRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskCategoryRepository taskCategoryRepository;

    @Autowired
    public TaskService(@NotNull TaskRepository taskRepository,
                       @NotNull TaskCategoryRepository taskCategoryRepository) {
        this.taskRepository = taskRepository;
        this.taskCategoryRepository = taskCategoryRepository;
    }

    @NotNull
    @Transactional
    public Task createTask(@NotNull Task task) {
        if (task.getCategory() == null) {
            task.setCategory(getDefaultTaskCategory(task.getAuthor()));
        }
        task.setNumber(getNextTaskNumber(task));
        task.setStatus(TaskStatusEnum.NEW);

        return taskRepository.save(task);
    }

    @NotNull
    @Transactional
    public Task updateTask(@NotNull Long taskIdToUpdate, @NotNull Task newTask) {
        final Task taskToUpdate =
                taskRepository.findById(taskIdToUpdate)
                        .orElseThrow(ResourceNotFoundException::new);
        if (!newTask.getCategory().equals(taskToUpdate.getCategory())) {
            newTask.setNumber(getNextTaskNumber(newTask));
        }
        newTask.setId(taskToUpdate.getId());

        return taskRepository.save(newTask);
    }

    public void updateTaskStatus(@NotNull final Long taskIdToUpdate,
                                 @NotNull TaskStatusEnum newTaskStatus) {
        final Task taskToUpdate =
                taskRepository.findById(taskIdToUpdate)
                        .orElseThrow(ResourceNotFoundException::new);

        if (TaskStatusEnum.NEW == taskToUpdate.getStatus()
                && TaskStatusEnum.IN_PROGRESS == newTaskStatus
                && taskToUpdate.isAutoReduce()) {
            taskToUpdate.setStartedDate(LocalDateTime.now());
            taskToUpdate.setStatus(newTaskStatus);
        } else if (TaskStatusEnum.IN_PROGRESS == taskToUpdate.getStatus()
                && TaskStatusEnum.PAUSE == newTaskStatus
                && taskToUpdate.isAutoReduce()) {
            final long leftTaskTime = ChronoUnit.MINUTES.between(taskToUpdate.getStartedDate(), LocalDateTime.now());
            if (taskToUpdate.getTotalTime() >= leftTaskTime) {
                taskToUpdate.setSpentTime((int) leftTaskTime);
                taskToUpdate.setStatus(newTaskStatus);
            } else {
                taskToUpdate.setSpentTime(taskToUpdate.getTotalTime());
                taskToUpdate.setStatus(TaskStatusEnum.NOT_COMPLETED);
            }
        } else if (TaskStatusEnum.PAUSE == taskToUpdate.getStatus()
                && TaskStatusEnum.IN_PROGRESS == newTaskStatus
                && taskToUpdate.isAutoReduce()) {
            taskToUpdate.setStartedDate(LocalDateTime.now().minus(taskToUpdate.getSpentTime(), ChronoUnit.MINUTES));
            taskToUpdate.setStatus(newTaskStatus);
        } else {
            taskToUpdate.setStatus(newTaskStatus);
        }


        taskRepository.save(taskToUpdate);
    }

    @NotNull
    private Long getNextTaskNumber(@NotNull Task task) {
        return taskRepository.countByAuthorAndCategory(task.getAuthor(), task.getCategory()) + 1;
    }

    @NotNull
    private TaskCategory getDefaultTaskCategory(@NotNull User author) {
        TaskCategory defaultCategory
                = taskCategoryRepository.findByPrefixIgnoreCaseAndUser(TaskCategory.getDefault().getPrefix(), author);
        if (defaultCategory != null) {
            return defaultCategory;
        }

        defaultCategory = TaskCategory.getDefault();
        defaultCategory.setUser(author);

        return taskCategoryRepository.save(defaultCategory);
    }

}
