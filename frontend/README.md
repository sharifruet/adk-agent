# Frontend - Life Insurance Sales Agent

React.js frontend application for the Life Insurance Sales Agent.

## Setup

1. Install dependencies:
```bash
npm install
```

2. Create `.env` file (optional, defaults to http://localhost:5098):
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
│   │   ├── ChatInterface.jsx  # Header, conversation column, composer
│   │   ├── EmptyState.jsx     # Welcome screen with suggested questions
│   │   ├── MessageList.jsx
│   │   ├── Message.jsx        # Markdown rendering for agent replies
│   │   ├── MessageInput.jsx
│   │   ├── TypingIndicator.jsx
│   │   └── LeadCaptureForm.jsx
│   ├── context/             # State management
│   │   ├── ConversationContext.jsx
│   │   └── LocaleContext.jsx  # Interface language (bn / en), persisted
│   ├── i18n/
│   │   └── strings.js         # Every static string, in Bangla and English
│   ├── services/            # API services
│   │   └── agentService.js
│   ├── utils/
│   ├── App.jsx
│   ├── main.jsx
│   └── index.css            # Design tokens, font stack, base styles
├── public/
│   └── fonts/               # Self-hosted Nikosh fallback (see its README)
├── package.json
└── vite.config.js
```

## Design

- **Typography**: Manrope for Latin, Hind Siliguri for Bangla, both from
  Google Fonts. See `public/fonts/README.md` for how the two are combined.
- **Colour**: everything derives from the JBC navy sampled from the logo.
  Tokens are defined on `:root` in `src/index.css`.
- **Language**: the chrome (welcome text, placeholders, form labels, errors)
  follows the toggle in the header and defaults to Bangla. The agent itself
  answers in whichever language the customer writes in.

## Features

- Real-time chat interface with Markdown replies (lists, tables, links)
- Bangla and English interface, remembered between visits
- Suggested opening questions
- Lead capture form shown inline in the conversation
- New chat control that also starts a fresh backend session
- Typing indicator
- Responsive design, keyboard accessible, respects reduced motion
- Error handling

## API Integration

The frontend communicates with the backend API at `http://localhost:5098` by default. This can be configured via the `VITE_API_URL` environment variable.
