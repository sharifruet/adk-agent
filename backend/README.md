# Backend - Life Insurance Sales Agent

Spring Boot REST API backend for the Life Insurance Sales Agent application.

## Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/i2gether/lic/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controllers/     # REST controllers
│   │   │   ├── models/          # Data models
│   │   │   ├── services/        # Business logic
│   │   │   └── tool/            # Custom tools
│   │   └── resources/
│   │       ├── application.yaml # Configuration
│   │       └── prompts/         # AI agent prompts
│   └── test/                    # Test classes
├── pom.xml                      # Maven configuration
├── mvnw                         # Maven wrapper
└── run.sh                       # Run script
```

## Configuration

### API Key Setup

Set your Gemini API key in `src/main/resources/application.yaml`:

```yaml
com:
  i2gether:
    lic:
      agent:
        api-key: YOUR_API_KEY_HERE
```

Or set environment variable:
```bash
export GEMINI_API_KEY=your-api-key
```

## Running the Application

### Using the run script:
```bash
./run.sh
```

### Using Maven directly:
```bash
export GEMINI_API_KEY=your-api-key
./mvnw spring-boot:run
```

### Using system Maven:
```bash
export GEMINI_API_KEY=your-api-key
mvn spring-boot:run
```

## API Testing

Use `apis.rest` file with VS Code REST Client extension to test the API endpoints.

## Endpoints

- `POST /api/v1/agent/interact` - Main interaction endpoint
- `POST /api/v1/agent/leads` - Submit customer information
- `GET /api/v1/agent/session/{sessionId}` - Get conversation history
- `GET /api/v1/agent/admin/leads` - Get all leads
- `GET /api/v1/agent/admin/leads/{leadId}` - Get specific lead

## CORS

CORS is configured to allow requests from frontend. Update `CorsConfig.java` to restrict origins in production.

