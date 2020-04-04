package ru.lagoshny.task.manager.web.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lagoshny.task.manager.domain.entity.TaskCategory;
import ru.lagoshny.task.manager.domain.entity.User;
import ru.lagoshny.task.manager.domain.entity.UserRole;
import ru.lagoshny.task.manager.domain.entity.enums.UserRoleEnum;
import ru.lagoshny.task.manager.domain.repository.TaskCategoryRepository;
import ru.lagoshny.task.manager.domain.repository.UserRepository;
import ru.lagoshny.task.manager.domain.repository.UserRoleRepository;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final TaskCategoryRepository taskCategoryRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       TaskCategoryRepository taskCategoryRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.taskCategoryRepository = taskCategoryRepository;
    }

    @Transactional
    public User saveUser(@NotNull User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        final User savedUser = userRepository.save(user);
        userRoleRepository.save(new UserRole(savedUser, UserRoleEnum.ROLE_USER));

        final TaskCategory defaultTaskCategory = TaskCategory.getDefault();
        defaultTaskCategory.setUser(savedUser);
        taskCategoryRepository.save(defaultTaskCategory);

        return savedUser;
    }

}
