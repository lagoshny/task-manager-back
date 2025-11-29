package ru.lagoshny.task.manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.lagoshny.task.manager.config.app.ApplicationRestConfig;
import ru.lagoshny.task.manager.security.XUserDetailsService;
import ru.lagoshny.task.manager.utils.StringUtils;
import ru.lagoshny.task.manager.utils.StringUtils.Const;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringWebSecurityConfig {

    private static final List<String> HTTP_ALLOWED_METHODS =
            Arrays.asList(
                    HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.POST.name(),
                    HttpMethod.PUT.name(), HttpMethod.PATCH.name(), HttpMethod.DELETE.name(),
                    HttpMethod.OPTIONS.name(), HttpMethod.TRACE.name()
            );

    private static final List<String> HTTP_EXPOSED_HEADERS =
            Arrays.asList(
                    HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
                    HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                    HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                    HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN
            );

    private final ApplicationRestConfig applicationRestConfig;
    private final XUserDetailsService xUserDetailsService;
    private final CustomBasicAuthenticationEntryPoint authenticationEntryPoint;

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    public SpringWebSecurityConfig(ApplicationRestConfig applicationRestConfig,
                                   XUserDetailsService xUserDetailsService,
                                   CustomBasicAuthenticationEntryPoint authenticationEntryPoint) {
        this.applicationRestConfig = applicationRestConfig;
        this.xUserDetailsService = xUserDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        if (applicationRestConfig.isHttpEnableCors()) {
            http.cors(Customizer.withDefaults());
        }

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, basePath + "/users").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .userDetailsService(xUserDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(name = "task.manager.rest-api.httpEnableCors", havingValue = "true")
    public CorsConfigurationSource corsConfigurationSource() {

        List<String> httpAllowedOriginsList =
                StringUtils.isNotBlank(applicationRestConfig.getHttpAllowedOrigins())
                        ? Arrays.asList(StringUtils.split(applicationRestConfig.getHttpAllowedOrigins(), Const.COMMA))
                        : null;

        CorsConfiguration cors = new CorsConfiguration().applyPermitDefaultValues();
        cors.setAllowedMethods(HTTP_ALLOWED_METHODS);
        cors.setAllowedOrigins(httpAllowedOriginsList);
        cors.setExposedHeaders(HTTP_EXPOSED_HEADERS);
        cors.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
