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
}