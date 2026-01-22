# LIC Agent

A Spring Boot application using Google's ADK (Agent Development Kit) with Gemini AI.

## Prerequisites

- Java 25+
- Maven 3.6+

## Configuration

The application requires a Gemini API key. You can configure it in two ways:

### Option 1: Set Environment Variable (Recommended)

Set the environment variable before starting the application:

```bash
export GEMINI_API_KEY=your-api-key-here
# or
export GOOGLE_API_KEY=your-api-key-here
```

Then run:
```bash
mvn spring-boot:run
```

### Option 2: Use application.yaml

Add your API key to `src/main/resources/application.yaml`:

```yaml
com:
  i2gether:
    lic:
      agent:
        api-key: your-api-key-here
```

Then use the provided script to automatically set the environment variable:

```bash
./run.sh
```

The script reads the API key from `application.yaml` and sets it as an environment variable before starting the application.

## Running the Application

### Using the run script (reads from application.yaml):
```bash
./run.sh
```

### Using Maven directly (requires environment variable):
```bash
export GEMINI_API_KEY=your-api-key-here
mvn spring-boot:run
```

### Using Maven with inline environment variable:
```bash
GEMINI_API_KEY=your-api-key-here mvn spring-boot:run
```

## API Endpoints

Once running, the application exposes:

- `POST /api/v1/agent/interact` - Interact with the AI agent

See `apis.rest` for example requests using the VS Code REST Client extension.

## Troubleshooting

If you see the error: "API key must either be provided or set in the environment variable GOOGLE_API_KEY or GEMINI_API_KEY"

1. Ensure the environment variable is set before the JVM starts
2. Use the `run.sh` script which handles this automatically
3. Or set it manually: `export GEMINI_API_KEY=your-key` before running

Note: The library checks environment variables at runtime, so they must be set before starting the application. Setting them programmatically inside the application may not work due to JVM security restrictions.

