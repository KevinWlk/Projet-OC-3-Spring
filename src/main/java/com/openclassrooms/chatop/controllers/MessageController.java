package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.MessageRequest;
import com.openclassrooms.chatop.dtos.MessageResponse;
import com.openclassrooms.chatop.exceptions.NotFoundException;
import com.openclassrooms.chatop.services.MessageService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Validated
@RequestMapping("/api")
@Tag(name = "Message", description = "Gestion des messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary = "Créer un nouveau message",
            description = "Permet de créer un nouveau message lié à une location et un utilisateur.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Les informations du message à créer",
                    required = true,
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MessageRequest.class),
                            examples = @ExampleObject(value = """
                {
                    "userId": 1,
                    "rentalId": 2,
                    "message": "Je suis intéressé par la location."
                }
                """))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Message créé avec succès", content = @Content(
                            schema = @Schema(implementation = MessageResponse.class),
                            examples = @ExampleObject(value = """
                {
                    "id": 1,
                    "userId": 1,
                    "rentalId": 2,
                    "message": "Je suis intéressé par la location.",
                    "createdAt": "2024-10-05T12:34:56Z"
                }
                """))),
                    @ApiResponse(responseCode = "404", description = "Location ou utilisateur non trouvé", content = @Content(
                            schema = @Schema(implementation = NotFoundException.class),
                            examples = @ExampleObject(value = """
                {
                    "message": "Location ou utilisateur introuvable"
                }
                """)))
            })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/messages", consumes = APPLICATION_JSON_VALUE)
    public MessageResponse createUser(@Valid @RequestBody MessageRequest messageRequest) throws NotFoundException {
        return messageService.createMessage(messageRequest);
    }
}
