package ru.practicum.main.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.main.dto.*;
import ru.practicum.main.enums.EventState;
import ru.practicum.main.model.Category;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.Location;
import ru.practicum.main.model.User;

import java.time.LocalDateTime;

@Component
public class EventMapper {

    public EventShortDto toShortDto(Event event, long confirmedRequests, long views) {
        EventShortDto dto = new EventShortDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setAnnotation(event.getAnnotation());
        dto.setEventDate(event.getEventDate());
        dto.setPaid(event.getPaid());
        dto.setViews(views);
        dto.setConfirmedRequests(confirmedRequests);
        dto.setCategory(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()));
        dto.setInitiator(new UserDto(event.getInitiator().getId(), event.getInitiator().getName()));
        return dto;
    }

    public EventFullDto toFullDto(Event event, long confirmedRequests, long views) {
        EventFullDto dto = new EventFullDto();

        BeanUtils.copyProperties(toShortDto(event, confirmedRequests, views), dto);

        dto.setDescription(event.getDescription());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setPublishedOn(event.getPublishedOn());
        dto.setState(event.getState());
        dto.setRequestModeration(event.getRequestModeration());

        return dto;
    }

    public EventFullDto toFullDto(Event event) {
        EventFullDto dto = new EventFullDto();

        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()));
        dto.setPaid(event.getPaid());
        dto.setEventDate(event.getEventDate());
        dto.setInitiator(new UserDto(event.getInitiator().getId(), event.getInitiator().getName()));

        dto.setDescription(event.getDescription());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setState(event.getState());
        dto.setCreatedOn(event.getCreatedOn());
        dto.setPublishedOn(event.getPublishedOn());
        dto.setRequestModeration(event.getRequestModeration());

        if (event.getLocation() != null) {
            dto.setLocation(new LocationDto(event.getLocation().getLat(), event.getLocation().getLon()));
        }

        return dto;
    }

    public Event toEvent(NewEventDto dto, Category category, User initiator) {
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setAnnotation(dto.getAnnotation());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());

        if (dto.getLocation() != null) {
            Location location = new Location();
            location.setLat(dto.getLocation().getLat());
            location.setLon(dto.getLocation().getLon());
            event.setLocation(location);
        }

        event.setPaid(dto.getPaid());
        event.setParticipantLimit(dto.getParticipantLimit());
        event.setRequestModeration(dto.getRequestModeration());
        event.setCategory(category);
        event.setInitiator(initiator);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setViews(0L);

        return event;
    }

    public AdminEventDto toAdminDto(Event event, long confirmedRequests, long views) {
        AdminEventDto dto = new AdminEventDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()));
        dto.setPaid(event.getPaid());
        dto.setEventDate(event.getEventDate());
        dto.setInitiator(new UserDto(event.getInitiator().getId(), event.getInitiator().getName()));
        dto.setDescription(event.getDescription());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setState(event.getState());
        dto.setCreatedOn(event.getCreatedOn());
        dto.setPublishedOn(event.getPublishedOn());
        dto.setRequestModeration(event.getRequestModeration());
        if (event.getLocation() != null) {
            dto.setLocation(new LocationDto(event.getLocation().getLat(), event.getLocation().getLon()));
        }
        dto.setViews(views);
        dto.setConfirmedRequests(confirmedRequests);
        return dto;
    }

    public EventDetailsDto toDetailsDto(Event event, long confirmedRequests, long views) {
        return new EventDetailsDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                new CategoryDto(event.getCategory().getId(), event.getCategory().getName()),
                event.getPaid(),
                event.getEventDate(),
                new UserDto(event.getInitiator().getId(), event.getInitiator().getName()),
                event.getDescription(),
                event.getParticipantLimit(),
                event.getState(),
                event.getCreatedOn(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getLocation() != null
                        ? new LocationDto(event.getLocation().getLat(), event.getLocation().getLon())
                        : null,
                views,
                confirmedRequests
        );
    }
}

