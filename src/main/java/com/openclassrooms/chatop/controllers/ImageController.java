package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.exceptions.FormatNotSupportedException;
import com.openclassrooms.chatop.services.ImageService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public String uploadImage(@RequestParam("image") MultipartFile file) throws IOException, FormatNotSupportedException {
        return imageService.uploadImage(file);
    }

    @Operation(summary = "Récupérer une image",
            description = "Permet de récupérer une image par son nom.",
            parameters = @Parameter(name = "name", description = "Nom de l'image à récupérer", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Image récupérée avec succès", content = @Content(
                            schema = @Schema(type = "string", format = "binary"))),
                    @ApiResponse(responseCode = "404", description = "Image non trouvée")
            })
    @GetMapping(path = {"/get/image/{name}"})
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) throws FileNotFoundException, FormatNotSupportedException {
        HttpHeaders headers = new HttpHeaders();
        String extension = name.substring(name.lastIndexOf(".") + 1);
        if (extension.equals("jpg") || extension.equals("jpeg")) {
            headers.setContentType(MediaType.IMAGE_JPEG);
        } else if (extension.equals("png")) {
            headers.setContentType(MediaType.IMAGE_PNG);
        } else {
            throw new FormatNotSupportedException("Format invalide (doit être : \".jpeg\", \".jpg\" ou \".png\").");
        }
        byte[] imageData = imageService.getImage(name);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }
}

