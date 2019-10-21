package ru.lagoshny.task.manager.domain.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.lagoshny.task.manager.domain.entity.enums.TaskPriorityEnum;
import ru.lagoshny.task.manager.domain.entity.enums.TaskStatusEnum;
import ru.lagoshny.task.manager.domain.validator.group.ChangeTaskGroup;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_author_number_category",
                columnNames = {"author_id", "category_id", "number"}
        )}
)
public class Task extends AbstractIdPersistence {

    /**
     * Task number.
     * The number generated on the server-side before saving {@link Task} to the database.
     * Task number depends on {@link TaskCategory}, each task category starts task number from 0.
     */
    @NotNull(groups = {ChangeTaskGroup.class})
    @Column(nullable = false)
    private Long number;

    /**
     * Task name.
     */
    @NotBlank
    @Column(nullable = false)
    private String name;

    /**
     * Task description.
     */
    @Column
    private String description;

    /**
     * Date when the task was created.
     */
    @NotNull
    @Column(nullable = false)
    private Date creationDate;

    /**
     * {@link User} who create this task.
     */
    @NotNull
    @Fetch(FetchMode.SELECT)
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_task_author"))
    private User author;

    /**
     * Task category {@link TaskCategory}.
     * If the task will create with {@code null} category,
     * then {@link TaskCategory#getUndefined()} will be used by default.
     */
    @NotNull(groups = {ChangeTaskGroup.class})
    @ManyToOne(optional = false)
    private TaskCategory category;

    /**
     * Task priority.
     * Possible values {@link TaskPriorityEnum}.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriorityEnum priority;

    /**
     * Task's status.
     * Possible values {@link TaskStatusEnum}.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatusEnum status;

    /**
     * {@code true} when need task time management, {@code false} otherwise.
     */
    @Column
    private boolean needTimeManagement;

    /**
     * Total time allotted for the task.
     */
    @Column
    private int totalTime;

    /**
     * {@code true} when need auto reduce total time, {@code false} otherwise.
     */
    @Column
    private boolean autoReduce;

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public TaskCategory getCategory() {
        return category;
    }

    public void setCategory(TaskCategory category) {
        this.category = category;
    }

    public TaskPriorityEnum getPriority() {
        return priority;
    }

    public void setPriority(TaskPriorityEnum priority) {
        this.priority = priority;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }

    public boolean isNeedTimeManagement() {
        return needTimeManagement;
    }

    public void setNeedTimeManagement(boolean needTimeManagement) {
        this.needTimeManagement = needTimeManagement;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public boolean isAutoReduce() {
        return autoReduce;
    }

    public void setAutoReduce(boolean autoReduce) {
        this.autoReduce = autoReduce;
    }

}
