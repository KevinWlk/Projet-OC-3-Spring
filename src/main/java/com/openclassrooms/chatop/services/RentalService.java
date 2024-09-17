package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.dtos.RentalRequest;
import com.openclassrooms.chatop.dtos.RentalResponse;
import com.openclassrooms.chatop.models.Rental;
import com.openclassrooms.chatop.models.User;
import com.openclassrooms.chatop.exceptions.AlreadyExistException;
import com.openclassrooms.chatop.exceptions.FormatNotSupportedException;
import com.openclassrooms.chatop.exceptions.NotFoundException;
import com.openclassrooms.chatop.mappers.RentalDTOMapper;
import com.openclassrooms.chatop.repositories.RentalRepository;
import com.openclassrooms.chatop.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class RentalService implements RentalInterface {

    private final RentalRepository rentalRepository;
    private final RentalDTOMapper rentalDTOMapper;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private static final Logger logger = LoggerFactory.getLogger(RentalService.class);

    public RentalService(RentalRepository rentalRepository, RentalDTOMapper rentalDTOMapper, UserRepository userRepository, ImageService imageService) {
        this.rentalRepository = rentalRepository;
        this.rentalDTOMapper = rentalDTOMapper;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    @Override
    public RentalResponse createRental(RentalRequest rentalRequest) throws AlreadyExistException, NotFoundException, IOException, FormatNotSupportedException {
        Optional<Rental> rentalInDB = rentalRepository.findByName(rentalRequest.getName());
        if (rentalInDB.isPresent()) {
            throw new AlreadyExistException("Ce nom n'est plus disponible.");
        }

        Rental rental = new Rental();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        logger.info(currentUser.getId().toString());
        Optional<User> userInDB = userRepository.findById(currentUser.getId());
        User user;
        if (userInDB.isPresent()) {
            logger.info(String.valueOf(userInDB.get()));
            rental.setOwner(userInDB.get());
        } else {
            throw new NotFoundException("Utilisateur non référencé.");
        }

        rental.setName(rentalRequest.getName());
        rental.setSurface(rentalRequest.getSurface());
        rental.setPrice(rentalRequest.getPrice());
        rental.setDescription(rentalRequest.getDescription());

        String imageName = imageService.uploadImage(rentalRequest.getPicture());
        String pictureURL = "http://localhost:3001/api/get/image/" + imageName;
        rental.setPicture(pictureURL);

        rental.setCreatedAt(LocalDate.now());
        rental.setUpdatedAt(LocalDate.now());

        rentalRepository.save(rental);

        return new RentalResponse(rental.getId(), rental.getName(), rental.getSurface(), rental.getPrice(), rental.getPicture(), rental.getDescription(), rental.getOwner().getId(), rental.getCreatedAt(), rental.getUpdatedAt());
    }

    @Override
    public List<RentalResponse> getRentals() {
        return rentalRepository.findAll()
                .stream().map(rentalDTOMapper).toList();
    }

    @Override
    public RentalResponse getRental(Integer id) throws NotFoundException {
        Optional<Rental> rentalInDB = rentalRepository.findById(id);
        if(rentalInDB.isPresent()) {
            Rental rental = rentalInDB.get();
            return new RentalResponse(rental.getId(), rental.getName(), rental.getSurface(), rental.getPrice(), rental.getPicture(), rental.getDescription(), rental.getOwner().getId(), rental.getCreatedAt(), rental.getUpdatedAt());
        } else {
            throw new NotFoundException("Location non référencée.");
        }
    }

    @Override
    public RentalResponse updateRental(Integer id, RentalRequest rentalRequest) throws NotFoundException {
        Optional<Rental> rentalInDB = rentalRepository.findById(id);

        if (rentalInDB.isPresent()) {
            Rental rental = rentalInDB.get();

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
            return new RentalResponse(rental.getId(), rental.getName(), rental.getSurface(), rental.getPrice(), rental.getPicture(), rental.getDescription(), rental.getOwner().getId(), rental.getCreatedAt(), rental.getUpdatedAt());
        } else {
            throw new NotFoundException("Location non référencée.");
        }
    }
}