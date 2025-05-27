package ru.practicum.main.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.main.dto.EventDetailsDto;
import ru.practicum.main.dto.EventShortDto;

import java.util.List;

public interface EventService {
    List<EventShortDto> searchEvents(String text, List<Long> categories, Boolean paid,
                                     String rangeStart, String rangeEnd,
                                     boolean onlyAvailable, String sort, int from, int size,
                                     HttpServletRequest request);

    EventDetailsDto getEventById(Long eventId, HttpServletRequest request);
}
