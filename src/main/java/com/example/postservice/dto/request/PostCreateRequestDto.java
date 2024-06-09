package com.example.postservice.dto.request;

import com.example.postservice.model.enums.PostAccessibility;

import java.util.List;

public record PostCreateRequestDto(String userLink, String nickname, Long personalPostId, String postVoiceFileUrl, Long categoryId, Long subCategoryId, String subject, String title, String content, String thumbnail, String thumbnailImageUrl, List<String> tags, PostAccessibility accessibility) {
}
