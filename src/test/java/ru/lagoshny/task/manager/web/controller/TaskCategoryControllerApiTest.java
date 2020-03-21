package ru.lagoshny.task.manager.web.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.Test;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;

import static io.restassured.RestAssured.given;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class TaskCategoryControllerApiTest extends AbstractControllerApiTest<TaskCategory> {

    private static final String DS_CATEGORY_PATH = "api/task-category";

    @Test
    @DataSet(value = {DS_COMMON_PATH + "/user.yml", DS_CATEGORY_PATH + "/tasks.yml"}, cleanAfter = true)
    @ExpectedDataSet(value = {DS_COMMON_EXP_PATH + "/empty-task.yml", DS_COMMON_EXP_PATH + "/empty-category.yml"})
    public void beforeDeleteCategoryAllCategoryTasksShouldBeDeleted() {
        final long categoryId = 1000;

        given()
                .header(getAuthHeader())
        .when()
                .delete(getBasePath() + "/task-categories/" + categoryId)
        .then().
                statusCode(SC_OK);
    }

}
