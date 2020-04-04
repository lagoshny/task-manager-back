package ru.lagoshny.task.manager.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import ru.lagoshny.task.manager.domain.validator.LatinWithNumbers;
import ru.lagoshny.task.manager.domain.validator.SymbolsWithNumbers;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class TaskCategory extends AbstractIdPersistence {

    /**
     * User who owner this category.
     * Each user has own category list.
     */
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_category_user"))
    private User user;

    /**
     * Category name.
     */
    @NotBlank
    @Size(max = 100)
    @SymbolsWithNumbers
    @Column(nullable = false)
    private String name;

    /**
     * Category description.
     */
    @Size(max = 255)
    @Column
    private String description;

    /**
     * Category short prefix which uses for building full task number,
     * which contains category prefix and task number.
     */
    @NotBlank
    @Size(max = 50)
    @LatinWithNumbers
    @Column(nullable = false)
    private String prefix;

    /**
     * Category icon.
     */
    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String icon;

    public TaskCategory() {
    }

    public TaskCategory(@org.jetbrains.annotations.NotNull final String name,
                        @org.jetbrains.annotations.NotNull final String prefix,
                        @org.jetbrains.annotations.NotNull final String icon,
                        @org.jetbrains.annotations.Nullable final String description) {
        this.name = name;
        this.prefix = prefix;
        this.icon = icon;
        this.description = description;
    }

    /**
     * Get default task category uses when creating a task without specifying a category.
     */
    public static TaskCategory getDefault() {
        return new TaskCategory("Default",
                "default",
                "fa-certificate",
                "This is default category");
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(final String icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(final Object rafThat) {
        if (this == rafThat) {
            return true;
        }
        if (!(rafThat instanceof TaskCategory)) {
            return false;
        }
        if (!super.equals(rafThat)) {
            return false;
        }
        final TaskCategory that = (TaskCategory) rafThat;
        return Objects.equals(this.user, that.user)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.description, that.description)
                && Objects.equals(this.prefix, that.prefix)
                && Objects.equals(this.icon, that.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, name, description, prefix, icon);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("user", user)
                .append("name", name)
                .append("description", description)
                .append("prefix", prefix)
                .append("icon", icon)
                .toString();
    }

}
