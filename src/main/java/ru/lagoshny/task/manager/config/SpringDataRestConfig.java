package ru.lagoshny.task.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ru.lagoshny.task.manager.config.app.ApplicationRestConfig;

/**
 * Additional configuration for Spring Data Rest module.
 */
@Configuration
public class SpringDataRestConfig implements RepositoryRestConfigurer {

    private final ApplicationRestConfig applicationRestConfig;

    @Autowired
    public SpringDataRestConfig(ApplicationRestConfig applicationRestConfig) {
        this.applicationRestConfig = applicationRestConfig;
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        Validator validator = validator();
        // Bean validation always before save and create
        validatingListener.addValidator("beforeCreate", validator);
        validatingListener.addValidator("beforeSave", validator);
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        if (applicationRestConfig.isHideAllDefaultEndpoints()) {
            // Export as REST resource only annotated repository and annotated methods
            config.setRepositoryDetectionStrategy(
                    RepositoryDetectionStrategy.RepositoryDetectionStrategies.ANNOTATED);
            config.setExposeRepositoryMethodsByDefault(false);
        }
    }

}
