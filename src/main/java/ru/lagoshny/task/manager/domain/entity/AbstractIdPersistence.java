package ru.lagoshny.task.manager.domain.entity;

import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.util.Objects;

/**
 * Adds common properties to inherited entities e.g. ID property.
 */
@MappedSuperclass
public class AbstractIdPersistence implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "common_id_generator")
    @SequenceGenerator(name = "common_id_generator", sequenceName = "common_id_seq")
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object rafThat) {
        if (this == rafThat) {
            return true;
        }
        if (!(rafThat instanceof AbstractIdPersistence)) {
            return false;
        }
        AbstractIdPersistence that = (AbstractIdPersistence) rafThat;

        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
