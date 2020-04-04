package ru.lagoshny.task.manager.web.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import ru.lagoshny.task.manager.domain.entity.Task;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.entity.User;
import ru.lagoshny.task.manager.domain.entity.enums.TaskPriorityEnum;
import ru.lagoshny.task.manager.domain.entity.enums.TaskStatusEnum;
import ru.lagoshny.task.manager.utils.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static javax.servlet.http.HttpServletResponse.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

public class TaskControllerApiTest extends AbstractControllerApiTest<Task> {

    private static final String DS_TASK_PATH = "api/task";
    private static final String DS_TASK_EXP_PATH = "api/task/expected";

    @Test
    @DataSet(value = {DS_COMMON_PATH + "/user.yml", DS_TASK_PATH + "/task.yml"}, cleanAfter = true)
    @ExpectedDataSet(DS_TASK_EXP_PATH + "/task.yml")
    public void taskShouldBeUpdated() throws IOException {
        final Long existTaskId = 1000L;

        final Task taskToUpdate = createTask();
        taskToUpdate.setId(existTaskId);

        final TaskCategory taskCategory = TaskCategory.getDefault();
        taskCategory.setId(1000L);
        taskToUpdate.setCategory(taskCategory);

        given()
                .header(getAuthHeader())
                .contentType("application/hal+json")
                .body(converter.convertEntityToJsonResource(taskToUpdate))
        .when()
                .patch(getBasePath() + "/tasks/" + existTaskId)
        .then()
                .statusCode(SC_OK)
                .contentType("application/hal+json;charset=UTF-8")
                .body("id", is(taskToUpdate.getId().intValue()))
                .body("number", is(taskToUpdate.getNumber().intValue()))
                .body("name", is(taskToUpdate.getName()))
                .body("priority", is(taskToUpdate.getPriority().name()))
                .body("status", is(taskToUpdate.getStatus().name()))
                .body("needTimeManagement", is(taskToUpdate.isNeedTimeManagement()))
                .body("totalTime", is(taskToUpdate.getTotalTime()))
                .body("autoReduce", is(taskToUpdate.isAutoReduce()))
                .body("_links", not(StringUtils.Const.EMPTY))
                .body("_links.category", not(StringUtils.Const.EMPTY))
                .body("_links.author", not(StringUtils.Const.EMPTY));
    }

    @Test
    public void allServerErrorsAreReturnedWhenCreateEmptyTask() throws Exception {
        final Task badTask = new Task();
        badTask.setId(1L);
        badTask.setName(null);
        badTask.setCreationDate(null);
        badTask.setAuthor(null);
        badTask.setPriority(null);
        badTask.setStatus(null);

        given()
                .header(getAuthHeader())
                .contentType("application/hal+json")
                .body(converter.convertEntityToJsonResource(badTask))
        .when()
                .post(getBasePath() + "/tasks")
        .then()
                .statusCode(SC_BAD_REQUEST)
                .contentType("application/json;charset=UTF-8")
                .body("messages", not(StringUtils.Const.EMPTY))
                .and()
                .body("messages.size", is(5));
    }

    @Test
    @ExpectedDataSet({DS_TASK_EXP_PATH + "/task.yml", DS_COMMON_EXP_PATH + "/default-category.yml"})
    public void taskShouldBeCreatedWithUndefinedCategory() throws IOException {
        final Task task = createTask();

        given()
                .header(getAuthHeader())
                .contentType("application/hal+json")
                .body(converter.convertEntityToJsonResource(task))
        .when()
                .post(getBasePath() + "/tasks")
        .then()
                .statusCode(SC_CREATED)
                .contentType("application/hal+json;charset=UTF-8")
                .body("number", is(task.getNumber().intValue()))
                .body("name", is(task.getName()))
                .body("priority", is(task.getPriority().name()))
                .body("status", is(task.getStatus().name()))
                .body("needTimeManagement", is(task.isNeedTimeManagement()))
                .body("totalTime", is(task.getTotalTime()))
                .body("autoReduce", is(task.isAutoReduce()))
                .body("_links", not(StringUtils.Const.EMPTY))
                .body("_links.category", not(StringUtils.Const.EMPTY))
                .body("_links.author", not(StringUtils.Const.EMPTY));
    }

    @Test
    @DataSet(value = {DS_COMMON_PATH + "/user.yml", DS_TASK_PATH + "/task.yml"}, cleanAfter = true)
    @ExpectedDataSet({DS_TASK_EXP_PATH + "/task_in_progress_status.yml"})
    public void taskStatusShouldBeUpdatedToInProgress() {
        final long taskIdToUpdate = 1000L;
        final TaskController.TaskStatusUpdater newTaskStatus
                = new TaskController.TaskStatusUpdater(TaskStatusEnum.IN_PROGRESS);

        given()
                .header(getAuthHeader())
                .contentType("application/hal+json")
                .body(newTaskStatus)
        .when()
                .post(getBasePath() + "/tasks/" + taskIdToUpdate +"/update/status")
        .then()
                .statusCode(SC_OK)
                .contentType("application/hal+json;charset=UTF-8")
                .body("status", is(TaskStatusEnum.IN_PROGRESS.name()));
    }

    @NotNull
    private Task createTask() {
        final Task taskToUpdate = new Task();
        taskToUpdate.setNumber(1L);
        taskToUpdate.setName("Name");
        taskToUpdate.setCreationDate(LocalDateTime.now());
        taskToUpdate.setPriority(TaskPriorityEnum.LOW);
        taskToUpdate.setStatus(TaskStatusEnum.NEW);
        taskToUpdate.setNeedTimeManagement(true);
        taskToUpdate.setTotalTime(10);
        taskToUpdate.setAutoReduce(true);
        taskToUpdate.setAuthor(new User(1000L));

        return taskToUpdate;
    }

}