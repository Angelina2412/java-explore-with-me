package ru.practicum.main.service;

import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAll(int from, int size);
    CategoryDto getById(Long catId);
    CategoryDto addCategory(NewCategoryDto newCategoryDto);
    void deleteCategory(Long catId);
    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);
}
