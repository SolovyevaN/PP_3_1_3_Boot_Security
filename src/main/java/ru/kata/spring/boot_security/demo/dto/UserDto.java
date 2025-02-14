package ru.kata.spring.boot_security.demo.dto;

import lombok.Data;
import lombok.NonNull;


@Data
public class UserDto {
    private Long id;
    @NonNull
    private String name;
    private String surname;
    private int age;
    private String password;
    private String email;
    private String role;
}
