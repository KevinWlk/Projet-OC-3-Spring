package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.LoginResponse;
import com.openclassrooms.chatop.dtos.UserRequest;
import com.openclassrooms.chatop.dtos.UserResponse;
import com.openclassrooms.chatop.models.User;
import com.openclassrooms.chatop.exceptions.AlreadyExistException;
import com.openclassrooms.chatop.exceptions.NoUserInContextException;
import com.openclassrooms.chatop.exceptions.NotFoundException;
import com.openclassrooms.chatop.services.AuthentificationService;
import com.openclassrooms.chatop.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentification", description = "Gérer l'authentification des utilisateurs")
@RestController
@RequestMapping("/api")
@Validated
public class AuthentificationController {
    private final AuthentificationService authentificationService;
    private final UserService userService;

    public AuthentificationController(AuthentificationService authentificationService, UserService userService) {
        this.authentificationService = authentificationService;
        this.userService = userService;
    }

    @Operation(summary = "Enregistrer un nouvel utilisateur", description = "Permet d'enregistrer un utilisateur avec son email, son nom et son mot de passe.")
    @PostMapping("/auth/register")
    public LoginResponse register(@Valid @RequestBody UserRequest userRequest) throws AlreadyExistException, NotFoundException {
        // Enregistre un nouvel utilisateur
        userService.createUser(userRequest);
        return authentificationService.authenticate(userRequest);
    }

    @Operation(summary = "Authentifier un utilisateur", description = "Permet d'authentifier un utilisateur avec son email et son mot de passe.")
    @PostMapping("/auth/login")
    public LoginResponse authenticate(@Valid @RequestBody UserRequest userRequest) throws NotFoundException {
        // Authentifie un utilisateur existant
        return authentificationService.authenticate(userRequest);
    }

    @Operation(summary = "Récupérer les informations de l'utilisateur authentifié", description = "Retourne les informations de l'utilisateur actuellement connecté.")
    @GetMapping("/auth/me")
    public UserResponse authenticatedUser() throws NoUserInContextException {
        // Récupère les informations de l'utilisateur actuellement connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            User user = (User) authentication.getPrincipal();
            return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
        } catch (Exception e) {
            throw new NoUserInContextException("Aucun utilisateur authentifié.");
        }
    }
}
