package ru.practicum.main.service;

import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.dto.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto saveCompilation(NewCompilationDto newCompilationDto);
    void deleteCompilation(Long compId);
    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest);
}

