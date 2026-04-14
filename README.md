# Customer Feedback Analyzer (Spring Boot + Ollama)

A backend application that stores customer feedback and uses a local LLM to generate structured insights including sentiment, themes, and summaries. Designed prompts to enforce consistent JSON outputs from the LLM, enabling reliable parsing and storage. Can be used for further data analytics purposes.

## Tech Stack

* Java 21 + Spring Boot
* PostgreSQL (Docker)
* Ollama (local LLM)
* Mistral 7B model
* REST API + Swagger UI

## Features

* Submit customer feedback
* Store feedback in PostgreSQL
* Analyze feedback using a local LLM
* Extract:

  * sentiment
  * themes
  * feature requests
  * bug reports
  * summary
* Cache analysis results

## Setup

### 1. Start database

```bash
docker compose up -d
```

### 2. Start Ollama

```bash
ollama serve
```

Ensure model exists:

```bash
ollama pull mistral
```

### 3. Run backend

```bash
cd backend
./mvnw spring-boot:run
```

## API

### Create feedback

```bash
curl -X POST http://localhost:8080/feedback \
  -H "Content-Type: application/json" \
  -d '{"text":"Your feedback here"}'
```

### Get feedback

```bash
curl http://localhost:8080/feedback
```

### Analyze feedback

```bash
curl -X POST http://localhost:8080/feedback/1/analyze
```

## Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

## Notes

* Uses local LLM via Ollama (no API keys required)
