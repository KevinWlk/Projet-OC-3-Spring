package com.openclassrooms.chatop.controllers;

import com.openclassrooms.chatop.exceptions.FormatNotSupportedException;
import com.openclassrooms.chatop.services.ImageService;
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

    @Operation(summary = "Télécharger une image", description = "Permet de télécharger une image sur le serveur.")
    @PostMapping("/upload/image")
    public String uploadImage(@RequestParam("image") MultipartFile file) throws IOException, FormatNotSupportedException {
        return imageService.uploadImage(file);
    }

    @Operation(summary = "Récupérer une image", description = "Permet de récupérer une image par son nom.")
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
