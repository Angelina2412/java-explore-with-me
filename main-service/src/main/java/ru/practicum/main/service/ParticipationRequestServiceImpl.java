package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.ParticipationRequestDto;
import ru.practicum.main.enums.EventState;
import ru.practicum.main.enums.RequestStatus;
import ru.practicum.main.exceptions.ConflictException;
import ru.practicum.main.exceptions.NotFoundException;
import ru.practicum.main.mapper.ParticipationRequestMapper;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.ParticipationRequest;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.ParticipationRequestRepository;
import ru.practicum.main.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestMapper requestMapper;

    @Override
    public List<ParticipationRequestDto> getRequestsByUser(Long userId) {
        checkUserExists(userId);
        return requestRepository.findByRequesterId(userId).stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Request already exists");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Initiator can't request to own event");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Cannot request participation in unpublished event");
        }

        if (event.getParticipantLimit() != 0 &&
                requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED) >= event.getParticipantLimit()) {
            throw new ConflictException("Participant limit reached");
        }

        ParticipationRequest request = ParticipationRequest.builder()
                .event(event)
                .requester(user)
                .created(LocalDateTime.now())
                .status((!event.isRequestModeration() || event.getParticipantLimit() == 0)
                        ? RequestStatus.CONFIRMED
                        : RequestStatus.PENDING)
                .build();

        return requestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));

        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toDto(requestRepository.save(request));
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
    }
}

