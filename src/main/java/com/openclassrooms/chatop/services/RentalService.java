package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.dtos.RentalRequest;
import com.openclassrooms.chatop.dtos.RentalResponse;
import com.openclassrooms.chatop.models.Rental;
import com.openclassrooms.chatop.models.User;
import com.openclassrooms.chatop.exceptions.AlreadyExistException;
import com.openclassrooms.chatop.exceptions.FormatNotSupportedException;
import com.openclassrooms.chatop.exceptions.NotFoundException;
import com.openclassrooms.chatop.repositories.RentalRepository;
import com.openclassrooms.chatop.repositories.UserRepository;
import com.openclassrooms.chatop.mappers.RentalMapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class RentalService implements RentalInterface {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    public RentalService(RentalRepository rentalRepository, UserRepository userRepository, ImageService imageService) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    @Override
    public RentalResponse createRental(RentalRequest rentalRequest) throws AlreadyExistException, NotFoundException, IOException, FormatNotSupportedException {
        Optional<Rental> rentalInDB = rentalRepository.findByName(rentalRequest.getName());
        if (rentalInDB.isPresent()) {
            throw new AlreadyExistException("Ce nom n'est plus disponible.");
        }

        Rental rental = RentalMapper.INSTANCE.rentalRequestToRental(rentalRequest); // Utilisation de MapStruct

        // Gestion de l'utilisateur actuel
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Optional<User> userInDB = userRepository.findById(currentUser.getId());

        if (userInDB.isPresent()) {
            rental.setOwner(userInDB.get());
        } else {
            throw new NotFoundException("Utilisateur non référencé.");
        }

// Gestion de l'image avec chemin relatif
        Long imageId = imageService.uploadImage(rentalRequest.getPicture());
        String pictureURL = "/api/get/image/" + imageId;  // Chemin relatif basé sur l'ID
        rental.setPicture(pictureURL);


        rental.setCreatedAt(LocalDate.now());
        rental.setUpdatedAt(LocalDate.now());

        rentalRepository.save(rental);

        return RentalMapper.INSTANCE.rentalToRentalResponse(rental); // Utilisation de MapStruct
    }

    @Override
    public List<RentalResponse> getRentals() {
        return rentalRepository.findAll()
                .stream()
                .map(RentalMapper.INSTANCE::rentalToRentalResponse)
                .toList();
    }

    @Override
    public RentalResponse getRental(Integer id) throws NotFoundException {
        Optional<Rental> rentalInDB = rentalRepository.findById(id);
        if (rentalInDB.isPresent()) {
            return RentalMapper.INSTANCE.rentalToRentalResponse(rentalInDB.get());
        } else {
            throw new NotFoundException("Location non référencée.");
        }
    }

    @Override
    public RentalResponse updateRental(Integer id, RentalRequest rentalRequest) throws NotFoundException {
        Optional<Rental> rentalInDB = rentalRepository.findById(id);

        if (rentalInDB.isPresent()) {
            Rental rental = rentalInDB.get();

            // Mise à jour des champs
            if (rentalRequest.getName() != null) {
                rental.setName(rentalRequest.getName());
            }
            if (rentalRequest.getSurface() != null) {
                rental.setSurface(rentalRequest.getSurface());
            }
            if (rentalRequest.getPrice() != null) {
                rental.setPrice(rentalRequest.getPrice());
            }
            if (rentalRequest.getDescription() != null) {
                rental.setDescription(rentalRequest.getDescription());
            }

            rental.setUpdatedAt(LocalDate.now());
            rentalRepository.save(rental);

            return RentalMapper.INSTANCE.rentalToRentalResponse(rental);
        } else {
            throw new NotFoundException("Location non référencée.");
        }
    }
}
