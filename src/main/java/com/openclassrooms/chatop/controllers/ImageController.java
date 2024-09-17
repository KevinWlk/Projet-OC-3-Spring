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

@Slf4j
@RestController
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;
    private final Logger logger = LoggerFactory.getLogger(ImageController.class);

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload/image")
    public String uploadImage(@RequestParam("image") MultipartFile file) throws IOException, FormatNotSupportedException {
        return imageService.uploadImage(file);
    }

    @GetMapping(path = {"/get/image/{name}"})
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) throws FileNotFoundException, FormatNotSupportedException {
        HttpHeaders headers = new HttpHeaders();

        String extension = name.substring(name.lastIndexOf(".") + 1);
        logger.info(extension);
        if (extension.equals("jpg") || extension.equals("jpeg")) {
            headers.setContentType(MediaType.IMAGE_JPEG);
        } else if (extension.equals("png")) {
            headers.setContentType(MediaType.IMAGE_PNG);
        } else {
            throw new FormatNotSupportedException("Format invalide (doit être : \".jpeg\", \".jpg\" ou \".png\".");
        }

        byte[] imageData = imageService.getImage(name);


        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }
}