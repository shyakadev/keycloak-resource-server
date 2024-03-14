package com.example.keycloakresourceserver;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ArticleService {
    private ArticleRepository articleRepository;

    public ArticleService(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getAll() {
        return (List<Article>) articleRepository.findAll();
    }

    public List<ArticleDto> getAllMinified() {
        return getAll().stream()
              .map(ArticleDto::minify)
              .collect(Collectors.toList());
    }

    public Article createArticle(ArticleDto article) {
        return articleRepository.save(new Article(article.getTitle(), article.getContent()));
    }
}
