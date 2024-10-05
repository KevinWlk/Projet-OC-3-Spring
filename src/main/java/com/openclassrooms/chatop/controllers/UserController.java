package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.UserResponse;
import com.openclassrooms.chatop.exceptions.NotFoundException;
import com.openclassrooms.chatop.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Récupérer les informations d'un utilisateur par ID",
            description = "Permet de récupérer les informations d'un utilisateur spécifique grâce à son ID.",
            parameters = @Parameter(name = "id", description = "ID de l'utilisateur à récupérer", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Utilisateur récupéré avec succès", content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(value = """
                {
                    "id": 1,
                    "name": "Antoine Dupont",
                    "email": "antoine.dupont@example.com",
                    "createdAt": "2024-01-01T10:00:00Z",
                    "updatedAt": "2024-10-01T10:00:00Z"
                }
                """))),
                    @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = @Content(
                            schema = @Schema(implementation = NotFoundException.class),
                            examples = @ExampleObject(value = """
                {
                    "message": "Utilisateur introuvable avec cet ID"
                }
                """)))
            })
    @GetMapping("/user/{id}")
    public UserResponse getUser(@PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).") int id) throws NotFoundException {
        return userService.getUser(id);
    }
}
