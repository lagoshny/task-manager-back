package ru.lagoshny.task.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import ru.lagoshny.task.manager.config.app.ApplicationRestConfig;
import ru.lagoshny.task.manager.domain.entity.Task;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.entity.User;

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

}
