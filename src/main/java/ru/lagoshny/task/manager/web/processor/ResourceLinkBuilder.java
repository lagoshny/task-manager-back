package ru.lagoshny.task.manager.web.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.regex.Pattern;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Resource link builder that allows to add base path api to resource's link.
 * <p>
 * https://github.com/spring-projects/spring-hateoas/issues/434
 */
@Component
public class ResourceLinkBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLinkBuilder.class);

    private final RepositoryRestConfiguration config;

    private static final Pattern TEMPLATE_URI_PATTERN = Pattern.compile("\\{[^}]*}");

    public ResourceLinkBuilder(RepositoryRestConfiguration config) {
        this.config = config;
    }

    public Link fixLinkTo(Object invocationValue) {
        try {
            final String link = linkTo(invocationValue).toString();
            final URI uri = linkTo(invocationValue).toUri();

            return new Link(link.replace(uri.getPath(), config.getBasePath() + uri.getPath()), Link.REL_SELF);
        } catch (Exception e) {
            logger.error("An error occurred while creating the resource link", e);
        }

        return null;
    }

}
