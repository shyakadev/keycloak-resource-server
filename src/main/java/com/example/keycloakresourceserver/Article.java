package com.example.keycloakresourceserver;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Article {

  public Article() {
      this.createdAt = Instant.now();
  }

  public Article(String title, String content) {
      this.title = title;
      this.content = content;
      this.createdAt = Instant.now();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long Id;

  @Column(name = "title", unique = true)
  private String title;

  @Column(name = "content")
  private String content;

  @Column(name = "createdAt")
  private Instant createdAt;
}
