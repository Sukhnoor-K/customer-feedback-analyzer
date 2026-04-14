package com.sukhnoor.feedbackanalyzer.feedback;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

  private final FeedbackRepository repo;

  public FeedbackController(FeedbackRepository repo) {
    this.repo = repo;
  }

  public record CreateFeedbackRequest(@NotBlank String text) {}
  public record CreateFeedbackResponse(Long id) {}
  public record FeedbackItem(Long id, String text, java.time.Instant createdAt) {}

  @PostMapping
  public CreateFeedbackResponse create(@Valid @RequestBody CreateFeedbackRequest req) {
    Feedback saved = repo.save(new Feedback(req.text()));
    return new CreateFeedbackResponse(saved.getId());
  }

  @GetMapping
  public List<FeedbackItem> list(@RequestParam(defaultValue = "20") int limit) {
    var page = repo.findAll(
        PageRequest.of(0, Math.min(limit, 100), Sort.by(Sort.Direction.DESC, "id"))
    );
    return page.stream()
        .map(f -> new FeedbackItem(f.getId(), f.getText(), f.getCreatedAt()))
        .toList();
  }

  @GetMapping("/{id}")
  public FeedbackItem get(@PathVariable Long id) {
    Feedback f = repo.findById(id).orElseThrow();
    return new FeedbackItem(f.getId(), f.getText(), f.getCreatedAt());
  }
}