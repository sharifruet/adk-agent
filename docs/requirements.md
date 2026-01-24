# Life Insurance Sales Agent - Requirements Document

## 1. Project Overview

### 1.1 Purpose
Transform the generic AI agent application into a specialized **Life Insurance Sales Agent** that can interactively engage with prospective customers, provide information about insurance products, and collect customer information for policy subscriptions.

### 1.2 Domain
Life Insurance Company - Sales and Customer Acquisition

### 1.3 Target Users
- Prospective customers seeking life insurance
- Sales team members who need assistance
- Customer service representatives

## 2. Functional Requirements

### 2.1 Core Agent Capabilities

#### 2.1.1 Product Knowledge Base
The agent must have comprehensive knowledge about:
- **Term Life Insurance**
  - Features, benefits, coverage periods
  - Premium structures
  - Eligibility criteria
  - Suitable for different age groups and life stages
  
- **Whole Life Insurance**
  - Permanent coverage features
  - Cash value accumulation
  - Premium payment options
  - Investment component details

- **Universal Life Insurance**
  - Flexible premium options
  - Adjustable death benefits
  - Cash value growth potential
  - Policy customization options

- **Variable Life Insurance**
  - Investment-linked features
  - Risk factors
  - Market performance impact
  - Suitability criteria

- **Group Life Insurance**
  - Employer-sponsored plans
  - Coverage benefits
  - Enrollment processes
  - Portability options

#### 2.1.2 Sales Conversation Flow
1. **Initial Greeting & Engagement**
   - Warm, professional greeting
   - Introduction as a life insurance specialist
   - Understanding customer's current situation
   - Identifying needs and concerns

2. **Needs Assessment**
   - Financial situation inquiry
   - Dependents and family structure
   - Current insurance coverage status
   - Life stage and future goals
   - Budget considerations

3. **Product Recommendation**
   - Match customer needs with appropriate products
   - Explain product features and benefits
   - Address common concerns and objections
   - Provide comparisons between products

4. **Objection Handling**
   - Cost concerns
   - Coverage adequacy questions
   - Trust and credibility building
   - Policy terms clarification
   - Claims process explanation

5. **Interest Confirmation**
   - Gauge customer interest level
   - Answer final questions
   - Create urgency (if appropriate)
   - Move to information collection phase

6. **Lead Capture (Final Step)**
   - When customer shows interest, politely ask for contact information
   - Collect the following information:
     - Full Name
     - Phone Number
     - Email Address
   - Store the customer information along with conversation context
   - Inform customer that a human sales agent will contact them soon
   - Thank the customer for their interest
   - **Note**: This data will be used by human sales agents for follow-up communication

### 2.2 Interactive Features

#### 2.2.1 Conversation Management
- Maintain context throughout the conversation
- Remember customer preferences and stated needs
- Reference previous parts of the conversation
- Handle multi-turn conversations naturally

#### 2.2.2 Personalization
- Use customer's name once provided
- Tailor recommendations based on:
  - Age and life stage
  - Family situation
  - Financial capacity
  - Risk tolerance
  - Specific goals

#### 2.2.3 Engagement Techniques
- Ask probing questions to understand needs
- Use empathy and understanding
- Provide relevant examples and scenarios
- Build trust through transparency
- Create value proposition
- When interest is shown, naturally transition to collecting contact information
- Explain that a human agent will follow up for personalized service

### 2.3 Data Collection

#### 2.3.1 Customer Information Fields
**Information to Collect (when customer shows interest):**
- Full Name
- Phone Number
- Email Address

**Additional Context Stored:**
- Conversation history (for human agent reference)
- Product interest/mentions during conversation
- Customer concerns or questions raised
- Session timestamp
- Any preferences or needs mentioned

#### 2.3.2 Data Collection Approach
- Agent should naturally ask for information in conversation
- No complex forms or validation required at this stage
- Human sales agents will validate and follow up
- Store responses as provided by customer

#### 2.3.3 Data Storage
- Store customer information securely
- Associate with session ID and conversation history
- Timestamp all interactions
- Store raw conversation for human agent review
- **Purpose**: Enable human sales agents to understand context and follow up effectively

## 3. Technical Requirements

### 3.1 System Architecture

#### 3.1.1 Architecture Overview
- **Backend**: Spring Boot REST API (Java)
- **Frontend**: React.js Single Page Application (SPA)
- **Communication**: RESTful API over HTTP/HTTPS
- **State Management**: React Context API or Redux
- **Styling**: CSS Modules or Tailwind CSS

