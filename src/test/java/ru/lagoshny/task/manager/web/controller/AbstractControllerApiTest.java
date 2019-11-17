package ru.lagoshny.task.manager.web.controller;

import com.github.database.rider.spring.api.DBRider;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.Identifiable;
import org.springframework.test.context.junit4.SpringRunner;
import ru.lagoshny.task.manager.helper.EntityToJsonResourceConverter;
import ru.lagoshny.task.manager.helper.category.ApiTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Common test class for testing controller's api.
 * Extend this class if need to write controller api test.
 * <p>
 * Date: 2/9/19
 * Time: 1:14 PM
 * Project: task-manager
 *
 * @author ilya@lagoshny.ru
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DBRider
@Category(ApiTest.class)
public abstract class AbstractControllerApiTest<T extends Identifiable> {

    static final String DS_COMMON_PATH = "api/common";
    static final String DS_COMMON_EXP_PATH = "api/common/expected";

    @LocalServerPort
    private int port;

    @Value("${spring.data.rest.base-path}")
    protected String basePath;

    @Autowired
    protected EntityToJsonResourceConverter<T> converter;

    @Before
    public void init() {
        RestAssured.port = port;
    }

}
