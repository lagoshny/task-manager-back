package ru.lagoshny.task.manager.web.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.UriTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ru.lagoshny.task.manager.utils.StringUtils;

import java.net.URL;
import java.util.regex.Matcher;
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
            String templateUriVars = StringUtils.EMPTY;
            final Matcher templateUriMatcher = TEMPLATE_URI_PATTERN.matcher(linkTo(invocationValue).toString());
            if (templateUriMatcher.find()) {
                templateUriVars = templateUriMatcher.group();
            }

            final UriComponentsBuilder uriComponentsBuilder = linkTo(invocationValue).toUriComponentsBuilder();
            final URL url = new URL(uriComponentsBuilder.toUriString());
            uriComponentsBuilder.replacePath(config.getBasePath() + url.getPath());
            final UriTemplate uriTemplate = new UriTemplate(uriComponentsBuilder.toUriString() + templateUriVars);

            return new Link(uriTemplate, Link.REL_SELF);
        } catch (Exception e) {
            logger.error("An error occurred while creating the resource link" ,e);
        }

        return null;
    }

}
