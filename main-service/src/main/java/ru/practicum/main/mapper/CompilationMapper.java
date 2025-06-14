package ru.practicum.main.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.dto.UpdateCompilationRequest;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.model.Event;
import ru.practicum.main.repository.EventRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    public Compilation toEntity(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(Boolean.TRUE.equals(newCompilationDto.getPinned()));

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
            compilation.setEvents(events);
        } else {
            compilation.setEvents(Collections.emptyList());
        }

        return compilation;
    }

    public CompilationDto toDto(Compilation compilation, Map<Long, Integer> confirmedRequests, Map<Long, Long> views) {
        CompilationDto dto = new CompilationDto();
        dto.setId(compilation.getId());
        dto.setTitle(compilation.getTitle());
        dto.setPinned(compilation.getPinned());

        dto.setEvents(compilation.getEvents().stream()
                .map(event -> eventMapper.toShortDto(
                        event,
                        confirmedRequests.getOrDefault(event.getId(), 0),
                        views.getOrDefault(event.getId(), 0L)
                ))
                .collect(Collectors.toList()));

        return dto;
    }

    public CompilationDto toDto(Compilation compilation) {
        return toDto(compilation, Collections.emptyMap(), Collections.emptyMap());
    }

    public void updateEntityFromDto(UpdateCompilationRequest updateCompilationRequest, Compilation compilation) {
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
    }
}

