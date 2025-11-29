package ru.lagoshny.task.manager.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import ru.lagoshny.task.manager.domain.entity.Task;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.entity.User;
import ru.lagoshny.task.manager.domain.entity.enums.TaskStatusEnum;
import ru.lagoshny.task.manager.domain.repository.TaskCategoryRepository;
import ru.lagoshny.task.manager.domain.repository.TaskRepository;
import ru.lagoshny.task.manager.helper.MockitoUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskCategoryRepository taskCategoryRepository;

    @Test
    public void shouldSetDefaultCategoryWhenCategoryIsNull() {
        // GIVEN
        final User author = new User();
        final Task task = new Task();
        task.setAuthor(author);
        task.setCategory(null);
        final TaskCategory defaultTaskCategory = TaskCategory.getDefault();

        when(taskCategoryRepository.findByPrefixIgnoreCaseAndUser(defaultTaskCategory.getPrefix(), author))
                .thenReturn(defaultTaskCategory);
        when(taskRepository.save(task)).thenAnswer(MockitoUtils::returnFirstParam);

        // WHEN
        final TaskCategory actualTaskCategory = taskService.createTask(task).getCategory();

        // THEN
        assertThat(actualTaskCategory, equalTo(defaultTaskCategory));
    }

    @Test
    public void shouldCreateDefaultCategoryForUserWhenHeHasNotOne() {
        // GIVEN
        final User author = new User();
        final Task task = new Task();
        task.setAuthor(author);
        task.setCategory(null);

        final TaskCategory defaultTaskCategory = TaskCategory.getDefault();
        defaultTaskCategory.setUser(author);

        when(taskCategoryRepository.findByPrefixIgnoreCaseAndUser(anyString(), eq(author)))
                .thenReturn(null);
        when(taskCategoryRepository.save(defaultTaskCategory)).thenAnswer(MockitoUtils::returnFirstParam);
        when(taskRepository.save(task)).thenAnswer(MockitoUtils::returnFirstParam);

        // WHEN
        taskService.createTask(task);

        // THEN
        verify(taskCategoryRepository, times(1)).save(defaultTaskCategory);
    }

    @Test
    public void taskNumberShouldBeIncreasedWhenCreateNewOneInSameCategory() {
        // GIVEN
        final TaskCategory taskCategory = TaskCategory.getDefault();
        final Task task = new Task();
        task.setCategory(taskCategory);

        when(taskRepository.maxTaskNumberByCategoryAndAuthor(any(), eq(taskCategory))).thenReturn(0L);
        when(taskRepository.save(task)).thenAnswer(MockitoUtils::returnFirstParam);

        // WHEN
        final Task createdTask = taskService.createTask(task);

        // THEN
        assertThat(createdTask.getNumber(), is(notNullValue()));
        assertThat(createdTask.getNumber(), is(1L));
    }

    @Test
    public void taskStatusShouldBeNewWhenCreateNewOne() {
        // GIVEN
        final TaskCategory taskCategory = TaskCategory.getDefault();
        final Task task = new Task();
        task.setCategory(taskCategory);

        when(taskRepository.maxTaskNumberByCategoryAndAuthor(any(), eq(taskCategory))).thenReturn(0L);
        when(taskRepository.save(task)).thenAnswer(MockitoUtils::returnFirstParam);

        // WHEN
        final Task createdTask = taskService.createTask(task);

        // THEN
        assertThat(createdTask.getStatus(), is(notNullValue()));
        assertThat(createdTask.getStatus(), is(TaskStatusEnum.NEW));
    }

    @Test
    public void shouldThrowExceptionWhenTaskToUpdateNotFound() {
        // GIVEN
        final Long nonExistTaskId = 0L;
        final Task newTask = new Task();
        when(taskRepository.findById(nonExistTaskId)).thenReturn(Optional.empty());

        // WHEN
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.updateTask(nonExistTaskId, newTask);
        });
    }

    @Test
    public void taskNumberShouldBeUpdatedWhenNewTaskHasAnotherCategory() {
        // GIVEN
        final Task newTask = new Task();
        final long newCategoryCountTasks = 1L;
        final TaskCategory newTaskCategory = new TaskCategory();
        newTaskCategory.setName("New Category");
        newTask.setCategory(newTaskCategory);
        newTask.setNumber(null);

        final Task existTask = new Task();
        final TaskCategory existTaskCategory = new TaskCategory();
        existTaskCategory.setName("Current Category");
        existTask.setId(1L);
        existTask.setCategory(existTaskCategory);
        existTask.setNumber(1L);

        when(taskRepository.findById(any())).thenReturn(Optional.of(existTask));
        when(taskRepository.maxTaskNumberByCategoryAndAuthor(any(), eq(newTaskCategory)))
                .thenReturn(newCategoryCountTasks);
        when(taskRepository.save(newTask)).thenAnswer(MockitoUtils::returnFirstParam);

        // WHEN
        final Task updatedTask = taskService.updateTask(existTask.getId(), newTask);

        // THEN
        assertThat(updatedTask.getNumber(), is(newCategoryCountTasks + 1));
    }

    @Test
    public void taskNumberShouldNotUpdateWhenUpdateTaskWithinSameCategory() {
        // GIVEN
        final TaskCategory taskCategory = new TaskCategory();
        taskCategory.setName("Category");

        final Task newTask = new Task();
        newTask.setCategory(taskCategory);
        newTask.setNumber(1L);

        final Task existTask = new Task();
        existTask.setCategory(taskCategory);
        existTask.setNumber(1L);

        when(taskRepository.findById(any())).thenReturn(Optional.of(existTask));
        when(taskRepository.save(newTask)).thenAnswer(MockitoUtils::returnFirstParam);

        // WHEN
        final Task updatedTask = taskService.updateTask(1L, newTask);

        // THEN
        assertThat(updatedTask.getNumber(), is(newTask.getNumber()));
    }

    @Test
    public void taskShouldUpdateStatus() {
        // GIVEN
        final Task existTask = new Task();
        existTask.setId(1L);
        existTask.setStatus(TaskStatusEnum.NEW);

        when(taskRepository.findById(any())).thenReturn(Optional.of(existTask));
        when(taskRepository.save(any())).thenAnswer(MockitoUtils::returnFirstParam);

        // WHEN
        final Task updatedTask = taskService.updateTaskStatus(existTask.getId(), TaskStatusEnum.COMPLETED);

        // THEN
        assertThat(updatedTask, notNullValue());
        assertThat(updatedTask.getStatus(), is(TaskStatusEnum.COMPLETED));
    }

    @Test
    public void shouldSetStartedDateToTaskWhenNewStatusIsInProgressWithAutoReduceTime() {
        // GIVEN
        final Task existTask = new Task();
        existTask.setId(1L);
        existTask.setStatus(TaskStatusEnum.NEW);
        existTask.setAutoReduce(true);

        when(taskRepository.findById(any())).thenReturn(Optional.of(existTask));
        when(taskRepository.save(any())).thenAnswer(MockitoUtils::returnFirstParam);

        // WHEN
        final Task updatedTask = taskService.updateTaskStatus(existTask.getId(), TaskStatusEnum.IN_PROGRESS);

        // THEN
        assertThat(updatedTask, notNullValue());
        assertThat(updatedTask.getStatus(), is(TaskStatusEnum.IN_PROGRESS));
        assertThat(updatedTask.getStartedDate(), notNullValue());
    }

    @Test
    public void shouldSetSpentTimeToTaskWhenNewStatusIsPauseWithAutoReduceTime() {
        // GIVEN
        final Task existTask = new Task();
        existTask.setId(1L);
        existTask.setStatus(TaskStatusEnum.IN_PROGRESS);
        existTask.setTotalTime(10);
        existTask.setAutoReduce(true);

        when(taskRepository.findById(any())).thenReturn(Optional.of(existTask));
        when(taskRepository.save(any())).thenAnswer(MockitoUtils::returnFirstParam);

        // WHEN
        final Task updatedTask = taskService.updateTaskStatus(existTask.getId(), TaskStatusEnum.PAUSE);

        // THEN
        assertThat(updatedTask, notNullValue());
        assertThat(updatedTask.getStatus(), is(TaskStatusEnum.PAUSE));
        assertThat(updatedTask.getSpentTime(), notNullValue());
    }

    @Test
    public void shouldSetNotCompletedTaskStatusWhenTaskTimeIsExpired() {
        // GIVEN
        final Task existTask = new Task();
        existTask.setId(1L);
        existTask.setStatus(TaskStatusEnum.IN_PROGRESS);
        existTask.setTotalTime(10);
        existTask.setStartedDate(LocalDateTime.now().minusMinutes(existTask.getTotalTime() + 1));
        existTask.setAutoReduce(true);

        when(taskRepository.findById(any())).thenReturn(Optional.of(existTask));
        when(taskRepository.save(any())).thenAnswer(MockitoUtils::returnFirstParam);

        // WHEN
        final Task updatedTask = taskService.updateTaskStatus(existTask.getId(), TaskStatusEnum.PAUSE);

        // THEN
        assertThat(updatedTask, notNullValue());
        assertThat(updatedTask.getStatus(), is(TaskStatusEnum.NOT_COMPLETED));
        assertThat(updatedTask.getSpentTime(), is(updatedTask.getTotalTime()));
    }

}