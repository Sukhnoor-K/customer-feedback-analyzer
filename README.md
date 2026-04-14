Customer Feedback Analyzer (Spring Boot + Ollama)

A backend application that stores customer feedback and uses a local LLM to generate structured insights including sentiment, themes, and summaries.

Tech Stack
Java 21 + Spring Boot
PostgreSQL (Docker)
Ollama (local LLM)
Mistral 7B model
REST API + Swagger UI
Features
Submit customer feedback
Store feedback in PostgreSQL
Analyze feedback using a local LLM
Extract:
sentiment
themes
feature requests
bug reports
summary
Cache analysis results
Setup
1. Start database
docker compose up -d
2. Start Ollama
ollama serve

Ensure model exists:

ollama pull mistral
3. Run backend
cd backend
./mvnw spring-boot:run
API
Create feedback
curl -X POST http://localhost:8080/feedback \
  -H "Content-Type: application/json" \
  -d '{"text":"Your feedback here"}'
Get feedback
curl http://localhost:8080/feedback
Analyze feedback
curl -X POST http://localhost:8080/feedback/1/analyze
Swagger UI
http://localhost:8080/swagger-ui/index.html
Notes
Uses local LLM via Ollama (no API keys required)