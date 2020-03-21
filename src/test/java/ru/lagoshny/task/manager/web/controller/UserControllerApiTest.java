package ru.lagoshny.task.manager.web.controller;

import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.Test;
import ru.lagoshny.task.manager.domain.entity.User;
import ru.lagoshny.task.manager.utils.StringUtils;

import static io.restassured.RestAssured.given;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;

public class UserControllerApiTest extends AbstractControllerApiTest<User> {

    private static final String DS_USER_EXP_PATH = "api/user/expected";

    @Test
    @ExpectedDataSet(DS_USER_EXP_PATH + "/user.yml")
    public void userShouldBeCreatedWithoutAuthorization() throws Exception {
        final User user = new User();
        user.setUsername("ivan777");
        user.setPassword("123456");
        user.setEmail("ivan@mail.ru");
        user.setEnabled(true);

        given()
                .contentType("application/hal+json")
                .body(converter.convertEntityToJsonResource(user))
        .when()
                .post(getBasePath() + "/users")
        .then()
                .statusCode(SC_CREATED)
                .contentType("application/hal+json")
                .body("username", is(user.getUsername()))
                .body("password", is(nullValue()))
                .body("email", is(user.getEmail()))
                .body("enabled", is(user.isEnabled()))
                .body("_links", not(StringUtils.Const.EMPTY));
    }

    @Test
    public void passwordShouldBeNullWhenGetUser() {
        final long userId = 1000;

        given()
                .header(getAuthHeader())
        .when()
                .get(getBasePath() + "/users/" + userId)
        .then()
                .statusCode(SC_OK)
                .contentType("application/hal+json")
                .body("password", is(nullValue()));
    }

}