package ru.practicum.main.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.dto.HitRequest;
import ru.practicum.dto.StatsResponse;
import ru.practicum.main.dto.EventDetailsDto;
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
public class EventServiceImpl implements EventService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final ParticipationRequestRepository requestRepository;
    private final StatsClient statsClient;

    @Override
    public List<EventShortDto> searchEvents(String text, List<Long> categories, Boolean paid,
                                            String rangeStart, String rangeEnd,
                                            boolean onlyAvailable, String sort,
                                            int from, int size,
                                            HttpServletRequest request) {
        statsClient.sendHit(new HitRequest(
                "main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        ));

        if (text != null && text.isBlank()) {
            text = null;
        }

        if (categories == null || categories.isEmpty()) {
            categories = null;
        }

        LocalDateTime start = (rangeStart != null && !rangeStart.isBlank())
                ? LocalDateTime.parse(rangeStart, FORMATTER)
                : null;

        LocalDateTime end = (rangeEnd != null && !rangeEnd.isBlank())
                ? LocalDateTime.parse(rangeEnd, FORMATTER)
                : null;

        if (start != null && end != null && start.isAfter(end)) {
            throw new IllegalArgumentException("rangeStart не может быть после rangeEnd");
        }

        Pageable pageable = PageRequest.of(from / size, size);

        int categoriesSize = (categories == null) ? 0 : categories.size();
        List<Event> events = eventRepository.findPublishedEventsWithFilters(
                text,
                (categoriesSize == 0) ? List.of(-1L) : categories,
                categoriesSize,
                paid,
                start,
                end,
                pageable
        );

        return events.stream()
                .filter(e -> !onlyAvailable ||
                        e.getParticipantLimit() > requestRepository.countByEventIdAndStatus(e.getId(), RequestStatus.CONFIRMED))
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
        System.out.println("Ответ от stats-сервиса: " + stats);

        long views = stats.isEmpty() ? 0 : stats.get(0).getHits();

        long confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);

        return eventMapper.toDetailsDto(event, confirmedRequests, views);
    }
}
