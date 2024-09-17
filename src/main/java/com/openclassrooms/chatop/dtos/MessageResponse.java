package com.openclassrooms.chatop.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Integer id;
    private Integer rental;
    private Integer user;
    private String message;
    private LocalDate created_at;
    private LocalDate updated_at;
}