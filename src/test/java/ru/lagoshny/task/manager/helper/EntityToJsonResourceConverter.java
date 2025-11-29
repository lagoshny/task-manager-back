package ru.lagoshny.task.manager.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import org.atteo.evo.inflector.English;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import ru.lagoshny.task.manager.domain.entity.AbstractIdPersistence;
import ru.lagoshny.task.manager.domain.entity.Identifiable;
import ru.lagoshny.task.manager.utils.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

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
public class EntityToJsonResourceConverter<T extends Identifiable<Long>> {

    @Value("${spring.data.rest.base-path}")
    private String baseApi;

    private final Repositories repositoriesHolder;

    private final ObjectMapper mapper;

    @Autowired
    public EntityToJsonResourceConverter(Repositories repositoriesHolder,
                                         ObjectMapper mapper) {
        this.repositoriesHolder = repositoriesHolder;
        this.mapper = mapper;
    }

    /**
     * Convert entity to HATEOAS json resource.
     *
     * @param entity that need to convert
     * @return {@link String} as HATEOAS json or empty string if error occurs
     */
    public String convertEntityToJsonResource(final T entity) throws IOException {
        final SimpleModule simpleModule = new SimpleModule();
        //noinspection unchecked
        simpleModule.addSerializer(new EntitySerializer((Class) Identifiable.class));
        mapper.registerModule(simpleModule);

        return mapper.writeValueAsString(entity);
    }


    /**
     * Custom Jackson serializer that doing converting from entity to HATEOAS json.
     */
    public class EntitySerializer extends StdSerializer<T> {
        public EntitySerializer(Class<T> clazz) {
            super(clazz);
        }

        @Override
        public void serialize(T entity, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            Arrays.stream(entity.getClass().getDeclaredFields()).forEach(field -> {
                try {
                    field.setAccessible(true);
                    if (field.get(entity) == null
                            || (field.get(entity) instanceof Collection && ((Collection) field.get(entity)).isEmpty())) {
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

    private String getResourceUrlByEntity(Identifiable<Long> entity) {
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