#### 3.1.2 Component Structure
```
Frontend (React)
├── Components
│   ├── ChatInterface (Main chat UI)
│   ├── MessageList (Display conversation)
│   ├── MessageInput (User input field)
│   ├── TypingIndicator (Agent thinking indicator)
│   ├── LeadCaptureForm (Collect customer info)
│   └── ProductCard (Display product info)
├── Services
│   ├── apiService.js (API calls)
│   └── sessionService.js (Session management)
├── Context/State
│   ├── ConversationContext (Conversation state)
│   └── UserContext (User/session state)
└── Utils
    ├── messageFormatter.js
    └── validators.js
```

### 3.2 Frontend Requirements

#### 3.2.1 Technology Stack
- **Framework**: React 18+
- **Build Tool**: Vite or Create React App
- **State Management**: React Context API (or Redux Toolkit if needed)
- **HTTP Client**: Axios or Fetch API
- **Styling**: 
  - Tailwind CSS (recommended) or
  - CSS Modules or
  - Styled Components
- **UI Components**: 
  - Material-UI (MUI) or
  - Ant Design or
  - Custom components
- **Routing**: React Router (if multi-page needed)
- **Form Handling**: React Hook Form
- **Date/Time**: date-fns or moment.js

#### 3.2.2 Frontend Features

**Chat Interface:**
- Real-time conversation display
- Message bubbles (user vs agent)
- Timestamp display
- Scroll to latest message
- Responsive design (mobile, tablet, desktop)
- Typing indicator when agent is processing
- Message status indicators

**User Experience:**
- Smooth animations and transitions
- Loading states
- Error handling and display
- Session persistence (localStorage)
- Auto-scroll to new messages
- Copy message functionality
- Clear conversation option

**Lead Capture:**
- Modal or inline form when agent requests info
- Form fields: Name, Phone, Email
- Simple validation (basic format checks)
- Submit button
- Success/error feedback
- Form auto-population if mentioned in chat

**Product Display:**
- Product cards when agent mentions products
- Expandable product details
- Comparison view (if multiple products)
- Visual icons and graphics

#### 3.2.3 Frontend API Integration

**API Endpoints to Consume:**
```javascript
// Main interaction endpoint
POST /api/v1/agent/interact
Request: { userId?, sessionId?, question }
Response: { userId, sessionId, answer, conversationState? }

// Submit lead information
POST /api/v1/agent/leads
Request: { sessionId, customerInfo: { name, phone, email } }
Response: { leadId, status, message }

// Get conversation history
GET /api/v1/agent/session/{sessionId}
Response: { sessionId, messages[], customerInfo?, createdAt }
```

**API Service Implementation:**
```javascript
// apiService.js
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export const agentService = {
  interact: async (question, userId, sessionId) => {
    const response = await fetch(`${API_BASE_URL}/api/v1/agent/interact`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ question, userId, sessionId })
    });
    return response.json();
  },
  
  submitLead: async (sessionId, customerInfo) => {
    const response = await fetch(`${API_BASE_URL}/api/v1/agent/leads`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ sessionId, customerInfo })
    });
    return response.json();
  },
  
  getSessionHistory: async (sessionId) => {
    const response = await fetch(`${API_BASE_URL}/api/v1/agent/session/${sessionId}`);
    return response.json();
  }
};
```

#### 3.2.4 Frontend State Management

**Conversation State:**
```javascript
// ConversationContext.js
{
  messages: [
    { id, type: 'user'|'agent', content, timestamp, sessionId }
  ],
  currentSessionId: UUID,
  userId: UUID,
  conversationState: 'GREETING' | 'NEEDS_ASSESSMENT' | 'PRODUCT_RECOMMENDATION' | 'LEAD_CAPTURE' | 'CLOSING',
  isLoading: boolean,
  error: string | null,
  customerInfo: { name?, phone?, email? }
}
```

**Actions:**
- `sendMessage(question)` - Send user message
- `receiveMessage(answer)` - Receive agent response
- `updateConversationState(state)` - Update conversation phase
- `setCustomerInfo(info)` - Store customer information
- `clearConversation()` - Reset conversation
- `loadSession(sessionId)` - Load previous session

#### 3.2.5 Frontend Deployment

**Build Configuration:**
- Environment variables for API URL
- Production build optimization
- Code splitting for performance
- Asset optimization

**Deployment Options:**
- Static hosting (Netlify, Vercel, AWS S3 + CloudFront)
- Docker containerization
- Nginx reverse proxy
- CDN for static assets

