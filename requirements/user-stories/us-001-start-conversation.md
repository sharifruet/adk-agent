# US-001: Start Conversation

## User Story
As a **potential customer**
I want to **start a conversation with the AI insurance agent**
So that **I can learn about life insurance options and get personalized advice**

## Acceptance Criteria

### AC-001.1: Automatic Welcome Message
- Given a new conversation session is initiated
- When the customer opens the chat interface
- Then the system automatically sends a friendly, professional welcome message
- And the welcome message includes agent name and purpose
- And the message waits for customer response before proceeding

### AC-001.2: Greeting Detection
- Given the system has sent the welcome message
- When customer responds with a greeting (hi, hello, hey, good morning, etc.)
- Then the system recognizes and responds appropriately to the greeting type
- And the system maintains a conversational, natural tone

### AC-001.3: Direct Question Handling
- Given a new conversation session
- When customer starts with a question or statement instead of greeting
- Then the system acknowledges the question and responds appropriately
- And the system doesn't require a formal greeting before addressing the question

### AC-001.4: Session Initialization
- Given a customer wants to start a conversation
- When they access the application
- Then a new conversation session is created with a unique session ID
- And the session state is initialized
- And conversation logs begin recording

## Detailed Scenarios

### Scenario 1: Happy Path - Casual Greeting
**Given**: Customer opens chat interface  
**When**: Customer sends "Hi"  
**Then**: System responds with personalized greeting and asks how it can help

### Scenario 2: Direct Question
**Given**: Customer opens chat interface  
**When**: Customer immediately asks "What life insurance policies do you offer?"  
**Then**: System acknowledges the question and begins providing policy information without requiring greeting

### Scenario 3: Formal Greeting
**Given**: Customer opens chat interface  
**When**: Customer sends "Good morning"  
**Then**: System responds with appropriate formal greeting and introduces itself

### Scenario 4: Session Creation
**Given**: Multiple customers accessing the system  
**When**: Each customer starts a conversation  
**Then**: Each gets a unique session ID and independent conversation state

## Technical Notes

- Session management required (in-memory or Redis)
- Conversation state initialization needed
- Session ID generation (UUID or similar)
- Welcome message template should be configurable
- Need to track conversation start timestamp

## Related Requirements
- **FR-1.1.1**: Automatic welcome message
- **FR-1.1.2**: Welcome message format
- **FR-1.1.3**: Wait for customer response
- **FR-1.1.4**: Handle direct questions
- **FR-1.1.5**: Greeting type detection

## Dependencies
- **Depends on**: None (foundational story)
- **Blocks**: US-002, US-003

## Story Points
**Estimate**: 3 points

## Priority
**High** - Foundation for all conversation features

---

## Implementation Considerations

- Consider using session middleware for session management
- Welcome message should be stored in configuration for easy updates
- Need logging for session creation and conversation starts
- Consider rate limiting for session creation to prevent abuse

