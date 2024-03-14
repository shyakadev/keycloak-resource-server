package com.example.keycloakresourceserver;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleDto {
    private String title;
    private String content;
    private Instant createdAt;

    private final static int PREVIEW_LENGTH = 20;

    public static ArticleDto minify(final Article article) {
        final int lastIndex = Math.min(article.getContent().length(), PREVIEW_LENGTH);
        final String minifiedContent = article.getContent().substring(0, lastIndex) + "...";
        return new ArticleDto(article.getTitle(), minifiedContent, article.getCreatedAt());
    }
}
