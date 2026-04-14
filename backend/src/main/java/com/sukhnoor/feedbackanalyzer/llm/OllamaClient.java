package com.sukhnoor.feedbackanalyzer.llm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OllamaClient {

  private final RestClient restClient;
  private final String model;

  public OllamaClient(
      @Value("${ollama.base-url}") String baseUrl,
      @Value("${ollama.model}") String model
  ) {
    this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    this.model = model;
  }

  public record GenerateRequest(String model, String prompt, boolean stream) {}
  public record GenerateResponse(String response) {}

  public String generate(String prompt) {
    GenerateResponse res = restClient.post()
        .uri("/api/generate")
        .body(new GenerateRequest(model, prompt, false))
        .retrieve()
        .body(GenerateResponse.class);

    if (res == null || res.response() == null) {
      throw new RuntimeException("Empty response from Ollama");
    }
    return res.response().trim();
  }

  public String model() {
    return model;
  }
}