### 3.3 Backend API Enhancements

#### 3.1.1 Enhanced Request Model
```java
UserRequest {
    UUID userId (optional)
    UUID sessionId (optional)
    String question
    Map<String, String> context (optional) // For storing conversation context
    CustomerInfo customerInfo (optional) // For submitting customer information
}
```

#### 3.1.2 Enhanced Response Model
```java
UserResponse {
    UUID userId
    UUID sessionId
    String answer
    List<SuggestedQuestions> suggestedQuestions (optional)
    ProductRecommendation productRecommendation (optional)
    LeadCaptureForm leadCaptureForm (optional) // When customer shows interest
    ConversationState state // GREETING, NEEDS_ASSESSMENT, PRODUCT_RECOMMENDATION, LEAD_CAPTURE, CLOSING
}
```

#### 3.1.3 API Endpoints

**Public Endpoints (Frontend):**
- `POST /api/v1/agent/interact` - Main interaction endpoint
  - Request: `{ userId?, sessionId?, question }`
  - Response: `{ userId, sessionId, answer, conversationState? }`
  - CORS enabled for frontend domain

- `POST /api/v1/agent/leads` - Submit customer information
  - Request: `{ sessionId, customerInfo: { name, phone, email } }`
  - Response: `{ leadId, status, message }`
  - Stores lead for human agent follow-up

- `GET /api/v1/agent/session/{sessionId}` - Get conversation history
  - Response: `{ sessionId, messages[], customerInfo?, createdAt }`
  - Used for session restoration

**Internal Endpoints (Admin/Human Agents):**
- `GET /api/v1/admin/leads` - List all leads (for human agents)
- `GET /api/v1/admin/leads/{leadId}` - Get lead details with full conversation
- `PUT /api/v1/admin/leads/{leadId}/status` - Update lead status
- `GET /api/v1/admin/sessions` - List all sessions

### 3.2 Knowledge Base Integration

#### 3.2.1 Product Knowledge Base
- Create structured knowledge base files for each product type
- Include: features, benefits, pricing, eligibility, FAQs
- Format: Markdown or structured JSON
- Location: `src/main/resources/knowledgebase/`

#### 3.2.2 Agent System Prompt
- Update system prompt to reflect insurance sales agent role
- Include sales techniques and best practices
- Define conversation flow guidelines
- Set tone and personality (professional, empathetic, helpful)

### 3.3 Data Models

#### 3.3.1 Customer Information Model
```java
CustomerInfo {
    String fullName
    String phoneNumber
    String email
    // Additional context from conversation
    String conversationSummary
    List<String> mentionedProducts
    List<String> customerConcerns
    Map<String, String> additionalNotes
}
```

#### 3.3.2 Product Model
```java
InsuranceProduct {
    String productId
    String productName
    String productType // TERM, WHOLE, UNIVERSAL, VARIABLE, GROUP
    String description
    List<String> features
    List<String> benefits
    PricingInfo pricing
    EligibilityCriteria eligibility
    List<String> faqs
}
```

#### 3.3.3 Lead Model
```java
Lead {
    UUID leadId
    UUID sessionId
    UUID userId
    CustomerInfo customerInfo
    String fullConversationHistory // Complete conversation for human agent review
    LocalDateTime createdAt
    String status // NEW, PENDING_CONTACT, CONTACTED, etc.
    String notes // For human sales agent to add notes
    // This lead will be picked up by human sales agents for follow-up
}
```

### 3.4 CORS and Security Configuration

#### 3.4.1 CORS Setup
- Enable CORS for frontend domain
- Allow credentials if needed
- Configure allowed methods (GET, POST, OPTIONS)
- Set appropriate headers

#### 3.4.2 Security Headers
- Content-Security-Policy
- X-Frame-Options
- X-Content-Type-Options
- HTTPS enforcement in production

### 3.6 Service Layer Enhancements

#### 3.6.1 Product Service
- Retrieve product information
- Match products to customer needs
- Provide product comparisons
- Answer product-specific questions

#### 3.6.2 Lead Management Service
- Store customer information as provided
- Store complete conversation history
- Associate customer info with conversation context
- Provide lead data to human sales agents
- Track lead status (managed by human agents)

#### 3.6.3 Conversation Service
- Manage conversation state
- Track conversation history
- Detect interest signals
- Trigger lead capture when appropriate

### 3.7 Database/Storage Requirements

