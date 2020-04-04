package ru.lagoshny.task.manager.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.lagoshny.task.manager.domain.validator.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_login", columnNames = "username"))
public class User extends AbstractIdPersistence {

    /**
     * User login.
     */
    @NotBlank
    @Size(max = 100)
    @LatinWithNumbers
    @Column(nullable = false)
    private String username;

    /**
     * User password.
     * Using Access.WRITE_ONLY because we allow to write password when create user, but don't allow to send password
     * when read user object.
     */
    @NotBlank
    @Size(max = 100)
    @Password
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    /**
     * User email.
     */
    @NotBlank
    @Size(max = 50)
    @Email
    @Column(nullable = false)
    private String email;

    /**
     * User first name.
     */
    @Size(max = 100)
    @LatinOrCyrillic
    @Column
    private String firstName;

    /**
     * User middle name.
     */
    @Size(max = 100)
    @LatinOrCyrillic
    @Column
    private String middleName;

    /**
     * User last name.
     */
    @Size(max = 100)
    @LatinOrCyrillic
    @Column
    private String lastName;

    /**
     * User birthday.
     */
    @NotFeatureDate
    @Column
    private Date birthday;

    /**
     * User city.
     */
    @Size(max = 50)
    @Column
    private String city;

    /**
     * List of the user tasks.
     */
    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private Collection<Task> tasks = new ArrayList<>();

    /**
     * {@code true} when user active, {@code false} otherwise.
     */
    @Column(nullable = false)
    private boolean enabled = true;

    public User() {
    }

    public User(@org.jetbrains.annotations.NotNull Long id) {
        this.id = id;
    }

    public User(User src) {
        this.username = src.username;
        this.password = src.password;
        this.email = src.email;
        this.firstName = src.firstName;
        this.middleName = src.middleName;
        this.lastName = src.lastName;
        this.birthday = src.birthday;
        this.city = src.city;
        this.tasks = src.tasks;
        this.enabled = src.enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
