package com.example.postservice.dto.request;

import com.example.postservice.model.enums.PostAccessibility;

public record PostUpdateRequestDto(Long id, String subject, String title, String content, String thumbnail, PostAccessibility accessibility, Long categoryId, Long subCategoryId) {
}
