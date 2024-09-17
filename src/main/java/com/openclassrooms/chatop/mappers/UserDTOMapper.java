package com.openclassrooms.chatop.mappers;

import com.openclassrooms.chatop.dtos.UserResponse;
import com.openclassrooms.chatop.models.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserDTOMapper implements Function<User, UserResponse> {
    @Override
    public UserResponse apply(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
    }
}