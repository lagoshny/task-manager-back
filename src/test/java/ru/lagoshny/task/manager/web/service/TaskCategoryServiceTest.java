package ru.lagoshny.task.manager.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import ru.lagoshny.task.manager.domain.entity.Task;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.repository.TaskCategoryRepository;
import ru.lagoshny.task.manager.domain.repository.TaskRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskCategoryServiceTest {

    @InjectMocks
    private TaskCategoryService taskCategoryService;

    @Mock
    private TaskCategoryRepository taskCategoryRepository;

    @Mock
    private TaskRepository taskRepository;

    @Test
    public void shouldThrowExceptionWhenTaskCategoryToDeleteNotFound() {
        // GIVEN
        final Long taskCategoryIdToDelete = 0L;
        when(taskCategoryRepository.findById(taskCategoryIdToDelete)).thenReturn(Optional.empty());

        // WHEN
        assertThrows(ResourceNotFoundException.class, () -> {
            taskCategoryService.deleteCategoryById(taskCategoryIdToDelete);
        });
    }

    @Test
    public void shouldDeleteCategoryById() {
        // GIVEN
        final TaskCategory taskCategory = new TaskCategory();
        taskCategory.setId(1L);
        final Task task = new Task();
        task.setCategory(taskCategory);
        when(taskCategoryRepository.findById(taskCategory.getId())).thenReturn(Optional.of(taskCategory));

        // WHEN
        taskCategoryService.deleteCategoryById(taskCategory.getId());

        // THEN
        verify(taskCategoryRepository, times(1)).delete(taskCategory);
    }

    @Test
    public void shouldDeleteCategoryTasksBeforeDeleteCategory() {
        // GIVEN
        final TaskCategory taskCategory = new TaskCategory();
        taskCategory.setId(1L);
        final Task task = new Task();
        task.setCategory(taskCategory);
        final List<Task> taskList = Collections.singletonList(task);
        when(taskCategoryRepository.findById(taskCategory.getId())).thenReturn(Optional.of(taskCategory));
        when(taskRepository.findAllByAuthorAndCategory(any(), any())).thenReturn(taskList);

        // WHEN
        taskCategoryService.deleteCategoryById(taskCategory.getId());

        // THEN
        verify(taskRepository, times(1)).deleteAll(taskList);
        verify(taskCategoryRepository, times(1)).delete(taskCategory);
    }

}