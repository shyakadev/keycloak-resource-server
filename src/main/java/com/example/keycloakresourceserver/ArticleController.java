package com.example.keycloakresourceserver;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/articles")
public class ArticleController {
    private ArticleService articleService;

    public ArticleController(final ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/all")
    public List<Article> getAll() {
        return articleService.getAll();
    }

    @GetMapping("/minified")
    public List<ArticleDto> getAllMinified() {
        return articleService.getAllMinified();
    }

    @PostMapping("/create")
    public ResponseEntity<Article> createArticle(@RequestBody ArticleDto article) {
        Article created = articleService.createArticle(article);
        return new ResponseEntity<Article>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
     @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Article> getById(@PathVariable @NonNull Long id) {
        Article article = articleService.findById(id);
        return ResponseEntity.ok(article);
    }
}
