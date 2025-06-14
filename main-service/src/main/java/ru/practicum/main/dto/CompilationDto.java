package ru.practicum.main.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CompilationDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private List<EventShortDto> events;

    public CompilationDto(Long id, String title, List<EventShortDto> events) {
        this.id = id;
        this.title = title;
        this.events = events;
    }
}
