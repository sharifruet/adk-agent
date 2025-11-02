# US-012: Confirm Information Before Saving

## User Story
As a **potential customer**
I want to **review and confirm my information before it's saved**
So that **I can ensure accuracy and have a chance to correct any mistakes**

## Acceptance Criteria

### AC-012.1: Information Summary
- Given all mandatory information is collected and validated
- Before saving to database
- Then the system presents a clear summary of all collected information:
  - Full Name
  - Phone Number
  - National ID
  - Address
  - Policy of Interest
  - Optional fields (email, preferred contact time, notes) if provided
- And summary is easy to read and understand

### AC-012.2: Confirmation Request
- Given information summary is presented
- When requesting confirmation
- Then the system clearly asks customer to confirm
- And confirmation question is simple (e.g., "Is this information correct?")
- And system waits for customer response

### AC-012.3: Confirmation Response Handling
- Given customer is asked to confirm
- When customer responds
- Then the system handles responses:
  - "Yes", "Correct", "That's right" → Proceed to save
  - "No", "That's wrong", specific corrections → Allow editing
  - Ambiguous response → Ask for clarification
- And system interprets responses accurately

### AC-012.4: Information Correction
- Given customer wants to correct information
- When customer indicates what's wrong
- Then the system:
  - Identifies which field needs correction
  - Asks for correct information
  - Validates new information
  - Re-confirms updated summary
- And correction process is smooth

### AC-012.5: Multiple Corrections
- Given customer needs to correct multiple fields
- When processing corrections
- Then the system handles corrections one at a time or all at once
- And system re-presents summary after corrections
- And system confirms again before saving

### AC-012.6: Final Confirmation
- Given customer has confirmed information (or corrected and re-confirmed)
- When ready to save
- Then the system confirms one final time
- And system proceeds to save only after final confirmation
- And system provides clear feedback that information is being saved

## Detailed Scenarios

### Scenario 1: Correct Information - Proceed
**Given**: All information collected  
**When**: System presents summary and asks confirmation  
**Then**: Customer confirms "Yes", system saves successfully

### Scenario 2: Incorrect Phone Number
**Given**: Summary shows wrong phone number  
**When**: Customer says "The phone number is wrong"  
**Then**: System asks for correct number, validates, updates summary, re-confirms

### Scenario 3: Partial Correction
**Given**: Summary shows multiple fields  
**When**: Customer corrects address only  
**Then**: System updates address, re-presents full summary, asks for confirmation again

### Scenario 4: Ambiguous Confirmation
**Given**: System asks "Is this correct?"  
**When**: Customer responds "Looks good"  
**Then**: System interprets as confirmation and proceeds to save

### Scenario 5: Customer Changes Mind
**Given**: System asks for confirmation  
**When**: Customer says "Actually, I'm not ready"  
**Then**: System acknowledges, asks if they want to continue later, handles gracefully

## Technical Notes

- Information summary formatting/rendering
- Confirmation response interpretation (NLP or pattern matching)
- Correction workflow state management
- Re-confirmation logic
- Save trigger only after final confirmation

## Related Requirements
- **FR-6.4**: Confirm collected information with customer

## Dependencies
- **Depends on**: US-010, US-011
- **Blocks**: US-013 (save only after confirmation)

## Story Points
**Estimate**: 3 points

## Priority
**High** - Important for data accuracy and customer trust

---

## Implementation Considerations

- Design summary presentation format (structured text, easy to read)
- Implement confirmation response interpretation
- Create correction workflow
- Ensure save only happens after explicit confirmation
- Handle edge cases: customer changes mind, ambiguous responses
- Consider saving draft if customer needs to return later (optional feature)

