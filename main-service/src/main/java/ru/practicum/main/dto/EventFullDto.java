package ru.practicum.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.main.enums.EventState;

import java.time.LocalDateTime;

@Data
public class EventFullDto {
    private Long id;

    @Size(min = 3,max = 120, message = "Заголовок не может быть меньше 3 символов и больше 120")
    private String title;

    @Size(min = 20, max = 2000, message = "Аннотация не может быть меньше 20 символов и больше 2000")
    private String annotation;
    private CategoryDto category;
    private Boolean paid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserDto initiator;

    @Size(min = 20, max = 7000, message = "Описание не может быть меньше 20 символов и больше 7000")
    private String description;
    private Integer participantLimit;
    private EventState state;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private LocationDto location;
}
