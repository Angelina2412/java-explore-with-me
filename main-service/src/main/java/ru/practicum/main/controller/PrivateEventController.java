package ru.practicum.main.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.*;
import ru.practicum.main.service.PrivateEventService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final PrivateEventService privateEventService;

    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(@PathVariable Long userId,
                                                    @RequestBody @Valid NewEventDto newEventDto) {
        return new ResponseEntity<>(privateEventService.createEvent(userId, newEventDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return privateEventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEventById(@PathVariable Long userId,
                                         @PathVariable Long eventId) {
        return privateEventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest updateRequest) {
        return privateEventService.updateEvent(userId, eventId, updateRequest);
    }

    @PatchMapping("/{eventId}/cancel")
    public EventFullDto cancelEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId) {
        return privateEventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventParticipants(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        return privateEventService.getEventParticipants(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestStatus(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody @Valid EventRequestStatusUpdateRequest request) {
        return privateEventService.changeRequestStatus(userId, eventId, request);
    }
}

