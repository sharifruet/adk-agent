# Life Insurance Sales Agent

A full-stack application featuring an AI-powered life insurance sales agent that helps prospective customers understand their insurance needs and collects lead information for human sales agents to follow up.

## Project Structure

```
lic-agent/
├── backend/          # Spring Boot REST API
│   ├── src/         # Java source code
│   ├── pom.xml      # Maven configuration
│   ├── mvnw         # Maven wrapper
│   └── run.sh       # Run script
├── frontend/        # React.js frontend (to be created)
├── docs/            # Documentation
│   ├── requirements.md
│   ├── README.md    # Detailed documentation
│   └── HELP.md
└── README.md        # This file
```

## Quick Start

### Backend

1. Navigate to backend directory:
   ```bash
   cd backend
   ```

2. Set your Gemini API key in `src/main/resources/application.yaml`:
   ```yaml
   com:
     i2gether:
       lic:
         agent:
           api-key: YOUR_API_KEY_HERE
   ```

3. Run the application:
   ```bash
   ./run.sh
   ```
   
   Or manually:
   ```bash
   export GEMINI_API_KEY=your-api-key
   ./mvnw spring-boot:run
   ```

The backend will start on `http://localhost:8080`

### Frontend

Frontend setup instructions will be added when the React application is created.

## API Endpoints

- `POST /api/v1/agent/interact` - Main interaction endpoint
- `POST /api/v1/agent/leads` - Submit customer information
- `GET /api/v1/agent/session/{sessionId}` - Get conversation history
- `GET /api/v1/agent/admin/leads` - Get all leads (for human agents)

See `backend/apis.rest` for API testing examples.

## Documentation

- [Requirements Document](docs/requirements.md) - Complete project requirements
- [Backend README](docs/README.md) - Detailed backend documentation

## Technology Stack

### Backend
- Java 25
- Spring Boot 4.0.1
- Google ADK (Agent Development Kit)
- Gemini AI

### Frontend (Planned)
- React 18+
- Tailwind CSS
- Axios

## Development

### Backend Development
All backend code is in the `backend/` directory. Use standard Maven commands from that directory.

### Frontend Development
Frontend code will be in the `frontend/` directory. Setup instructions will be provided.

## License

[Add your license here]

