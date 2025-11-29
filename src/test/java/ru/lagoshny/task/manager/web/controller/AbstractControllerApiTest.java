package ru.lagoshny.task.manager.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.lagoshny.task.manager.domain.entity.Identifiable;
import ru.lagoshny.task.manager.helper.EntityToJsonResourceConverter;
import ru.lagoshny.task.manager.helper.category.ApiTest;
import ru.lagoshny.task.manager.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static ru.lagoshny.task.manager.web.controller.AbstractControllerApiTest.DS_COMMON_PATH;

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
@Tag(ApiTest.NAME)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DBRider
@DataSet(value = DS_COMMON_PATH + "/user.yml", cleanAfter = true, disableConstraints = true)
@ActiveProfiles({"api-test", "dev-local-h2"})
public abstract class AbstractControllerApiTest<T extends Identifiable<Long>> {

    static final String RESOURCE_DS_PATH = "src/test/resources/datasets";
    static final String DS_COMMON_PATH = "api/common";
    static final String DS_COMMON_EXP_PATH = "api/common/expected";

    @Autowired
    private Environment environment;

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    private String authCredentials;

    @Autowired
    protected EntityToJsonResourceConverter<T> converter;

    @BeforeEach
    public void init() throws IOException {
        RestAssured.port = environment.getProperty("local.server.port", Integer.class);

        final ImmutablePair<String, String> userCredentials = getUserCredentials();
        authCredentials = "Basic " + Base64.getEncoder()
                .encodeToString((userCredentials.getLeft() + ":" + userCredentials.getRight())
                        .getBytes());
    }

    public String getBasePath() {
        return basePath;
    }

    public Header getAuthHeader() {
        return new Header("Authorization", authCredentials);
    }

    /**
     * Load user configuration file and extract username with password.
     *
     * @return {@link Pair} left part is username, right is password
     */
    private ImmutablePair<String, String> getUserCredentials() throws IOException {
        final ObjectMapper ymlReader = new ObjectMapper(new YAMLFactory());
        final File userConfigFile = new File(RESOURCE_DS_PATH + "/" + DS_COMMON_PATH + "/user.yml");
        final Map<String, ArrayList<Map<String, String>>> ymlData = ymlReader.readValue(userConfigFile, Map.class);

        return ymlData.get("USER").stream()
                .filter(stringStringMap -> StringUtils.isNotEmpty(stringStringMap.get("USERNAME"))
                        && StringUtils.isNotEmpty(stringStringMap.get("PASSWORD")))
                .map(stringStringMap ->
                        new ImmutablePair<>(stringStringMap.get("USERNAME"), stringStringMap.get("PASSWORD")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Auth user should has username and password"));
    }

}
