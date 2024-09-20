package com.openclassrooms.chatop.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    @NotNull(message = "La location doit figurer.")
    private Integer rental_id;

    @NotNull(message = "Le propriétaire doit figurer.")
    private Integer user_id;

    @Size(max = 2000)
    private String message;
}