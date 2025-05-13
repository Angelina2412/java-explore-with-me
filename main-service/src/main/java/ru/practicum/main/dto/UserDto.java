package ru.practicum.main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "Имя пользователя не может быть пустым или состоять только из пробелов.")
    @Size(min = 2, max = 250, message = "Имя пользователя не может превышать 250 символов")
    private String name;

    @NotBlank(message = "Email не может быть пустым или состоять только из пробелов.")
    private String email;

    public UserDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
