package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.dtos.MessageRequest;
import com.openclassrooms.chatop.dtos.MessageResponse;
import com.openclassrooms.chatop.models.Message;
import com.openclassrooms.chatop.models.Rental;
import com.openclassrooms.chatop.models.User;
import com.openclassrooms.chatop.exceptions.NotFoundException;
import com.openclassrooms.chatop.repositories.MessageRepository;
import com.openclassrooms.chatop.repositories.RentalRepository;
import com.openclassrooms.chatop.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class MessageService implements MessageInterface {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository, RentalRepository rentalRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    public MessageResponse createMessage(MessageRequest messageRequest) throws NotFoundException {
        Message message = new Message();

        Optional<User> userInDB = userRepository.findById(messageRequest.getUser_id());
        if (userInDB.isPresent()) {
            message.setUser(userInDB.get());
        } else {
            throw new NotFoundException("Utilisateur non référencé.");
        }

        Optional<Rental> rentalInDB = rentalRepository.findById(messageRequest.getRental_id());
        if (rentalInDB.isPresent()) {
            message.setRental(rentalInDB.get());
        } else {
            throw new NotFoundException("Location non référencé.");
        }

        message.setMessage(messageRequest.getMessage());

        message.setCreatedAt(LocalDate.now());
        message.setUpdatedAt(LocalDate.now());

        messageRepository.save(message);
        return new MessageResponse(message.getId(), message.getRental().getId(), message.getUser().getId(), message.getMessage(), message.getCreatedAt(), message.getUpdatedAt());
    }
}