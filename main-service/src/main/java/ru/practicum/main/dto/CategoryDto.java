package ru.practicum.main.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    @Size(max = 50, message = "Имя категории не может превышать 50 символов")
    private String name;
    private String description;

    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
