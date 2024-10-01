package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.RentalRequest;
import com.openclassrooms.chatop.dtos.RentalResponse;
import com.openclassrooms.chatop.exceptions.AlreadyExistException;
import com.openclassrooms.chatop.exceptions.FormatNotSupportedException;
import com.openclassrooms.chatop.exceptions.NotFoundException;
import com.openclassrooms.chatop.services.RentalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Rental", description = "Gestion des locations")
public class RentalController {
    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @Operation(summary = "Créer une nouvelle location", description = "Permet de créer une nouvelle annonce de location.")
    @PostMapping(value = "/rentals", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RentalResponse createRental(@Valid @ModelAttribute RentalRequest rentalRequest) throws AlreadyExistException, NotFoundException, IOException, FormatNotSupportedException {
        return rentalService.createRental(rentalRequest);
    }

    @Operation(summary = "Récupérer toutes les locations", description = "Retourne la liste de toutes les locations.")
    @GetMapping("/rentals")
    public Map<String, List<RentalResponse>> getRentals() {
        List<RentalResponse> rentalList = rentalService.getRentals();
        Map<String, List<RentalResponse>> response = new HashMap<>();
        response.put("rentals", rentalList);
        return response;
    }

    @Operation(summary = "Récupérer une location par ID", description = "Permet de récupérer une location spécifique par son identifiant.")
    @GetMapping("/rentals/{id}")
    public RentalResponse getRental(@PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).") int id) throws NotFoundException {
        return rentalService.getRental(id);
    }

    @Operation(summary = "Mettre à jour une location", description = "Permet de mettre à jour les informations d'une location existante.")
    @PutMapping(value = "/rentals/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RentalResponse updateRental(@PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).") int id, @Valid @ModelAttribute RentalRequest rentalRequest) throws NotFoundException {
        return rentalService.updateRental(id, rentalRequest);
    }
}
