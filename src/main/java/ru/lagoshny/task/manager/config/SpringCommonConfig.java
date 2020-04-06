package ru.lagoshny.task.manager.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class SpringCommonConfig {

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages");
        return messageSource;
    }

    /**
     * Define {@link LocalValidatorFactoryBean} with custom ValidationMessageSource.
     * This makes it possible to use custom property file instead standard ValidationMessages.properties
     * as source to JSR-303 validation messages.
     * <p>
     * Using {@link Primary} to override standard JSR-303 LocalValidatorFactoryBean
     * with default ValidationMessages.properties to custom messageSource otherwise, it will not work.
     *
     * @param messageSource where validation messages will holds
     */
    @Bean
    @Primary
    public LocalValidatorFactoryBean validatorBean(MessageSource messageSource) {
        final LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

}
