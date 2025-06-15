package ru.practicum.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.main.enums.EventState;

import java.time.LocalDateTime;

@Data
public class AdminEventDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private Boolean paid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserDto initiator;
    private String description;
    private Integer participantLimit;
    private EventState state;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private LocationDto location;
    private Long views;
    private Long confirmedRequests;
}
