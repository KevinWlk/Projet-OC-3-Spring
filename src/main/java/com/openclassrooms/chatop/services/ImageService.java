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
    public long uploadImage(MultipartFile file) throws IOException, FormatNotSupportedException {
        String imageFileName = file.getOriginalFilename();
        assert imageFileName != null;
        String extension = imageFileName.substring(imageFileName.lastIndexOf(".") + 1);

        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
            throw new FormatNotSupportedException("Format invalide (acceptés :\".jpeg\", \".jpg\" ou \".png\").");
        }

        Image image = Image.builder()
                .name(imageFileName)
                .type(file.getContentType())
                .bytes(file.getBytes())
                .build();

        imageRepository.save(image);
        return image.getId();  // Renvoie l'ID de l'image.
    }


    public Image getImageById(Long id) throws FileNotFoundException {
        Optional<Image> dbImage = imageRepository.findById(id);
        if (dbImage.isPresent()) {
            return dbImage.get();
        } else {
            throw new FileNotFoundException("Image non référencée : " + id);
        }
    }

}