#### 3.7.1 Data Storage Options
**Option 1: In-Memory (Development/Simple)**
- Use ConcurrentHashMap for sessions and leads
- Suitable for small scale or development
- Data lost on restart

**Option 2: Relational Database (Production)**
- PostgreSQL or MySQL
- Tables: sessions, messages, leads, customers
- ACID compliance for data integrity
- Support for complex queries

**Option 3: NoSQL Database**
- MongoDB for flexible schema
- Good for conversation history storage
- Easy to scale

#### 3.7.2 Data Models (Database Schema)

**Sessions Table:**
```sql
CREATE TABLE sessions (
    session_id UUID PRIMARY KEY,
    user_id UUID,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    conversation_state VARCHAR(50),
    customer_name VARCHAR(255),
    customer_phone VARCHAR(50),
    customer_email VARCHAR(255)
);
```

**Messages Table:**
```sql
CREATE TABLE messages (
    message_id UUID PRIMARY KEY,
    session_id UUID REFERENCES sessions(session_id),
    message_type VARCHAR(10), -- 'user' or 'agent'
    content TEXT,
    timestamp TIMESTAMP,
    sequence_number INTEGER
);
```

**Leads Table:**
```sql
CREATE TABLE leads (
    lead_id UUID PRIMARY KEY,
    session_id UUID REFERENCES sessions(session_id),
    customer_name VARCHAR(255),
    customer_phone VARCHAR(50),
    customer_email VARCHAR(255),
    conversation_summary TEXT,
    mentioned_products TEXT[],
    created_at TIMESTAMP,
    status VARCHAR(50), -- 'NEW', 'CONTACTED', etc.
    notes TEXT
);
```

### 3.8 Frontend-Backend Integration

#### 3.8.1 API Communication Flow
1. User sends message from React frontend
2. Frontend calls `POST /api/v1/agent/interact`
3. Backend processes with AI agent
4. Backend returns response with conversation state
5. Frontend updates UI with agent response
6. If state is LEAD_CAPTURE, show form
7. User submits info, frontend calls `POST /api/v1/agent/leads`
8. Backend stores lead and returns confirmation

#### 3.8.2 Error Handling
- Network errors: Show user-friendly message
- API errors: Display error from backend
- Timeout handling: Retry mechanism
- Offline detection: Queue messages

#### 3.8.3 Session Management
- Generate sessionId on first interaction
- Store in localStorage
- Send with each request
- Restore conversation on page reload

## 4. Non-Functional Requirements

### 4.1 Performance
- **Backend**: Response time < 2 seconds for agent responses
- **Frontend**: First Contentful Paint < 1.5 seconds
- **Frontend**: Time to Interactive < 3 seconds
- Support concurrent conversations (100+ concurrent users)
- Efficient session management
- Optimized API calls (debouncing, caching where appropriate)
- Code splitting in React for faster initial load

### 4.2 Security
- Secure storage of customer information
- Data encryption at rest and in transit (HTTPS/TLS)
- Compliance with data protection regulations (GDPR, CCPA)
- Secure API endpoints (authentication if needed)
- Input sanitization on both frontend and backend
- XSS protection
- CSRF protection for state-changing operations
- Environment variable management for sensitive data
- Secure session management

### 4.3 Usability
- **Agent**: Natural, conversational language
- **Agent**: Clear and concise responses
- **Agent**: Professional and empathetic tone
- **Frontend**: Intuitive chat interface
- **Frontend**: Mobile-responsive design
- **Frontend**: Accessible (WCAG 2.1 AA compliance)
- **Frontend**: Fast and responsive UI
- Easy information collection process
- Clear visual feedback for all actions

### 4.4 Reliability
- Handle errors gracefully
- Provide fallback responses
- Maintain conversation context
- Recover from interruptions

## 5. Implementation Phases

### Phase 1: Foundation & Backend (Week 1)
- [ ] Update system prompt for insurance sales agent
- [ ] Create product knowledge base structure
- [ ] Enhance request/response models
- [ ] Update agent configuration
- [ ] Set up CORS configuration
- [ ] Create basic API endpoints

### Phase 1.5: Frontend Setup (Week 1-2)
- [ ] Initialize React project (Vite or CRA)
- [ ] Set up project structure
- [ ] Configure routing (if needed)
- [ ] Set up state management (Context API)
- [ ] Create API service layer
- [ ] Set up styling (Tailwind CSS or chosen framework)
- [ ] Create basic chat UI components

