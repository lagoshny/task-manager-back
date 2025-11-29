package ru.lagoshny.task.manager.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lagoshny.task.manager.domain.entity.Task;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.repository.TaskCategoryRepository;
import ru.lagoshny.task.manager.domain.repository.TaskRepository;

import java.util.List;

@Service
public class TaskCategoryService {

    private final TaskCategoryRepository taskCategoryRepository;

    private final TaskRepository taskRepository;

    @Autowired
    public TaskCategoryService(final TaskCategoryRepository taskCategoryRepository,
                               final TaskRepository taskRepository) {
        this.taskCategoryRepository = taskCategoryRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public void deleteCategoryById(final Long taskCategoryId) {
        final TaskCategory taskCategory = taskCategoryRepository.findById(taskCategoryId)
                .orElseThrow(ResourceNotFoundException::new);

        final List<Task> allTasksByUserAndCategory =
                taskRepository.findAllByAuthorAndCategory(taskCategory.getUser(), taskCategory);
        taskRepository.deleteAll(allTasksByUserAndCategory);
        taskCategoryRepository.delete(taskCategory);
    }

}
