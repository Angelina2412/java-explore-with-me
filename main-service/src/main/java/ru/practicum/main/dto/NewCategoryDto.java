package ru.practicum.main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewCategoryDto {
    @NotBlank(message = "Название категории не может быть пустым или состоять только из пробелов.")
    @Size(max = 50, message = "Имя категории не может превышать 50 символов")
    private String name;

    private String description;
}