### Phase 2: Knowledge Base & Frontend Integration (Week 2)
- [ ] Create detailed product information files
- [ ] Add FAQs for each product
- [ ] Create comparison matrices
- [ ] Add objection handling responses

### Phase 3: Conversation Flow & UI Enhancement (Week 2-3)
- [ ] Implement conversation state management
- [ ] Add needs assessment logic
- [ ] Implement product recommendation engine
- [ ] Add objection handling responses

### Phase 4: Lead Capture & Form Integration (Week 3)
- [ ] Create customer information models
- [ ] Create lead storage service
- [ ] Implement conversation history storage
- [ ] Add natural lead capture in agent responses
- [ ] Create endpoint for human agents to retrieve leads

### Phase 5: Integration & Testing (Week 3-4)
- [ ] Integrate frontend with backend
- [ ] End-to-end testing
- [ ] Test conversation flows
- [ ] Test lead capture process
- [ ] Cross-browser testing
- [ ] Mobile responsiveness testing
- [ ] Performance testing (frontend and backend)
- [ ] Security review
- [ ] Accessibility testing

### Phase 6: Deployment (Week 4)
- [ ] Build production frontend bundle
- [ ] Configure production API URL
- [ ] Deploy backend to production
- [ ] Deploy frontend to static hosting/CDN
- [ ] Set up CI/CD pipeline
- [ ] Deploy to staging environment
- [ ] User acceptance testing
- [ ] Production deployment
- [ ] Monitoring setup (frontend and backend)
- [ ] Error tracking (Sentry, etc.)

## 6. Success Metrics

### 6.1 Engagement Metrics
- Average conversation length
- Number of questions per session
- Return visitor rate
- Engagement rate

### 6.2 Conversion Metrics
- Lead capture rate (interested customers)
- Information completion rate
- Follow-up contact success rate
- Policy application conversion rate

### 6.3 Quality Metrics
- Customer satisfaction scores
- Response relevance
- Information accuracy
- Objection handling success rate

## 7. Compliance & Legal

### 7.1 Regulatory Compliance
- Insurance sales regulations compliance
- Data protection compliance
- Consumer protection laws
- Disclosure requirements

### 7.2 Disclaimers
- Clear disclaimers about policy terms
- Limitations of AI agent advice
- Requirement for human agent consultation
- Terms and conditions acceptance

## 8. Future Enhancements

### 8.1 Advanced Features
- Multi-language support
- Voice interaction capability
- Integration with CRM systems
- Automated follow-up emails
- Policy application form integration
- Payment processing integration

### 8.2 Analytics
- Conversation analytics dashboard
- Lead quality scoring
- Product recommendation effectiveness
- Customer journey tracking

### 8.3 Personalization
- Machine learning for better recommendations
- Customer preference learning
- Personalized product suggestions
- Dynamic pricing information

## 9. Dependencies

### 9.1 Backend Dependencies
- Spring Boot 4.0.1
- Google ADK (google-adk 0.5.0)
- Spring Web MVC
- Lombok
- Database driver (if using database)
  - PostgreSQL: postgresql
  - MySQL: mysql-connector-java
  - MongoDB: spring-boot-starter-data-mongodb

### 9.2 Frontend Dependencies
- React 18+
- React DOM
- React Router (if multi-page)
- Axios or Fetch API
- React Hook Form
- Tailwind CSS (or chosen CSS framework)
- UI Component Library (Material-UI, Ant Design, or custom)
- date-fns (for date formatting)

### 9.3 External Services
- Google Gemini API (already integrated)
- Database for lead storage (PostgreSQL/MySQL/MongoDB)
- Email service for notifications (optional)
- SMS service for notifications (optional)
- CDN for frontend assets (CloudFront, Cloudflare)
- Static hosting (Netlify, Vercel, AWS S3)

### 9.4 Development Tools
- Node.js 18+ (for frontend development)
- npm or yarn (package manager)
- Git (version control)
- Docker (optional, for containerization)
- Postman or similar (API testing)

### 9.5 Internal Systems
- CRM integration (future)
- Policy management system (future)
- Document management system (future)

## 10. Risk Assessment

### 10.1 Technical Risks
- API rate limits
- Service downtime
- Data loss
- Security breaches

### 10.2 Business Risks
- Regulatory compliance issues
- Customer data privacy concerns
- Misleading information liability
- Low conversion rates

### 10.3 Mitigation Strategies
- Implement rate limiting and caching
- Regular backups and disaster recovery
- Security audits and compliance reviews
- Continuous monitoring and improvement

