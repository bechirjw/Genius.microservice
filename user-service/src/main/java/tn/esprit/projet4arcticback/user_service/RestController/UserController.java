package tn.esprit.projet4arcticback.user_service.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.projet4arcticback.user_service.entity.Role;
import tn.esprit.projet4arcticback.user_service.entity.User;
import tn.esprit.projet4arcticback.user_service.service.UserService;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userservice;

    @GetMapping
    public List<User> getUsers() {
        return userservice.getAllUsers();
    }
    @PreAuthorize("isAuthenticated()")

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        System.out.println("Connected user: " + connectedUser); // 👈

        userservice.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userservice.getUserById(id); // Pas besoin d'Optional ici
        return ResponseEntity.ok(user);
    }
    @PutMapping("/{id}/change-role")
    public ResponseEntity<User> changeRole(@PathVariable Long id, @RequestBody Role newRole) {
        return ResponseEntity.ok(userservice.changeRole(id, newRole));
    }
    @PatchMapping("/ban/{id}")
    public ResponseEntity<?> banUser(@PathVariable Long id) {
        try {
            userservice.banUser(id);
            return ResponseEntity.ok("User account locked successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/change-role/{id}")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id, @RequestParam String role)
    {
        try {
            userservice.changeUserRole(id, role);
            return ResponseEntity.ok("User role changed successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PatchMapping("/unban/{id}")
    public ResponseEntity<?> unbanUser(@PathVariable Long id) {
        try {
            userservice.unbanUser(id);
            return ResponseEntity.ok("User account unlocked successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}