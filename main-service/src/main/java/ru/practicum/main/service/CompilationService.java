package ru.practicum.main.service;

import org.springframework.data.crossstore.ChangeSetPersister;
import ru.practicum.main.dto.CompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getById(Long id) throws ChangeSetPersister.NotFoundException;
}
