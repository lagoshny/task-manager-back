package ru.lagoshny.task.manager.web.processor.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import ru.lagoshny.task.manager.domain.entity.Task;
import ru.lagoshny.task.manager.web.controller.TaskController;
import ru.lagoshny.task.manager.web.processor.ResourceLinkBuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static ru.lagoshny.task.manager.web.processor.task.TaskRelation.UPDATE_STATUS_REL;

/**
 * Custom resource processor for {@link Task} resource that allows adding new links to task's _link array.
 */
@Component
public class TaskResourceProcessor implements ResourceProcessor<Resource<Task>> {

    private ResourceLinkBuilder resourceLinkBuilder;

    @Autowired
    public TaskResourceProcessor(ResourceLinkBuilder resourceLinkBuilder) {
        this.resourceLinkBuilder = resourceLinkBuilder;
    }

    @Override
    public Resource<Task> process(Resource<Task> resource) {
        final Task task = resource.getContent();
        resource.add(resourceLinkBuilder.fixLinkTo(methodOn(TaskController.class)
                .updateTaskStatus(task.getId(), null,null)).withRel(UPDATE_STATUS_REL));

        return resource;
    }

}
