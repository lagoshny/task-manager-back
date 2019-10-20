package ru.lagoshny.task.manager.domain.entity.enums;

import org.jetbrains.annotations.NotNull;

/**
 * Enumeration possible user roles.
 */
public enum UserRoleEnum {

    ROLE_SUPER_ADMIN("SUPER_ADMIN"),
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    /**
     * String user role name.
     */
    private String name;

    UserRoleEnum(@NotNull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
