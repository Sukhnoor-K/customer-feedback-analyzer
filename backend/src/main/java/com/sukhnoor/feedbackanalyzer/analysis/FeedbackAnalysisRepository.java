package com.sukhnoor.feedbackanalyzer.analysis;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedbackAnalysisRepository extends JpaRepository<FeedbackAnalysis, Long> {
  Optional<FeedbackAnalysis> findByFeedbackId(Long feedbackId);
}