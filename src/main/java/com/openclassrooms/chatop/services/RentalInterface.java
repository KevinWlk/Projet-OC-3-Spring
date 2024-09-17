package com.openclassrooms.chatop.services;

import com.openclassrooms.chatop.dtos.RentalRequest;
import com.openclassrooms.chatop.dtos.RentalResponse;
import com.openclassrooms.chatop.exceptions.AlreadyExistException;
import com.openclassrooms.chatop.exceptions.FormatNotSupportedException;
import com.openclassrooms.chatop.exceptions.NotFoundException;

import java.io.IOException;
import java.util.List;

public interface RentalInterface {
    RentalResponse createRental(RentalRequest rentalRequest) throws AlreadyExistException, NotFoundException, IOException, FormatNotSupportedException;

    List<RentalResponse> getRentals();

    RentalResponse getRental(Integer id) throws NotFoundException;

    RentalResponse updateRental(Integer id, RentalRequest rentalRequest) throws NotFoundException;
}