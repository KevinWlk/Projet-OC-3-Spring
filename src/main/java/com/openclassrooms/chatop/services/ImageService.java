package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.models.Image;
import com.openclassrooms.chatop.exceptions.FormatNotSupportedException;
import com.openclassrooms.chatop.repositories.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

@Service
public class ImageService implements ImageInterface{

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException, FormatNotSupportedException {
        // Gets the filename.
        String imageFileName = file.getOriginalFilename();

        assert imageFileName != null;
        String extension = imageFileName.substring(imageFileName.lastIndexOf(".") + 1);

        // Accept the following extensions.
        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
            throw new FormatNotSupportedException("Format invalide (acceptés :\".jpeg\", \".jpg\" ou \".png\").");
        }

        Optional<Image> imageOptional = imageRepository.findByName(imageFileName);
        // If unknown from DB :
        if (imageOptional.isEmpty()) {
            imageRepository.save(Image.builder()
                    .name(imageFileName)
                    .type(file.getContentType())
                    .bytes(file.getBytes())
                    .build());
        }

        return imageFileName;
    }

    @Override
    public byte[] getImage(String name) throws FileNotFoundException {
        Optional<Image> dbImage = imageRepository.findByName(name);
        byte[] imageBytes;

        if (dbImage.isPresent()) {
            imageBytes = dbImage.get().getBytes();
        } else {
            throw new FileNotFoundException("Image non référencé : " + name);
        }
        return imageBytes;
    }
}