package ru.lagoshny.task.manager.domain.entity.projection;

import org.springframework.data.rest.core.config.Projection;
import ru.lagoshny.task.manager.domain.entity.Task;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.entity.User;
import ru.lagoshny.task.manager.domain.entity.enums.TaskPriorityEnum;
import ru.lagoshny.task.manager.domain.entity.enums.TaskStatusEnum;

import java.time.LocalDateTime;

/**
 * Spring data rest projection for {@link Task} resource.
 * This projection allow to see inline all tasks's resource associations.
 */
@Projection(name = "taskProjection", types = Task.class)
public interface TaskProjection {

    String getId();

    Long getNumber();

    String getName();

    String getDescription();

    LocalDateTime getCreationDate();

    User getAuthor();

    TaskCategory getCategory();

    TaskPriorityEnum getPriority();

    TaskStatusEnum getStatus();

    boolean isNeedTimeManagement();

    Integer getTotalTime();

    Integer getSpentTime();

    boolean isAutoReduce();

}
