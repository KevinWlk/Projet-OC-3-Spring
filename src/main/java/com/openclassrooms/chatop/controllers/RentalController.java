package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.dtos.RentalRequest;
import com.openclassrooms.chatop.dtos.RentalResponse;
import com.openclassrooms.chatop.exceptions.AlreadyExistException;
import com.openclassrooms.chatop.exceptions.FormatNotSupportedException;
import com.openclassrooms.chatop.exceptions.NotFoundException;
import com.openclassrooms.chatop.services.RentalService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
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

    @Operation(summary = "Créer une nouvelle location",
            description = "Permet de créer une nouvelle annonce de location.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Les informations de la location",
                    content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = RentalRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Location créée avec succès", content = @Content(
                            schema = @Schema(implementation = RentalResponse.class),
                            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "name": "Belle maison",
                        "surface": 120,
                        "price": 1500,
                        "picture": "https://example.com/image.jpg",
                        "description": "Maison avec piscine"
                    }
                    """))),
                    @ApiResponse(responseCode = "400", description = "Erreur dans la requête")
            })
    @PostMapping(value = "/rentals", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RentalResponse createRental(@Valid @ModelAttribute RentalRequest rentalRequest)
            throws AlreadyExistException, NotFoundException, IOException, FormatNotSupportedException {
        return rentalService.createRental(rentalRequest);
    }

    @Operation(summary = "Récupérer toutes les locations",
            description = "Retourne la liste de toutes les locations.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des locations récupérée avec succès", content = @Content(
                            schema = @Schema(implementation = RentalResponse.class),
                            examples = @ExampleObject(value = """
                    {
                        "rentals": [
                            {
                                "id": 1,
                                "name": "Maison avec jardin",
                                "surface": 150,
                                "price": 2000,
                                "picture": "https://example.com/image.jpg",
                                "description": "Grande maison avec un jardin"
                            }
                        ]
                    }
                    """))),
                    @ApiResponse(responseCode = "404", description = "Aucune location trouvée")
            })
    @GetMapping("/rentals")
    public Map<String, List<RentalResponse>> getRentals() {
        List<RentalResponse> rentalList = rentalService.getRentals();
        Map<String, List<RentalResponse>> response = new HashMap<>();
        response.put("rentals", rentalList);
        return response;
    }

    @Operation(summary = "Récupérer une location par ID",
            description = "Permet de récupérer une location spécifique par son identifiant.",
            parameters = @Parameter(name = "id", description = "ID de la location", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Location récupérée avec succès", content = @Content(
                            schema = @Schema(implementation = RentalResponse.class),
                            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "name": "Maison au bord de la mer",
                        "surface": 100,
                        "price": 3000,
                        "picture": "https://example.com/image.jpg",
                        "description": "Maison au bord de la mer avec vue imprenable"
                    }
                    """))),
                    @ApiResponse(responseCode = "404", description = "Location non trouvée")
            })
    @GetMapping("/rentals/{id}")
    public RentalResponse getRental(@PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).") int id)
            throws NotFoundException {
        return rentalService.getRental(id);
    }

    @Operation(summary = "Mettre à jour une location",
            description = "Permet de mettre à jour les informations d'une location existante.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Les informations de la location à mettre à jour",
                    content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = RentalRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Location mise à jour avec succès", content = @Content(
                            schema = @Schema(implementation = RentalResponse.class),
                            examples = @ExampleObject(value = """
                    {
                        "id": 1,
                        "name": "Maison rénovée",
                        "surface": 120,
                        "price": 1500,
                        "picture": "https://example.com/image.jpg",
                        "description": "Maison avec un jardin et une piscine"
                    }
                    """))),
                    @ApiResponse(responseCode = "404", description = "Location non trouvée")
            })
    @PutMapping(value = "/rentals/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RentalResponse updateRental(@PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).") int id,
                                       @Valid @ModelAttribute RentalRequest rentalRequest)
            throws NotFoundException {
        return rentalService.updateRental(id, rentalRequest);
    }
}
