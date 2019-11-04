package ru.lagoshny.task.manager.domain.entity;

import ru.lagoshny.task.manager.domain.entity.enums.UserRoleEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Domain object for hold mapping {@link User} to {@link UserRoleEnum}.
 * This simple implementation means that one user has one role.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_user_role", columnNames = {"user_id", "role"}))
public class UserRole extends AbstractIdPersistence {

    /**
     * {@link User} who is assigned the {@link #role}.
     */
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    /**
     * Role {@link UserRoleEnum} that granted to {@link #user}.
     */
    @NotNull
    @Column(nullable = false)
    private UserRoleEnum role;

    public UserRole() {
    }

    public UserRole(@org.jetbrains.annotations.NotNull final User user,
                    @org.jetbrains.annotations.NotNull final UserRoleEnum role) {
        this.user = user;
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(final UserRoleEnum role) {
        this.role = role;
    }

    @Override
    public boolean equals(final Object rafThat) {
        if (this == rafThat) {
            return true;
        }
        if (!(rafThat instanceof UserRole)) {
            return false;
        }
        if (!super.equals(rafThat)) {
            return false;
        }
        final UserRole that = (UserRole) rafThat;
        return Objects.equals(this.user, that.user)
                && this.role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, role);
    }

}
