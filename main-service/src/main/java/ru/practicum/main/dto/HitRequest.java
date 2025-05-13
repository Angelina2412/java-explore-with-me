package ru.practicum.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HitRequest {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
