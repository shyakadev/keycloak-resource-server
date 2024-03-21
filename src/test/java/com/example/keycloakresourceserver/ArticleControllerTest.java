package com.example.keycloakresourceserver;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static com.example.keycloakresourceserver.JsonUtils.*;

@WebMvcTest(ArticleController.class)
@Import(SecurityConfiguration.class)
public class ArticleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ArticleService articleService;

    List<Article> articles = new ArrayList<>();

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setup() {
        articles = List.of(
                new Article("Article Title one", "Article one Description"),
                new Article("Article Title two", "Article two Description"));
    }

    @SuppressWarnings("null")
    @Test
    @WithMockUser(username = "user", roles = { "USER", "ADMIN" })
    void shouldFindAllArticles() throws Exception {
        when(articleService.getAll()).thenReturn(articles);

        mockMvc
                .perform(get("/v1/articles/all").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Article Title one")))
                .andExpect(jsonPath("$[0].content", is("Article one Description")));
    }

    @SuppressWarnings("null")
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void shouldCreateNewArticle_whenValidInput() throws Exception {
        ArticleDto articleDto = new ArticleDto("Article Title three", "Article three Description");
        Article createdArticle = new Article("Article Title three", "Article three Description");
        when(articleService.createArticle(articleDto)).thenReturn(createdArticle);

        mockMvc.perform(post("/v1/articles/create").content(asJsonString(articleDto))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(articleDto.getTitle())))
                .andExpect(jsonPath("$.content", is(articleDto.getContent())));

        verify(articleService).createArticle(articleDto);
    }

}
