
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
public class UserResponse {
    Integer id;
    String name;
    String email;
    LocalDate created_at;
    LocalDate updated_at;
}
