package com.sukhnoor.feedbackanalyzer.analysis;

import com.sukhnoor.feedbackanalyzer.feedback.Feedback;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "feedback_analysis")
public class FeedbackAnalysis {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional = false)
  @JoinColumn(name = "feedback_id", unique = true, nullable = false)
  private Feedback feedback;

  @Column(nullable = false)
  private String sentiment; // "positive" | "neutral" | "negative"

  @Column(nullable = false, columnDefinition = "text")
  private String themesJson;

  @Column(nullable = false, columnDefinition = "text")
  private String featureRequestsJson;

  @Column(nullable = false, columnDefinition = "text")
  private String bugReportsJson;

  @Column(nullable = false, columnDefinition = "text")
  private String summary;

  @Column(nullable = false)
  private String model;

  @Column(nullable = false)
  private Instant createdAt = Instant.now();

  protected FeedbackAnalysis() {}

  public FeedbackAnalysis(
      Feedback feedback,
      String sentiment,
      String themesJson,
      String featureRequestsJson,
      String bugReportsJson,
      String summary,
      String model
  ) {
    this.feedback = feedback;
    this.sentiment = sentiment;
    this.themesJson = themesJson;
    this.featureRequestsJson = featureRequestsJson;
    this.bugReportsJson = bugReportsJson;
    this.summary = summary;
    this.model = model;
  }

  public Long getId() { return id; }
  public Feedback getFeedback() { return feedback; }
  public String getSentiment() { return sentiment; }
  public String getThemesJson() { return themesJson; }
  public String getFeatureRequestsJson() { return featureRequestsJson; }
  public String getBugReportsJson() { return bugReportsJson; }
  public String getSummary() { return summary; }
  public String getModel() { return model; }
  public Instant getCreatedAt() { return createdAt; }
}