package com.sukhnoor.feedbackanalyzer.analysis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sukhnoor.feedbackanalyzer.feedback.Feedback;
import com.sukhnoor.feedbackanalyzer.feedback.FeedbackRepository;
import com.sukhnoor.feedbackanalyzer.llm.OllamaClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/feedback")
public class FeedbackAnalysisController {

  private final FeedbackRepository feedbackRepo;
  private final FeedbackAnalysisRepository analysisRepo;
  private final OllamaClient ollama;
  private final ObjectMapper objectMapper;

  public FeedbackAnalysisController(
      FeedbackRepository feedbackRepo,
      FeedbackAnalysisRepository analysisRepo,
      OllamaClient ollama,
      ObjectMapper objectMapper
  ) {
    this.feedbackRepo = feedbackRepo;
    this.analysisRepo = analysisRepo;
    this.ollama = ollama;
    this.objectMapper = objectMapper;
  }

  public record AnalysisResponse(
      Long analysisId,
      Long feedbackId,
      String model,
      String sentiment,
      String themes,
      String featureRequests,
      String bugReports,
      String summary
  ) {}

  @PostMapping("/{id}/analyze")
  public AnalysisResponse analyze(@PathVariable Long id) {

    var existing = analysisRepo.findByFeedbackId(id);
    if (existing.isPresent()) {
      return toResponse(existing.get());
    }

    Feedback feedback = feedbackRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found"));

    String prompt = """
Return ONLY valid JSON. No markdown.
Schema:
{
  "sentiment": "positive" | "neutral" | "negative",
  "themes": ["..."],
  "feature_requests": [{"title":"...","evidence":"..."}],
  "bug_reports": [{"title":"...","evidence":"..."}],
  "summary": "..."
}

Customer feedback:
\"\"\"%s\"\"\"
""".formatted(feedback.getText());

    String raw = ollama.generate(prompt);

    JsonNode root;
    try {
      root = objectMapper.readTree(raw);
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_GATEWAY,
          "LLM returned invalid JSON: " + raw
      );
    }

    String sentiment = root.path("sentiment").asText("unknown");
    JsonNode themes = root.path("themes");
    JsonNode featureRequests = root.path("feature_requests");
    JsonNode bugReports = root.path("bug_reports");
    String summary = root.path("summary").asText("");

    FeedbackAnalysis saved = analysisRepo.save(new FeedbackAnalysis(
        feedback,
        sentiment,
        themes.toString(),
        featureRequests.toString(),
        bugReports.toString(),
        summary,
        ollama.model()
    ));

    return toResponse(saved);
  }

  private AnalysisResponse toResponse(FeedbackAnalysis a) {
    return new AnalysisResponse(
        a.getId(),
        a.getFeedback().getId(),
        a.getModel(),
        a.getSentiment(),
        a.getThemesJson(),
        a.getFeatureRequestsJson(),
        a.getBugReportsJson(),
        a.getSummary()
    );
  }
}