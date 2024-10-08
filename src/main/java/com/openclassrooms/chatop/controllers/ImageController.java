package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.exceptions.FormatNotSupportedException;
import com.openclassrooms.chatop.services.ImageService;
import com.openclassrooms.chatop.models.Image;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Image", description = "Gestion des images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Operation(summary = "Télécharger une image",
            description = "Permet de télécharger une image sur le serveur.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary"))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Image téléchargée avec succès", content = @Content(
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Image téléchargée avec succès"))),
                    @ApiResponse(responseCode = "400", description = "Erreur dans le format de l'image")
            })
    @PostMapping("/upload/image")
    public Long uploadImage(@RequestParam("image") MultipartFile file) throws IOException, FormatNotSupportedException {
        return imageService.uploadImage(file);
    }

    @Operation(summary = "Récupérer une image",
            description = "Permet de récupérer une image par son ID.",
            parameters = @Parameter(name = "id", description = "ID de l'image à récupérer", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Image récupérée avec succès", content = @Content(
                            schema = @Schema(type = "string", format = "binary"))),
                    @ApiResponse(responseCode = "404", description = "Image non trouvée")
            })
    @GetMapping(path = {"/get/image/{id}"})
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) throws FileNotFoundException, FormatNotSupportedException {
        HttpHeaders headers = new HttpHeaders();

        // Récupérer l'image en utilisant l'ID
        Image image = imageService.getImageById(id);

        // Vérifier le type d'extension pour définir le type de contenu
        String extension = image.getName().substring(image.getName().lastIndexOf(".") + 1);
        if (extension.equals("jpg") || extension.equals("jpeg")) {
            headers.setContentType(MediaType.IMAGE_JPEG);
        } else if (extension.equals("png")) {
            headers.setContentType(MediaType.IMAGE_PNG);
        } else {
            throw new FormatNotSupportedException("Format invalide (doit être : \".jpeg\", \".jpg\" ou \".png\").");
        }

        // Retourner les octets de l'image
        return new ResponseEntity<>(image.getBytes(), headers, HttpStatus.OK);
    }

}

