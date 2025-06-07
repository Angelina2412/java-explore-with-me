package ru.practicum.main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class NewCompilationDto {
    @NotBlank(message = "Заголовок не может быть пустым или состоять только из пробелов.")
    @Size(max = 50, message = "Заголовок не может превышать 50 символов")
    private String title;
    private List<Long> events;
    private Boolean pinned = false;
}
