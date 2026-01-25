# Life Insurance Sales Agent (LIC Agent)

A full-stack application featuring an AI-powered life insurance sales agent that helps prospective customers understand their insurance needs and collects lead information for human sales agents to follow up.

## Overview

The Life Insurance Sales Agent is designed to transform the customer acquisition process by providing 24/7 intelligent assistance. It engages users in natural conversations, assesses their financial situation and needs, recommends suitable insurance products (Term, Whole, Universal, etc.), handles objections, and seamlessly captures lead information when the customer expresses interest.

## Features

- **AI Sales Agent**: Powered by **Google Gemini**, capable of natural, context-aware conversations.
- **Product Expertise**: Comprehensive knowledge base covering:
  - Term Life Insurance
  - Whole Life Insurance
  - Universal Life Insurance
  - Variable Life Insurance
  - Group Life Insurance
- **Interactive Chat Interface**: Modern React-based UI with:
  - Real-time messaging
  - Typing indicators
  - Markdown support for rich text responses
  - Lead capture forms
- **Lead Management**: Automatically identifies purchase intent and collects customer details (Name, Phone, Email).
- **Session Persistence**: Maintains conversation context to provide personalized experiences.

## Project Structure

```
lic-agent/
├── backend/          # Spring Boot REST API
│   ├── src/          # Java source code
│   │   ├── main/java/com/i2gether/lic/
│   │   │   ├── config/       # Agent & App Config
│   │   │   ├── controllers/  # API Endpoints
│   │   │   ├── services/     # Business Logic
│   │   │   └── ...
│   │   └── resources/
│   │       ├── knowledgebase/# Insurance product details
│   │       └── prompts/      # System prompts
│   ├── pom.xml       # Maven configuration
│   └── run.sh        # Startup script
├── frontend/         # React.js frontend
│   ├── src/
│   │   ├── components/       # UI Components
│   │   ├── services/         # API Integration
│   │   └── ...
│   └── package.json  # Dependencies
├── docs/             # Documentation
│   ├── requirements.md
│   └── HELP.md
└── README.md         # This file
```

## Prerequisites

- **Java**: JDK 25 or later
- **Node.js**: v18 or later
- **Google Gemini API Key**: Required for the AI agent functionality.

## Configuration

The agent's behavior and connection settings can be configured in `backend/src/main/resources/application.yaml`.

### Core Settings

| Property | Default Value | Description |
|----------|---------------|-------------|
| `server.port` | `5098` | The port the backend server listens on. |
| `com.i2gether.lic.agent.name` | `Jibon-bima agent` | The internal name for the agent. |
| `com.i2gether.lic.agent.description` | `Jibon Bima corporation sales agent` | A brief description of the agent. |
| `com.i2gether.lic.agent.ai-model` | `gemini-2.5-flash` | The specific Gemini model version to use. |
| `com.i2gether.lic.agent.system-prompt` | `classpath:prompts/agent-system-prompt.txt` | Location of the system instructions file. |

### API Key Configuration

You must provide a valid Google Gemini API key. This can be done in two ways:

1.  **Environment Variable (Recommended)**:
    Set the `GEMINI_API_KEY` or `GOOGLE_API_KEY` environment variable on your system.
    ```bash
    export GEMINI_API_KEY=your_actual_api_key_here
    ```

2.  **Configuration File**:
    Directly edit `backend/src/main/resources/application.yaml`:
    ```yaml
    com:
      i2gether:
        lic:
          agent:
            api-key: YOUR_API_KEY_HERE
    ```

### Customizing the Agent

-   **System Prompt**: Modify `backend/src/main/resources/prompts/agent-system-prompt.txt` to change the agent's personality, tone, or core instructions.
-   **Knowledge Base**: The agent loads product information from `backend/src/main/resources/knowledgebase/`. Adding or editing Markdown files in this directory will automatically update the agent's knowledge.

### Updating Domain Knowledge

To add or update the insurance products information (domain knowledge):

1.  **Navigate to the knowledge base directory**:
    `backend/src/main/resources/knowledgebase/`

2.  **Add a new product**:
    -   Create a new Markdown file (e.g., `my-new-product.md`).
    -   Add comprehensive details about the product (features, benefits, eligibility, etc.).
    -   **Important**: You must register the new file in `backend/src/main/java/com/i2gether/lic/services/ProductService.java` inside the `getAllProductKnowledgeBase()` method. Add your filename to the `knowledgeBaseFiles` array.

3.  **Update an existing product**:
    -   Open the corresponding Markdown file (e.g., `term-benefit-pension-insurance.md`).
    -   Edit the content as needed. The agent will automatically pick up these changes upon restart.

4.  **Restart the backend**:
    The knowledge base is loaded into the agent's context during startup. You must restart the Spring Boot application for changes to take effect.

## Quick Start

### 1. Backend Setup

1.  Navigate to the `backend` directory:
    ```bash
    cd backend
    ```

2.  Ensure your API key is set (see [Configuration](#configuration)).

3.  Run the application:
    ```bash
    # Using the provided script
    ./run.sh

    # OR using Maven wrapper
    ./mvnw spring-boot:run
    ```
    The backend will start on `http://localhost:5098`.

### 2. Frontend Setup

1.  Open a new terminal and navigate to the `frontend` directory:
    ```bash
    cd frontend
    ```

2.  Install dependencies:
    ```bash
    npm install
    ```

3.  Start the development server:
    ```bash
    npm run dev
    ```
    The frontend will be available at `http://localhost:3000`.
    *Note: The frontend is configured to proxy API requests to `http://localhost:5098`.*

## API Endpoints

The backend provides the following REST endpoints:

- **Interaction**:
  - `POST /api/v1/agent/interact`: Main endpoint for chatting with the agent.
- **Leads**:
  - `POST /api/v1/agent/leads`: Submit captured lead information.
  - `GET /api/v1/agent/admin/leads`: Retrieve all leads (for admin/sales team).
  - `GET /api/v1/agent/admin/leads/{leadId}`: Retrieve a specific lead.
- **Session**:
  - `GET /api/v1/agent/session/{sessionId}`: Retrieve conversation history.

See `backend/apis.rest` for example API requests.

## Technology Stack

### Backend
- **Java 25**
- **Spring Boot 4.0.1**
- **Google ADK (Agent Development Kit)**
- **Google Gemini AI Model**

### Frontend
- **React 18**
- **Vite**
- **Axios**
- **CSS Modules**

## Documentation

- [Requirements Document](docs/requirements.md): Detailed functional and non-functional requirements.
- [Backend README](backend/README.md): Specific details about the backend architecture.
- [Frontend README](frontend/README.md): Frontend component structure and customization.

## License

[Add your license here]
