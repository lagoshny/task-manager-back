package ru.lagoshny.task.manager.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.entity.User;
import ru.lagoshny.task.manager.domain.entity.UserRole;
import ru.lagoshny.task.manager.domain.entity.enums.UserRoleEnum;
import ru.lagoshny.task.manager.domain.repository.TaskCategoryRepository;
import ru.lagoshny.task.manager.domain.repository.UserRepository;
import ru.lagoshny.task.manager.domain.repository.UserRoleRepository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private TaskCategoryRepository taskCategoryRepository;

    @Test
    public void shouldCreateNewUserWithRoleUser() {
        User userToSave = new User();
        userToSave.setUsername("Test");
        userToSave.setPassword("Test123");
        userToSave.setEmail("test@test.com");
        when(userRepository.save(userToSave)).thenReturn(userToSave);

        userService.saveUser(userToSave);

        verify(userRoleRepository).save(new UserRole(userToSave, UserRoleEnum.ROLE_USER));
    }

    @Test
    public void shouldCreateDefaultCategoryForNewUser() {
        User userToSave = new User();
        userToSave.setUsername("Test");
        userToSave.setPassword("Test123");
        userToSave.setEmail("test@test.com");
        when(userRepository.save(userToSave)).thenReturn(userToSave);

        userService.saveUser(userToSave);

        final TaskCategory defaultTaskCategory = TaskCategory.getDefault();
        defaultTaskCategory.setUser(userToSave);
        verify(taskCategoryRepository).save(defaultTaskCategory);
    }

}