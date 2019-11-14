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
import ru.lagoshny.task.manager.domain.repository.UserRepository;

import javax.transaction.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskCategoryRepository taskCategoryRepository;

    private final UserRepository userRepository;

    @Autowired
    public TaskService(@NotNull TaskRepository taskRepository,
                       @NotNull TaskCategoryRepository taskCategoryRepository,
                       @NotNull UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskCategoryRepository = taskCategoryRepository;
        this.userRepository = userRepository;
    }

    @NotNull
    @Transactional
    public Task createTask(@NotNull Task task) {
        if (task.getCategory() == null) {
            task.setCategory(getDefaultTaskCategory(task.getAuthor()));
        }
        task.setNumber(getNextTaskNumber(task));
        task.setStatus(TaskStatusEnum.NEW);
        final Task savedTask = taskRepository.save(task);
        final User user = userRepository.findByUsername(task.getAuthor().getUsername());
        user.addTask(savedTask);
        userRepository.save(user);

        return savedTask;
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
