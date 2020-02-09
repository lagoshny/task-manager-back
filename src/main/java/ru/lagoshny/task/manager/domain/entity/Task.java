package ru.lagoshny.task.manager.domain.entity;

import org.springframework.hateoas.Identifiable;
import ru.lagoshny.task.manager.domain.entity.enums.TaskPriorityEnum;
import ru.lagoshny.task.manager.domain.entity.enums.TaskStatusEnum;
import ru.lagoshny.task.manager.domain.validator.group.ChangeTaskGroup;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_author_number_category",
                columnNames = {"author_id", "category_id", "number"}
        )}
)
public class Task implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_generator")
    @SequenceGenerator(name = "task_id_generator", sequenceName = "task_id_seq")
    private Long id;

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
     * Time when the task passed from NEW state to IN_PROGRESS.
     * This using for auto calculate spent task time only when {@link #autoReduce} is {@code true},
     * otherwise author logged spent time manually use {@link #spentTime} property.
     */
    @Column
    private LocalDateTime startedDate;

    /**
     * {@link User} who create this task.
     */
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_task_author"))
    private User author;

    /**
     * Task category {@link TaskCategory}.
     * If the task will create with {@code null} category,
     * then {@link TaskCategory#getDefault()} will be used by default.
     */
    @NotNull(groups = {ChangeTaskGroup.class})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_task_category"))
    private TaskCategory category;

    /**
     * Task priority.
     * Possible values {@link TaskPriorityEnum}.
     */
    @NotNull
    @Column(nullable = false)
    private TaskPriorityEnum priority;

    /**
     * Task's status.
     * Possible values {@link TaskStatusEnum}.
     */
    @NotNull
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
    private Integer totalTime;

    /**
     * Number of the time which spent to solve the task.
     */
    @Column
    private Integer spentTime;

    /**
     * {@code true} when need auto reduce total time, {@code false} otherwise.
     */
    @Column
    private boolean autoReduce;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(LocalDateTime startedDate) {
        this.startedDate = startedDate;
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

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Integer getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(Integer spentTime) {
        this.spentTime = spentTime;
    }

    public boolean isAutoReduce() {
        return autoReduce;
    }

    public void setAutoReduce(boolean autoReduce) {
        this.autoReduce = autoReduce;
    }

}
