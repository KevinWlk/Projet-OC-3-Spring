package com.openclassrooms.chatop.mappers;

import com.openclassrooms.chatop.dtos.RentalRequest;
import com.openclassrooms.chatop.dtos.RentalResponse;
import com.openclassrooms.chatop.models.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RentalMapper {

    RentalMapper INSTANCE = Mappers.getMapper(RentalMapper.class);

    // Convert Rental to RentalResponse
    @Mapping(source = "owner.id", target = "owner_id")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    RentalResponse rentalToRentalResponse(Rental rental);

    // Convert RentalRequest to Rental
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "owner", ignore = true) // Owner will be set separately
    @Mapping(target = "picture", ignore = true) // Picture will be handled separately
    Rental rentalRequestToRental(RentalRequest rentalRequest);
}
