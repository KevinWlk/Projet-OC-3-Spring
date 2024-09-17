package com.openclassrooms.chatop.mappers;

import com.openclassrooms.chatop.dtos.RentalResponse;
import com.openclassrooms.chatop.models.Rental;
import org.springframework.stereotype.Component;

import java.util.function.Function;

// Unuseful for now, because we return all attrs. But could change.
@Component
public class RentalDTOMapper implements Function<Rental, RentalResponse> {
    @Override
    public RentalResponse apply(Rental rental) {
        return new RentalResponse(rental.getId(), rental.getName(), rental.getSurface(), rental.getPrice(), rental.getPicture(), rental.getDescription(), rental.getOwner().getId(), rental.getCreatedAt(), rental.getUpdatedAt());
    }
}