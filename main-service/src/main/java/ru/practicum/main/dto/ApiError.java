package ru.practicum.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class ApiError {
    private HttpStatus status;
    private String reason;
    private String message;
    private String stackTrace;
}
