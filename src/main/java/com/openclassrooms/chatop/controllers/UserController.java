package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.UserResponse;
import com.openclassrooms.chatop.exceptions.NotFoundException;
import com.openclassrooms.chatop.services.UserService;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Validated
@RequestMapping("/api")
@Tag(name = "User", description = "Gestion des utilisateurs")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Récupérer les informations d'un utilisateur par ID", description = "Permet de récupérer les informations d'un utilisateur spécifique grâce à son ID.")
    @GetMapping("/user/{id}")
    public UserResponse getUser(@PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).") int id) throws NotFoundException {
        return userService.getUser(id);
    }
}
