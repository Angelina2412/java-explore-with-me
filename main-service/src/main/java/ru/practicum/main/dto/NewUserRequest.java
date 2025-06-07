package ru.practicum.main.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank(message = "Имя пользователя не может быть пустым или состоять только из пробелов.")
    @Size(min = 2, max = 250, message = "Имя пользователя не может превышать 250 символов")
    private String name;

    @NotBlank(message = "Email не может быть пустым или состоять только из пробелов.")
    @Size(min = 6, max = 254, message = "Email должен быть от 6 до 254 символов")
    @Email(message = "Некорректный формат email")
    private String email;
}
