package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.UserDTO;
import com.example.studentmanagement.model.Role;
import com.example.studentmanagement.model.User;
import com.example.studentmanagement.service.RoleService;
import com.example.studentmanagement.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // Only users with ADMIN role can access all endpoints in this controller
@CrossOrigin(origins = "*")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService=roleService;
    }

    // Get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    // Get user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new user
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setBranch(userDTO.getBranch());
        user.setRollNumber(userDTO.getRollNumber());
        user.setEmployeeId(userDTO.getEmployeeId());
        user.setSemester(userDTO.getSemester());
        user.setStatus(userDTO.getStatus());
        user.setContactNumber(userDTO.getContactNumber());
        user.setEmergencyContactNumber(userDTO.getEmergencyContactNumber());
        user.setPermanentAddress(userDTO.getPermanentAddress());

        // Convert role names (Strings) to Role entities
        Set<Role> roles = userDTO.getRoles().stream()
            .map(roleName -> roleService.findByName(roleName))
            .collect(Collectors.toSet());
        user.setRoles(roles);
        

        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }
    
 // Create a new user
    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setBranch(userDTO.getBranch());
        user.setRollNumber(userDTO.getRollNumber());
        user.setEmployeeId(userDTO.getEmployeeId());
        user.setSemester(userDTO.getSemester());
        user.setStatus(userDTO.getStatus());
        user.setContactNumber(userDTO.getContactNumber());
        user.setEmergencyContactNumber(userDTO.getEmergencyContactNumber());
        user.setPermanentAddress(userDTO.getPermanentAddress());

        // Convert role names (Strings) to Role entities
        Set<Role> roles = userDTO.getRoles().stream()
            .map(roleName -> roleService.findByName(roleName))
            .collect(Collectors.toSet());
        user.setRoles(roles);
        

        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }



    // Update an existing user
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setPassword(user.getPassword());
                    existingUser.setRoles(user.getRoles());
                    userService.saveUser(existingUser);
                    return ResponseEntity.ok(existingUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
