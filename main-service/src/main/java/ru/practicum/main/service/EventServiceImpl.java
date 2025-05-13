package ru.practicum.main.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.dto.HitRequest;
import ru.practicum.dto.StatsResponse;
import ru.practicum.main.dto.EventDetailsDto;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.enums.EventState;
import ru.practicum.main.enums.RequestStatus;
import ru.practicum.main.exceptions.NotFoundException;
import ru.practicum.main.mapper.EventMapper;
import ru.practicum.main.model.Event;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.ParticipationRequestRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final ParticipationRequestRepository requestRepository;
    private final StatsClient statsClient;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<EventShortDto> searchEvents(String text, List<Long> categories, Boolean paid,
                                            String  rangeStart, String rangeEnd,
                                            boolean onlyAvailable, String sort,
                                            int from, int size,
                                            HttpServletRequest request) {
        statsClient.sendHit(new HitRequest(
                "main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        ));

        LocalDateTime start = (rangeStart != null) ? LocalDateTime.parse(rangeStart, FORMATTER) : null;
        LocalDateTime end = (rangeEnd != null) ? LocalDateTime.parse(rangeEnd, FORMATTER) : null;

        if (start != null && end != null && start.isAfter(end)) {
            throw new IllegalArgumentException("rangeStart не может быть после rangeEnd");
        }

        Pageable pageable = PageRequest.of(from / size, size);

        List<Event> events = eventRepository.findPublishedEventsWithFilters(
                text, categories, paid, start, end, pageable
        );

        return events.stream()
                .filter(e -> !onlyAvailable || e.getParticipantLimit() > requestRepository.countByEventIdAndStatus(e.getId(), RequestStatus.CONFIRMED))
                .map(e -> {
                    long confirmedRequests = requestRepository.countByEventIdAndStatus(e.getId(), RequestStatus.CONFIRMED);
                    long views = e.getViews();
                    return eventMapper.toShortDto(e, confirmedRequests, views);
                })
                .collect(Collectors.toList());
    }

    @Override
    public EventDetailsDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или не опубликовано"));

        statsClient.sendHit(new HitRequest(
                "main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        ));

        List<StatsResponse> stats = statsClient.getStats(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.now(),
                List.of(request.getRequestURI()),
                true
        );

        long views = stats.isEmpty() ? 0 : stats.get(0).getHits();

        long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);

        return eventMapper.toDetailsDto(event, confirmedRequests, views);
    }

//    @Override
//    public EventDetailsDto getEventById(Long eventId, HttpServletRequest request) {
//        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
//                .orElseThrow(() -> new NotFoundException("Событие не найдено или не опубликовано"));
//
//        statsClient.sendHit(new HitRequest(
//                "main-service",
//                request.getRequestURI(),
//                request.getRemoteAddr(),
//                LocalDateTime.now()
//        ));
//
//        event.setViews(event.getViews() + 1);
//        eventRepository.save(event);
//
//        long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
//        long views = event.getViews();
//
//        return eventMapper.toDetailsDto(event, confirmedRequests, views);
//    }

}
