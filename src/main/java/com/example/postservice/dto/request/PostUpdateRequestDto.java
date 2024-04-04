package com.example.postservice.dto.request;

public record PostUpdateRequestDto(Long id, String subject, String title, String thumbnail, Boolean accessibility, Long categoryId, Long subCategoryId) {
}
