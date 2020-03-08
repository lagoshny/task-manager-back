package ru.lagoshny.task.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.lagoshny.task.manager.config.app.ApplicationRestConfig;
import ru.lagoshny.task.manager.utils.StringUtils;
import ru.lagoshny.task.manager.utils.StringUtils.Const;

import java.util.Arrays;
import java.util.List;

import static org.apache.logging.log4j.Level.TRACE;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SpringWebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Allowed HTTP methods to server.
     */
    private static final List<String> HTTP_ALLOWED_METHODS =
            Arrays.asList(
                    GET.name(), HEAD.name(), POST.name(),
                    PUT.name(), PATCH.name(), DELETE.name(),
                    OPTIONS.name(), TRACE.name());

    /**
     * Params which can be added to HTTP headers.
     */
    private static final List<String> HTTP_EXPOSED_HEADERS =
            Arrays.asList(
                    ACCESS_CONTROL_ALLOW_CREDENTIALS,
                    ACCESS_CONTROL_ALLOW_HEADERS,
                    ACCESS_CONTROL_ALLOW_METHODS,
                    ACCESS_CONTROL_ALLOW_ORIGIN);


    private ApplicationRestConfig applicationRestConfig;

    @Autowired
    public SpringWebSecurityConfig(ApplicationRestConfig applicationRestConfig) {
        this.applicationRestConfig = applicationRestConfig;
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        if (applicationRestConfig.isHttpEnableCors()) {
            httpSecurity.cors();
        }

        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/**").permitAll();
    }

    @Bean
    @ConditionalOnProperty(name = "task.manager.rest-api.httpEnableCors", havingValue = "true")
    public CorsConfigurationSource corsConfigurationSource() {
        final List<String> httpAllowedOriginsList =
                StringUtils.isNotBlank(applicationRestConfig.getHttpAllowedOrigins())
                        ? Arrays.asList(StringUtils.split(applicationRestConfig.getHttpAllowedOrigins(), Const.COMMA))
                        : null;
        final CorsConfiguration corsConfiguration =
                new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.setAllowedMethods(HTTP_ALLOWED_METHODS);
        corsConfiguration.setAllowedOrigins(httpAllowedOriginsList);
        corsConfiguration.setExposedHeaders(HTTP_EXPOSED_HEADERS);
        corsConfiguration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
