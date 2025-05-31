package com.capstone.backend.dtos;

import com.capstone.backend.enums.CategoryStatus;

public record CategoryDto(
        String categoryId,
        String title,
        String description,
        String coverImage,
        CategoryStatus status

) {
}
