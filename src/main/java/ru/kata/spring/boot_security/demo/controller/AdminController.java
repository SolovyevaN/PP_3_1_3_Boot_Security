package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;
    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {

        this.userService = userService;
        this.roleRepository= roleRepository;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/")
    public String listUser(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("/addUser")
    public String addUser(@RequestParam("name") String name,
                          @RequestParam("surname") String surname,
                          @RequestParam("age") int age,
                          @RequestParam("password") String password,
                          @RequestParam("role") String role,
                          RedirectAttributes redirectAttributes) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setAge(age);
        user.setPassword(password);
        Role userRole = roleRepository.findByName(role)
                        .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(userRole);
        userService.addUser(user);
        redirectAttributes.addAttribute("message", "User added successfully! ");
        return "redirect:/admin";
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PutMapping("/updateUser/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @RequestParam("name") String name,
                             @RequestParam("surname") String surname,
                             @RequestParam("age") int age,
                             @RequestParam("password") String password) {
        User user = userService.findById(id);
        user.setName(name);
        user.setSurname(surname);
        user.setAge(age);
        user.setPassword(password);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/updateUser/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "/updateUser";
    }

    @GetMapping("/findById")
    public String findById(@RequestParam("id") Long id, Model model) {
        User user = userService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            model.addAttribute("errorMessage", "User not found!");
        }
        return "findById";
    }

    @PostMapping("/changeRole/{id}")
    public String changeRole(@PathVariable Long id, @RequestParam String role) {
        userService.changeRole(id, role);
        return "redirect:/admin";
    }
}
