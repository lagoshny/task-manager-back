package ru.lagoshny.task.manager.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.data.rest.webmvc.alps.AlpsJacksonJsonHttpMessageConverter;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import ru.lagoshny.task.manager.config.app.ApplicationRestConfig;
import ru.lagoshny.task.manager.domain.entity.Task;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.entity.User;

import java.util.List;

/**
 * Additional configuration for Spring Data Rest module.
 */
@Configuration
public class SpringDataRestConfig implements RepositoryRestConfigurer {

    private final ApplicationRestConfig applicationRestConfig;

    private final Validator validator;

    @Autowired
    public SpringDataRestConfig(ApplicationRestConfig applicationRestConfig,
                                Validator validator) {
        this.applicationRestConfig = applicationRestConfig;
        this.validator = validator;
    }

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        // Bean validation always before save and create
        validatingListener.addValidator("beforeCreate", validator);
        validatingListener.addValidator("beforeSave", validator);
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        if (applicationRestConfig.isHideAllDefaultEndpoints()) {
            // Export as REST resource only annotated repository and annotated methods
            config.setRepositoryDetectionStrategy(
                    RepositoryDetectionStrategy.RepositoryDetectionStrategies.ANNOTATED);
            config.setExposeRepositoryMethodsByDefault(false);
        }
        config.exposeIdsFor(User.class, Task.class, TaskCategory.class);
    }

    /**
     * Spring Boot 4.0.0 uses Jackson 3 and there is a bug with {@link AlpsJacksonJsonHttpMessageConverter}.
     * This converter intercept hateoas requests and can't parse relation with entity urls.
     * This hack excludes converter for json requests.
     */
    @Bean
    public HttpMessageConverter<@NotNull Object> alpsPatch(AlpsJacksonJsonHttpMessageConverter converter) {
        List<MediaType> patched = converter.getSupportedMediaTypes().stream()
                .filter(mt -> !mt.includes(MediaType.APPLICATION_JSON))
                .toList();
        converter.setSupportedMediaTypes(patched);
        return converter;
    }

}
