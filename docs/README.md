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

The key is passed straight to the Gemini model, so this works for `mvn spring-boot:run` and for a packaged jar (`java -jar`) alike, with no environment variable needed. When the line is left at its default (`${GEMINI_API_KEY:${GOOGLE_API_KEY:}}`), the value is taken from those environment variables instead.

The `run.sh` script still works: it reads the key from `application.yaml`, exports it, and starts the application with Maven.

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

If you see the error "API key must either be provided or set in the environment variable GOOGLE_API_KEY or GEMINI_API_KEY", the application found no key at all. Either:

1. Put the key in `application.yaml` under `com.i2gether.lic.agent.api-key` and rebuild the jar, or
2. Set `GEMINI_API_KEY` (or `GOOGLE_API_KEY`) in the environment of the process that runs the jar.

Note: the Google client library only reads those environment variables, and on Java 17+ a running application cannot inject variables into its own environment. That is why the key from `application.yaml` is handed to the model explicitly in `AgentConfiguration` rather than copied into the environment.

