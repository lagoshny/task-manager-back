package ru.lagoshny.task.manager.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.atteo.evo.inflector.English;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.hateoas.Identifiable;
import org.springframework.stereotype.Component;
import ru.lagoshny.task.manager.domain.entity.AbstractIdPersistence;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.IOException;
import java.util.Arrays;

import static org.springframework.mock.web.MockHttpServletRequest.DEFAULT_SCHEME;
import static org.springframework.mock.web.MockHttpServletRequest.DEFAULT_SERVER_NAME;

/**
 * Converter to convert JPA entity to HATEOAS json resource.
 * <p>
 * For example:
 * Suppose that we started application on localhost with 8080 port.
 * If you have <b>user</b> entity with id = 1 then after converter processed user entity will be result json as next:
 * {
 *   "id": 1,
 *   ....
 *   "_links": {
 *     "self": {
 *       "href": "http://localhost:8080/api/v1/users/1"
 *     },
 *     "user": {
 *       "href": "http://localhost:8080/api/v1/users/1"
 *     }
 *   }
 * }
 * <p>
 * For more information about HATEOAS see <a href="http://spring-projects.ru/understanding/hateoas"/>
 *
 * @param <T> to specify that entity need to have id property
 */
@Component
public class EntityToJsonResourceConverter<T extends Identifiable> {

    @Value("${server.port}")
    private int port;

    @Value("${spring.data.rest.base-path}")
    private String baseApi;

    @Autowired
    private Repositories repositoriesHolder;

    /**
     * Convert entity to HATEOAS json resource.
     *
     * @param entity that need to convert
     * @return {@link String} as HATEOAS json or empty string if error occurs
     */
    public String convertEntityToJsonResource(final T entity) throws IOException {
        final SimpleModule simpleModule = new SimpleModule();
        //noinspection unchecked
        simpleModule.addSerializer(new EntitySerializer((Class<T>) Identifiable.class));

        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(simpleModule);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsString(entity);
    }


    /**
     * Custom Jackson serializer that doing converting from entity to HATEOAS json.
     */
    private class EntitySerializer extends StdSerializer<T> {
        EntitySerializer(Class<T> clazz) {
            super(clazz);
        }

        @Override
        public void serialize(T entity, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            Arrays.stream(entity.getClass().getDeclaredFields()).forEach(field -> {
                try {
                    field.setAccessible(true);
                    if (field.get(entity) == null) {
                        return;
                    }
                    if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
                        jsonGenerator.writeStringField(field.getName(),
                                getResourceUrlByEntity((AbstractIdPersistence) field.get(entity)));
                    } else if (field.isAnnotationPresent(OneToMany.class)
                            || field.isAnnotationPresent(ManyToMany.class)) {
                        jsonGenerator.writeStringField(field.getName(),
                                getResourceUrlByEntity(entity) + "/" + field.getName());
                    } else {
                        jsonGenerator.writeObjectField(field.getName(), field.get(entity));
                    }
                } catch (IOException | IllegalAccessException ignored) {
                }
            });
            jsonGenerator.writeEndObject();
        }
    }

    private String getResourceUrlByEntity(Identifiable entity) {
        final String resourceName;
        if (repositoriesHolder.getRepositoryInformationFor(entity.getClass()).isPresent()) {
            RepositoryInformation repositoryInformation =
                    repositoriesHolder.getRepositoryInformationFor(entity.getClass()).get();
            RepositoryRestResource annotation =
                    AnnotationUtils.findAnnotation(repositoryInformation.getRepositoryInterface(),
                            RepositoryRestResource.class);
            if (annotation != null) {
                resourceName = annotation.path();
            } else {
                resourceName = English.plural(StringUtils.uncapitalize(entity.getClass().getSimpleName()));
            }
        } else {
            resourceName = English.plural(StringUtils.uncapitalize(entity.getClass().getSimpleName()));
        }

        return DEFAULT_SCHEME + "://" + DEFAULT_SERVER_NAME + baseApi + "/" + resourceName
                + "/" + entity.getId();
    }

}
