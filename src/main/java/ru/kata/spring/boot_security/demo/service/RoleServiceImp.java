package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Optional;
@Service
public class RoleServiceImp implements RoleService {

    @Override
    public Optional<Role> getAllRoles() {
        return Optional.empty();
    }
}
