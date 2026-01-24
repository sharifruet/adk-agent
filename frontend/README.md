# Frontend - Life Insurance Sales Agent

React.js frontend application for the Life Insurance Sales Agent.

## Setup

1. Install dependencies:
```bash
npm install
```

2. Create `.env` file (optional, defaults to http://localhost:8080):
```bash
cp .env.example .env
# Edit .env and set VITE_API_URL if needed
```

3. Start development server:
```bash
npm run dev
```

The application will be available at `http://localhost:3000`

## Build for Production

```bash
npm run build
```

The built files will be in the `dist/` directory.

## Project Structure

```
frontend/
├── src/
│   ├── components/          # React components
│   │   ├── ChatInterface.jsx
│   │   ├── MessageList.jsx
│   │   ├── Message.jsx
│   │   ├── MessageInput.jsx
│   │   ├── TypingIndicator.jsx
│   │   └── LeadCaptureForm.jsx
│   ├── context/             # State management
│   │   └── ConversationContext.jsx
│   ├── services/            # API services
│   │   └── agentService.js
│   ├── App.jsx
│   ├── main.jsx
│   └── index.css
├── public/                  # Static assets
├── package.json
└── vite.config.js
```

## Features

- Real-time chat interface
- Conversation history persistence (localStorage)
- Lead capture form
- Typing indicator
- Responsive design
- Error handling

## API Integration

The frontend communicates with the backend API at `http://localhost:8080` by default. This can be configured via the `VITE_API_URL` environment variable.
