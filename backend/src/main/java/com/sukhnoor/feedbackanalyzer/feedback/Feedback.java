package com.sukhnoor.feedbackanalyzer.feedback;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "feedback")
public class Feedback {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, columnDefinition = "text")
  private String text;

  @Column(nullable = false)
  private Instant createdAt = Instant.now();

  protected Feedback() {}

  public Feedback(String text) {
    this.text = text;
  }

  public Long getId() { return id; }
  public String getText() { return text; }
  public Instant getCreatedAt() { return createdAt; }
}