package ru.lagoshny.task.manager.config.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "task.manager.rest-api")
public class RestConfig {

    private boolean hideAllDefaultEndpoints;

    public boolean isHideAllDefaultEndpoints() {
        return hideAllDefaultEndpoints;
    }

    public void setHideAllDefaultEndpoints(boolean hideAllDefaultEndpoints) {
        this.hideAllDefaultEndpoints = hideAllDefaultEndpoints;
    }

}
