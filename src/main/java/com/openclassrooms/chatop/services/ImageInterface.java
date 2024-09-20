package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.exceptions.FormatNotSupportedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageInterface {
    String uploadImage(MultipartFile file) throws IOException, FormatNotSupportedException;

    byte[] getImage(String name) throws FileNotFoundException;
}