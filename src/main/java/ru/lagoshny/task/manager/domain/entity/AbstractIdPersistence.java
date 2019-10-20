package ru.lagoshny.task.manager.domain.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.hateoas.Identifiable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * Adds common properties to inherited entities e.g. ID property.
 */
@MappedSuperclass
public class AbstractIdPersistence implements Identifiable<Long> {

    /**
     * Use AUTO generation type.
     * To doing this use native generator because need to use IDENTITY strategy for MySQL database
     * <a href="https://vladmihalcea.com/why-should-not-use-the-auto-jpa-generationtype-with-mysql-and-hibernate/"/>
     */
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
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
