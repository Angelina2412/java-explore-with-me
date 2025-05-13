package ru.practicum.main.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.dto.UpdateCompilationRequest;
import ru.practicum.main.service.AdminCompilationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {

    private final AdminCompilationService adminCompilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> saveCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        CompilationDto compilationDto = adminCompilationService.saveCompilation(newCompilationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(compilationDto);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(
            @PathVariable Long compId,
            @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        CompilationDto compilationDto = adminCompilationService.updateCompilation(compId, updateCompilationRequest);
        return ResponseEntity.ok(compilationDto);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable Long compId) {
        adminCompilationService.deleteCompilation(compId);
        return ResponseEntity.noContent().build();
    }
}


