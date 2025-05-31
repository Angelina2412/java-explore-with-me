package ru.practicum.main.service;

import ru.practicum.main.dto.AdminEventDto;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventService {
    List<AdminEventDto> searchEvents(List<Long> users, List<String> states, List<Long> categories,
                                     String rangeStart, String rangeEnd, int from, int size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest request);


}
