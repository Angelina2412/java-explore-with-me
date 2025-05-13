package ru.practicum.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewEventDto {
    @Size(min = 3,max = 120, message = "Заголовок не может быть меньше 3 символов и больше 120")
    private String title;

    @NotBlank(message = "Аннотация не может быть пустая или состоять только из пробелов.")
    @Size(min = 20, max = 2000, message = "Аннотация не может быть меньше 20 символов и больше 2000")
    private String annotation;

    @NotBlank(message = "Описание не может быть пустым или состоять только из пробелов.")
    @Size(min = 20, max = 7000, message = "Описание не может быть меньше 20 символов и больше 7000")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero(message = "participantLimit не может быть отрицательным")
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    private Long category;
}

