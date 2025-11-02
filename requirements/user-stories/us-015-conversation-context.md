# US-015: Maintain Conversation Context

## User Story
As a **potential customer**
I want the **AI agent to remember our conversation**
So that **I don't have to repeat information and the conversation feels natural**

## Acceptance Criteria

### AC-015.1: Context Memory
- Given a conversation is in progress
- When customer provides information or asks questions
- Then the system remembers throughout conversation:
  - Customer profile information (age, family, needs)
  - Policies discussed and customer reactions
  - Questions asked and answers provided
  - Objections raised and responses given
  - Stated preferences and concerns
  - Collected information
- And context is maintained for entire session

### AC-015.2: Natural Context Reference
- Given previous conversation elements exist
- When system responds
- Then the system references previous elements naturally:
  - "Earlier you mentioned..."
  - "As we discussed..."
  - "Based on your situation..."
- And references feel natural, not forced

### AC-015.3: Avoid Repetition
- Given information was already provided
- When system responds
- Then the system doesn't repeat entire information unless:
  - Customer requests clarification
  - Customer asks to repeat
  - Summarizing at end of conversation
- And system acknowledges previous discussion

### AC-015.4: Context History
- Given conversation is ongoing
- When maintaining context
- Then the system maintains conversation history:
  - Minimum 50 previous messages in context
  - Full conversation history accessible
  - Context window optimized for LLM
- And system manages context window efficiently

### AC-015.5: Context Switching
- Given customer changes topic
- When handling topic change
- Then the system:
  - Preserves relevant background information
  - Maintains connection to previous topics when relevant
  - Switches context smoothly
- And system doesn't lose important information

### AC-015.6: Long Conversation Handling
- Given conversation exceeds context window
- When managing long conversations
- Then the system:
  - Maintains summary of earlier conversation
  - Preserves key information (profile, collected data)
  - Manages context window intelligently
- And conversation remains coherent

## Detailed Scenarios

### Scenario 1: Reference Previous Statement
**Given**: Customer earlier said "I have two children"  
**When**: Discussing policy benefits  
**Then**: System says "Since you mentioned you have two children, this policy would protect them with..."

### Scenario 2: Avoid Repeating Policy Info
**Given**: System already explained Term Life policy  
**When**: Customer asks follow-up question about same policy  
**Then**: System answers without repeating entire policy description, references previous explanation

### Scenario 3: Context Across Topics
**Given**: Customer discussed age (35), then asked about policies, then asked about cost  
**When**: Discussing cost  
**Then**: System remembers age when discussing age-related premiums, remembers policy interest when discussing payment

### Scenario 4: Long Conversation - Context Management
**Given**: 100-message conversation  
**When**: System responds to latest message  
**Then**: System maintains summary of earlier conversation, keeps key profile data, references recent messages, stays coherent

## Technical Notes

- Conversation state management (in-memory, Redis, or database)
- Context window management for LLM
- Conversation summarization for long conversations
- Key information extraction and preservation
- Context vector/embedding (if using semantic search)
- Memory architecture (short-term session, long-term if needed)

## Related Requirements
- **FR-4.1.1**: Maintain context throughout session
- **FR-4.1.2**: Remember discussed information
- **FR-4.1.3**: Reference previous elements naturally
- **FR-4.1.4**: Avoid repetition
- **FR-4.1.5**: Maintain conversation history (50 messages minimum)
- **FR-4.1.6**: Handle context switches

## Dependencies
- **Depends on**: US-001 (session management)
- **Blocks**: All conversation features (enhances all)

## Story Points
**Estimate**: 8 points

## Priority
**High** - Critical for natural conversation and user experience

---

## Implementation Considerations

- Design conversation state data structure
- Implement context window management (sliding window, summarization)
- Create conversation summarization (LLM-based or extractive)
- Design key information extraction (profile, collected data, preferences)
- Optimize context for LLM token limits
- Consider semantic search for retrieving relevant past context
- Implement efficient context storage and retrieval

