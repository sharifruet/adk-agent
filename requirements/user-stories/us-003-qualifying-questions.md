# US-003: Collect Qualifying Information

## User Story
As an **AI insurance agent**
I want to **ask qualifying questions to understand the customer's needs**
So that **I can provide relevant policy recommendations and personalize the conversation**

## Acceptance Criteria

### AC-003.1: Conversational Question Style
- Given the conversation has started
- When asking qualifying questions
- Then questions are asked in a conversational, non-interrogative manner
- And questions feel natural, not like a form or survey
- And the system asks one question at a time

### AC-003.2: Information Collection
- Given the system needs customer information
- When conducting qualification phase
- Then the system collects the following information:
  - Customer's age (numeric, 18-100)
  - Current life insurance coverage status (yes/no/unsure)
  - Purpose for seeking insurance (family protection, debt coverage, business, estate planning)
  - Approximate coverage amount interest or budget range (optional)
  - Dependents/family situation (spouse, children, parents to support)
- And each piece of information is validated before moving to next question

### AC-003.3: Response Format Flexibility
- Given the system asks a question
- When customer responds in various formats
- Then the system accepts multiple formats (e.g., "I'm 35", "35 years old", "mid-thirties")
- And the system extracts the correct information regardless of format
- And the system validates the extracted information

### AC-003.4: Partial Answer Handling
- Given the system asks a question
- When customer provides partial or unclear answer
- Then the system asks clarifying follow-up questions
- And the system doesn't move forward until information is clear
- And the system explains why the question is important

### AC-003.5: Question Rationale
- Given the system needs to ask a question
- When asking the question
- Then the system explains why it's being asked (to build trust)
- And the explanation is brief and natural

### AC-003.6: Evasion Handling
- Given the system asks a question
- When customer evades or shows unwillingness to answer
- Then the system handles the situation gracefully
- And the system doesn't pressure or become aggressive
- And the system may skip non-critical questions or rephrase

### AC-003.7: Information Extraction and Validation
- Given customer provides a response
- When extracting information
- Then the system validates data format and range
- And the system extracts structured data from natural language
- And the system stores extracted information in conversation context

## Detailed Scenarios

### Scenario 1: Happy Path - Complete Answers
**Given**: System asks "May I know your age?"  
**When**: Customer responds "I'm 32 years old"  
**Then**: System extracts age 32, validates range (18-100), stores in context, proceeds to next question

### Scenario 2: Partial Answer - Needs Clarification
**Given**: System asks about coverage needs  
**When**: Customer responds "Something for my family"  
**Then**: System asks clarifying questions about family size, dependents, specific protection needs

### Scenario 3: Multiple Formats
**Given**: System asks for age  
**When**: Customer responds in various ways:
- "I'm 45"
- "45 years old"
- "mid-forties"
- "I turned 45 last month"
**Then**: System correctly extracts age as 45 in all cases

### Scenario 4: Evasion Handling
**Given**: System asks about current insurance  
**When**: Customer responds "I'd rather not say" or changes topic  
**Then**: System acknowledges respectfully, explains importance briefly, offers to continue without that information or skips to next question

### Scenario 5: Invalid Data
**Given**: System asks for age  
**When**: Customer provides invalid input (e.g., "I'm a vampire", "999", negative number)  
**Then**: System politely asks for clarification and explains valid range

## Technical Notes

- NLP for information extraction (spacy, NER, or LLM-based extraction)
- Age extraction patterns: numeric patterns, age-related keywords
- Validation rules for each data type
- Conversation state machine to track which questions have been answered
- Context storage for collected information
- Natural language understanding for flexible input formats

## Related Requirements
- **FR-1.3.1**: Conversational question style
- **FR-1.3.2**: One question at a time
- **FR-1.3.3**: Validate and extract before next question
- **FR-1.3.4**: Qualifying information list
- **FR-1.3.5**: Handle partial answers
- **FR-1.3.6**: Accept various formats
- **FR-1.3.7**: Handle evasion gracefully
- **FR-1.3.8**: Explain why asking

## Dependencies
- **Depends on**: US-001, US-002
- **Blocks**: US-004, US-008

## Story Points
**Estimate**: 8 points (complex NLP requirements)

## Priority
**High** - Essential for personalization and relevant recommendations

---

## Implementation Considerations

- Use LLM for information extraction from natural language
- Define clear validation schemas for each data type
- Implement conversation state tracking
- Create templates for common question explanations
- Consider using structured extraction (function calling) if LLM supports it
- Handle edge cases: ambiguous responses, multiple mentions of same data

