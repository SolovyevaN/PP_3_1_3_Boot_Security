package ru.kata.spring.boot_security.demo.service;


import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.exception.DuplicatePasswordException;
import ru.kata.spring.boot_security.demo.exception.UserNotFindExeption;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,RoleRepository roleRepository ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void addUser(User user) {
        Optional<User> existingUser = userRepository.findByPassword(user.getPassword());
        if (existingUser.isPresent()) {
            throw new DuplicatePasswordException("Пользователь с таким паролем уже существует!");
        }
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new UserNotFindExeption("User with id " + user.getId() + " not found");
        }
        userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFindExeption("User with id " + id + " not found"));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFindExeption("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Hibernate.initialize(user.getRoles());
        return user;
    }
    @Transactional
    public void changeRole(Long id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        if ("ROLE_ADMIN".equals(role)) {
            user.getRoles().add(adminRole);
        } else {
            user.getRoles().remove(adminRole);
        }
        userRepository.save(user);
    }
}
