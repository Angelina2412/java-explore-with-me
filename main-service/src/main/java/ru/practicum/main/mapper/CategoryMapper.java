package ru.practicum.main.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.NewCategoryDto;
import ru.practicum.main.model.Category;

@Component
public class CategoryMapper {
    public CategoryDto toDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
    public Category toEntity(NewCategoryDto newCategoryDto) {
        return new Category(
                null,
                newCategoryDto.getName(),
                newCategoryDto.getDescription()
        );
    }

}

