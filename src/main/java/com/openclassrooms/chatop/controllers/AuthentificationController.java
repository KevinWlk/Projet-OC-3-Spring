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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Enregistrer un nouvel utilisateur",
            description = "Permet d'enregistrer un utilisateur avec son email, son nom et son mot de passe.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Utilisateur enregistré avec succès", content = @Content(
                            schema = @Schema(implementation = LoginResponse.class),
                            examples = @ExampleObject(value = """
                {
                    "token": "eyJhbGciOiJIUzUxMiJ9..."
                }
                """))),
                    @ApiResponse(responseCode = "400", description = "Requête incorrecte"),
                    @ApiResponse(responseCode = "409", description = "Utilisateur déjà existant")
            })
    @PostMapping("/auth/register")
    public LoginResponse register(@Valid @RequestBody UserRequest userRequest) throws AlreadyExistException, NotFoundException {
        userService.createUser(userRequest);
        return authentificationService.authenticate(userRequest);
    }

    @Operation(summary = "Authentifier un utilisateur",
            description = "Permet d'authentifier un utilisateur avec son email et son mot de passe.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentification réussie", content = @Content(
                            schema = @Schema(implementation = LoginResponse.class),
                            examples = @ExampleObject(value = """
                {
                    "token": "eyJhbGciOiJIUzUxMiJ9..."
                }
                """))),
                    @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
            })
    @PostMapping("/auth/login")
    public LoginResponse authenticate(@Valid @RequestBody UserRequest userRequest) throws NotFoundException {
        return authentificationService.authenticate(userRequest);
    }

    @Operation(summary = "Récupérer les informations de l'utilisateur authentifié",
            description = "Retourne les informations de l'utilisateur actuellement connecté.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Utilisateur récupéré avec succès", content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(value = """
                {
                    "id": 1,
                    "name": "Antoine Dupont",
                    "email": "antoine.dupont@example.com",
                    "createdAt": "2024-01-01T10:00:00Z",
                    "updatedAt": "2024-01-10T10:00:00Z"
                }
                """))),
                    @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
            })
    @GetMapping("/auth/me")
    public UserResponse authenticatedUser() throws NoUserInContextException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            User user = (User) authentication.getPrincipal();
            return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
        } catch (Exception e) {
            throw new NoUserInContextException("Aucun utilisateur authentifié.");
        }
    }
}
