package com.openclassrooms.chatop.mappers;

import com.openclassrooms.chatop.dtos.MessageRequest;
import com.openclassrooms.chatop.dtos.MessageResponse;
import com.openclassrooms.chatop.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    // Convert Message to MessageResponse
    @Mapping(source = "rental.id", target = "rental")
    @Mapping(source = "user.id", target = "user")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    MessageResponse messageToMessageResponse(Message message);

    // Convert MessageRequest to Message
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rental", ignore = true) // Rental will be set separately
    @Mapping(target = "user", ignore = true) // User will be set separately
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDate.now())")
    Message messageRequestToMessage(MessageRequest messageRequest);
}
