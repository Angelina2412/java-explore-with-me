package ru.practicum.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventShortDto {
    private Long id;
    private String title;
    private String annotation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Boolean paid;
    private Long views;
    private CategoryDto category;
    private UserDto initiator;
    private long confirmedRequests;

}
