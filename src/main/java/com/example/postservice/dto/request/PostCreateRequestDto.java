package com.example.postservice.dto.request;

import java.util.List;

public record PostCreateRequestDto(String subject, String title, String content, List<String> tags, String thumbnail, Boolean accessibility, Long userId, Long categoryId, Long subCategoryId) {
}
