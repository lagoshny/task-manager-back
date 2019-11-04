package ru.lagoshny.task.manager;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.ChainEventHandler;
import io.hypersistence.optimizer.core.event.Event;
import io.hypersistence.optimizer.core.event.ListEventHandler;
import io.hypersistence.optimizer.core.event.LogEventHandler;
import io.hypersistence.optimizer.hibernate.event.configuration.connection.SkipAutoCommitCheckEvent;
import io.hypersistence.optimizer.hibernate.event.configuration.query.QueryInClauseParameterPaddingEvent;
import io.hypersistence.optimizer.hibernate.event.configuration.query.QueryPaginationCollectionFetchingEvent;
import io.hypersistence.optimizer.hibernate.event.configuration.schema.SchemaGenerationEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.ManyToManyListEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.OneToOneParentSideEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.OneToOneWithoutMapsIdEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.association.fetching.EagerFetchingEvent;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.Arrays;

import static org.junit.Assert.assertSame;

@Ignore
@RunWith(SpringRunner.class)
@DataJpaTest
public class TaskManagerApplicationTest {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;


    private final ListEventHandler listEventHandler = new ListEventHandler();

    @Before
    public void init() {
        new HypersistenceOptimizer(
                new JpaConfig(entityManagerFactory)
                        .setEventHandler(new ChainEventHandler(
                                Arrays.asList(
                                        LogEventHandler.INSTANCE,
                                        listEventHandler
                                )
                        ))
        ).init();
    }


    @Test
    public void testOptimizer() {
        assertEventTriggered(0, EagerFetchingEvent.class);
        assertEventTriggered(0, ManyToManyListEvent.class);
        assertEventTriggered(0, OneToOneParentSideEvent.class);
        assertEventTriggered(0, OneToOneWithoutMapsIdEvent.class);
        assertEventTriggered(0, SkipAutoCommitCheckEvent.class);
        assertEventTriggered(0, SchemaGenerationEvent.class);
        assertEventTriggered(0, QueryPaginationCollectionFetchingEvent.class);
        assertEventTriggered(0, QueryInClauseParameterPaddingEvent.class);
    }

    protected void assertEventTriggered(int expectedCount, Class<? extends Event> eventClass) {
        int count = 0;

        for (Event event : listEventHandler.getEvents()) {
            if (event.getClass().equals(eventClass)) {
                count++;
            }
        }

        assertSame(expectedCount, count);
    }

}