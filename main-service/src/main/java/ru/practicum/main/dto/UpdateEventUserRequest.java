package ru.practicum.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.enums.UserStateAction;
import ru.practicum.main.model.Location;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {

    @Size(min = 3,max = 120, message = "Заголовок не может быть меньше 3 символов и больше 120")
    private String title;

    @Size(min = 20, max = 2000, message = "Аннотация не может быть меньше 20 символов и больше 2000")
    private String annotation;

    @Size(min = 20, max = 7000, message = "Описание не может быть меньше 20 символов и больше 7000")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private Location location;
    private UserStateAction stateAction;
}