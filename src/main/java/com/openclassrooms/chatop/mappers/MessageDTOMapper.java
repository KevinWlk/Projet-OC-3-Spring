package com.openclassrooms.chatop.mappers;

import com.openclassrooms.chatop.dtos.MessageResponse;
import com.openclassrooms.chatop.models.Message;
import org.springframework.stereotype.Component;

import java.util.function.Function;

// Unuseful for now, because we return all attrs. But could change.
@Component
public class MessageDTOMapper implements Function<Message, MessageResponse> {
    @Override
    public MessageResponse apply(Message message) {
        return new MessageResponse(message.getId(), message.getRental().getId(), message.getUser().getId(), message.getMessage(), message.getCreatedAt(), message.getUpdatedAt());
    }
}