package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.annotation.PostConstruct;

@Component
public class Init {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public Init(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostConstruct
    @Transactional
    public void doInit() {
        if (userRepository.findAll().size() == 0) {
            User user = new User("admin", "admin", (byte)0, "admin", "100");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Role roleUser = new Role("ROLE_USER");
            Role roleAdmin = new Role("ROLE_ADMIN");
            roleRepository.save(roleAdmin);
            roleRepository.save(roleUser);
            userRepository.save(user);
            user.getRoles().add(roleRepository.findRoleByRole("ROLE_ADMIN"));
            userRepository.save(user);
            User userTwo = new User("user", "user", (byte)31, "user", "100");
            userTwo.setPassword(passwordEncoder.encode(userTwo.getPassword()));
            userRepository.save(userTwo);
            userTwo.getRoles().add(roleRepository.findRoleByRole("ROLE_USER"));
            userRepository.save(userTwo);
        }
    }
}