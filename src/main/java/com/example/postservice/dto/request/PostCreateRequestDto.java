package com.example.postservice.dto.request;

public record PostCreateRequestDto(String subject, String title, String thumbnail, Boolean accessibility, Long userId, Long categoryId, Long subCategoryId) {
}
