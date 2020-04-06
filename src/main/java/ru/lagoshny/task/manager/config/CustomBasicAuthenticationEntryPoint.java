package ru.lagoshny.task.manager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.lagoshny.task.manager.web.exception.ServerError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

@Component
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    private final MessageSource messageSource;

    private final ObjectMapper objectMapper;

    @Autowired
    public CustomBasicAuthenticationEntryPoint(MessageSource messageSource,
                                               ObjectMapper objectMapper) {
        this.messageSource = messageSource;
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        final ServerError serverError = ServerError.builder()
                .path(request.getRequestURI())
                .messages(Collections.singletonList(
                        messageSource.getMessage("login.badCredentials", null, Locale.getDefault()))
                )
                .timestamp(new Date())
                .status(HttpStatus.UNAUTHORIZED)
                .build();

        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.getWriter().write(objectMapper.writeValueAsString(serverError));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("Task manager");
        super.afterPropertiesSet();
    }

}
