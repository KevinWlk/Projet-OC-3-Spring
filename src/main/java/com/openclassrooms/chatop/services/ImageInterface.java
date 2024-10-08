package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.exceptions.FormatNotSupportedException;
import com.openclassrooms.chatop.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageInterface {
    long uploadImage(MultipartFile file) throws IOException, FormatNotSupportedException;

    Image getImageById(Long id) throws FileNotFoundException;
}