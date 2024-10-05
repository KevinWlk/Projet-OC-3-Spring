package com.openclassrooms.chatop.mappers;

import com.openclassrooms.chatop.dtos.MessageRequest;
import com.openclassrooms.chatop.dtos.MessageResponse;
import com.openclassrooms.chatop.models.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {
    // Mapper qui convertit un objet Message en MessageResponse et vice-versa
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "rental.id", target = "rental")
    @Mapping(source = "user.id", target = "user")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    MessageResponse messageToMessageResponse(Message message);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rental", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDate.now())")
    Message messageRequestToMessage(MessageRequest messageRequest);
}
