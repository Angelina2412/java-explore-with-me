package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.*;
import ru.practicum.main.enums.EventState;
import ru.practicum.main.enums.RequestStatus;
import ru.practicum.main.exceptions.ConflictException;
import ru.practicum.main.exceptions.ForbiddenException;
import ru.practicum.main.exceptions.NotFoundException;
import ru.practicum.main.mapper.EventMapper;
import ru.practicum.main.mapper.ParticipationRequestMapper;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.ParticipationRequest;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.ParticipationRequestRepository;
import ru.practicum.main.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final EventMapper eventMapper;
    private final ParticipationRequestMapper requestMapper;

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));

        if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("Мероприятие должно быть как минимум через два часа");
        }

        if (dto.getPaid() == null) {
            dto.setPaid(false);
        }
        if (dto.getParticipantLimit() == null) {
            dto.setParticipantLimit(0);
        }
        if (dto.getRequestModeration() == null) {
            dto.setRequestModeration(true);
        }

        Event event = eventMapper.toEvent(dto, category, user);
        Event saved = eventRepository.save(event);
        return eventMapper.toFullDto(saved);
    }


    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        List<Event> events = eventRepository.findByInitiatorId(userId, PageRequest.of(from / size, size));
        return events.stream()
                .map(e -> eventMapper.toShortDto(e, 0, e.getViews()))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        return eventMapper.toFullDto(event, 0, event.getViews());
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateRequest) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Событие не может быть обновлено, если оно уже опубликовано.");
        }

        if (updateRequest.getEventDate() != null &&
                updateRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("Мероприятие не может быть раньше, чем через 2 часа");
        }

        if (updateRequest.getTitle() != null) event.setTitle(updateRequest.getTitle());
        if (updateRequest.getAnnotation() != null) event.setAnnotation(updateRequest.getAnnotation());
        if (updateRequest.getDescription() != null) event.setDescription(updateRequest.getDescription());
        if (updateRequest.getEventDate() != null) event.setEventDate(updateRequest.getEventDate());
        if (updateRequest.getPaid() != null) event.setPaid(updateRequest.getPaid());
        if (updateRequest.getParticipantLimit() != null) {
            if (updateRequest.getParticipantLimit() < 0) {
                throw new IllegalArgumentException("participantLimit не может быть отрицательным");
            }
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getRequestModeration() != null)
            event.setRequestModeration(updateRequest.getRequestModeration());
        if (updateRequest.getLocation() != null) event.setLocation(updateRequest.getLocation());

        if (updateRequest.getStateAction() != null) {
            switch (updateRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }

        Event updatedEvent = eventRepository.save(event);
        return eventMapper.toFullDto(updatedEvent);
    }

    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        if (event.getState() == EventState.PUBLISHED) {
            throw new ForbiddenException("Нельзя отменить опубликованное мероприятие");
        }

        event.setState(EventState.CANCELED);
        return eventMapper.toFullDto(eventRepository.save(event), 0, event.getViews());
    }

    public List<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId) {
        eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        List<ParticipationRequest> requests = participationRequestRepository.findByEventId(eventId);

        return requests.stream()
                .map(request -> {
                    ParticipationRequestDto dto = new ParticipationRequestDto();
                    dto.setId(request.getId());
                    dto.setEvent(request.getEvent().getId());
                    dto.setRequester(request.getRequester().getId());
                    dto.setStatus(request.getStatus().name());
                    dto.setCreated(request.getCreated());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult changeRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));

        List<ParticipationRequest> requests = participationRequestRepository.findAllById(updateRequest.getRequestIds());

        List<ParticipationRequest> confirmedRequests = new ArrayList<>();
        List<ParticipationRequest> rejectedRequests = new ArrayList<>();

        int alreadyConfirmed = participationRequestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        int participantLimit = event.getParticipantLimit() != null ? event.getParticipantLimit() : 0;
        int confirmedCount = alreadyConfirmed;

        for (ParticipationRequest request : requests) {
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("Изменять можно только запросы со статусом PENDING.");
            }

            if (RequestStatus.CONFIRMED.name().equals(updateRequest.getStatus())) {
                if (participantLimit != 0 && confirmedCount >= participantLimit) {
                    request.setStatus(RequestStatus.REJECTED);
                    rejectedRequests.add(request);
                } else {
                    request.setStatus(RequestStatus.CONFIRMED);
                    confirmedRequests.add(request);
                    confirmedCount++;
                }
            } else if (RequestStatus.REJECTED.name().equals(updateRequest.getStatus())) {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(request);
            }

            participationRequestRepository.save(request);
        }

        boolean allRejectedDueToLimit = RequestStatus.CONFIRMED.name().equals(updateRequest.getStatus())
                && confirmedRequests.isEmpty()
                && !rejectedRequests.isEmpty();

        if (allRejectedDueToLimit) {
            throw new ConflictException("Невозможно подтвердить запросы: достигнут лимит участников.");
        }

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        result.setEventId(eventId);
        result.setConfirmedRequests(
                confirmedRequests.stream()
                        .map(requestMapper::toDto)
                        .collect(Collectors.toList())
        );
        result.setRejectedRequests(
                rejectedRequests.stream()
                        .map(requestMapper::toDto)
                        .collect(Collectors.toList())
        );

        return result;
    }
}

