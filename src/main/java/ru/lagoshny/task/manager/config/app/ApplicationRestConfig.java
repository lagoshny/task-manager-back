package ru.lagoshny.task.manager.config.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.annotation.RestResource;

@Configuration
@ConfigurationProperties(prefix = "task.manager.rest-api")
public class ApplicationRestConfig {

    /**
     * {@code true} then only explicitly annotated repository methods with {@link RestResource} will be exported as
     * rest endpoints, {@code false} then all repository methods will be exported as rest endpoints.
     */
    private boolean hideAllDefaultEndpoints;

    /**
     * {@code true} when need to enable cors configuration, {@code false} otherwise.
     */
    private boolean httpEnableCors;

    /**
     * Which hosts can contact server using HTTP origins.
     */
    private String httpAllowedOrigins;


    public boolean isHideAllDefaultEndpoints() {
        return hideAllDefaultEndpoints;
    }

    public void setHideAllDefaultEndpoints(boolean hideAllDefaultEndpoints) {
        this.hideAllDefaultEndpoints = hideAllDefaultEndpoints;
    }

    public boolean isHttpEnableCors() {
        return httpEnableCors;
    }

    public void setHttpEnableCors(boolean httpEnableCors) {
        this.httpEnableCors = httpEnableCors;
    }

    public String getHttpAllowedOrigins() {
        return httpAllowedOrigins;
    }

    public void setHttpAllowedOrigins(String httpAllowedOrigins) {
        this.httpAllowedOrigins = httpAllowedOrigins;
    }

}
