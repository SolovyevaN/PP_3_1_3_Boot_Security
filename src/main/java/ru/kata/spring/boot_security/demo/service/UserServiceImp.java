package ru.kata.spring.boot_security.demo.service;


import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.exception.DuplicatePasswordException;
import ru.kata.spring.boot_security.demo.exception.UserNotFindExeption;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class UserServiceImp implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;


    public UserServiceImp(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Transactional
    public void addUser(UserDto userDto) {
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            throw new DuplicatePasswordException("Пользователь с таким логином уже существует!");
        }
        Role userRole = roleService.getNameRoles(userDto.getRole())
                .orElseThrow(() -> new RuntimeException("Роль не найдена"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        User user = new User(userDto.getName(), userDto.getSurname(), userDto.getAge(),
                userDto.getPassword(), userDto.getEmail(), roles);
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFindExeption("User with id " + userDto.getId() + " not found"));
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setAge(userDto.getAge());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        if (userDto.getRole() != null) {
            Set<Role> roles = new HashSet<>(user.getRoles());
            Role adminRole = roleService.getNameRoles("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            if ("ROLE_ADMIN".equals(userDto.getRole())) {
                roles.add(adminRole);
            } else {
                roles.remove(adminRole);
            }
            user.setRoles(roles);
        }
        userRepository.save(user);
    }


    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFindExeption("User with id " + id + " not found"));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFindExeption("User with id " + id + " not found"));
        user.getRoles().clear();
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Hibernate.initialize(user.getRoles());
        return user;
    }
}
