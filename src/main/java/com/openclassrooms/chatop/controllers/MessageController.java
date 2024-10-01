package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.MessageRequest;
import com.openclassrooms.chatop.dtos.MessageResponse;
import com.openclassrooms.chatop.exceptions.NotFoundException;
import com.openclassrooms.chatop.services.MessageService;
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

    @Operation(summary = "Créer un nouveau message", description = "Permet de créer un nouveau message lié à une location et un utilisateur.")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/messages", consumes = APPLICATION_JSON_VALUE)
    public MessageResponse createUser(@Valid @RequestBody MessageRequest messageRequest) throws NotFoundException {
        return messageService.createMessage(messageRequest);
    }
}
