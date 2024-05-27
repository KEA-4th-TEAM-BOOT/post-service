package com.example.postservice.dto.request;

public record PostUpdateRequestDto(Long id, String subject, String title, String content, String thumbnail, Boolean accessibility, Long categoryId, Long subCategoryId) {
}
