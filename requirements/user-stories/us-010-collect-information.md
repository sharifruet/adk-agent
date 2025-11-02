# US-010: Collect Customer Information

## User Story
As an **AI insurance agent**
I want to **collect customer information from interested prospects**
So that **I can register them as leads for the sales team to follow up**

## Acceptance Criteria

### AC-010.1: Mandatory Information Collection
- Given customer has shown interest
- When collecting information
- Then the system collects the following mandatory fields:
  - Full Name
  - Phone Number
  - National ID (NID)
  - Address (complete address)
  - Policy of Interest (specific policy name/ID)
- And all mandatory fields are collected before submission

### AC-010.2: Optional Information Collection
- Given mandatory information is being collected
- When system requests information
- Then the system may also collect optional information:
  - Email address
  - Preferred contact time
  - Additional notes/comments
- And optional fields don't block submission

### AC-010.3: Sequential Information Gathering
- Given system is collecting information
- When requesting each piece of information
- Then the system asks for one piece at a time
- And the system waits for response before asking next
- And the system confirms each piece before moving forward

### AC-010.4: Information Explanation
- Given system needs to ask for information
- When requesting each field
- Then the system explains why the information is needed
- And explanations are brief and reassuring
- And system addresses privacy concerns

### AC-010.5: Validation During Collection
- Given customer provides information
- When system receives input
- Then the system validates format immediately
- And the system asks for correction if invalid
- And the system provides helpful error messages

### AC-010.6: Information Confirmation
- Given all mandatory information is collected
- Before saving to database
- Then the system confirms all collected information with customer
- And customer can review and correct if needed
- And confirmation is clear and easy to understand

### AC-010.7: Missing Information Handling
- Given customer skips or provides incomplete information
- When system detects missing mandatory data
- Then the system politely asks for missing information
- And the system explains why it's needed
- And the system doesn't proceed until all mandatory data is collected

## Detailed Scenarios

### Scenario 1: Complete Information Collection
**Given**: Customer has shown interest  
**When**: System collects information  
**Then**: System asks for each field sequentially, validates each, confirms all at end, saves successfully

### Scenario 2: Invalid Phone Number
**Given**: Customer provides phone number "123"  
**When**: System validates  
**Then**: System detects invalid format, explains expected format (e.g., country code + number), asks for correction

### Scenario 3: Partial Address
**Given**: Customer provides only city name  
**When**: System requests address  
**Then**: System asks for complete address (street, city, postal code), explains why needed, validates completeness

### Scenario 4: Information Confirmation
**Given**: All information collected  
**When**: System confirms  
**Then**: System displays: "Let me confirm: Name: John Doe, Phone: +1234567890, Policy: Term Life 20yr... Is this correct?"
- Customer can say "yes" or request changes

### Scenario 5: Customer Changes Mind Mid-Collection
**Given**: System is collecting information  
**When**: Customer says "Actually, I'm not ready"  
**Then**: System acknowledges, asks if they want to continue later, offers to save progress (if partial data acceptable) or exits gracefully

## Technical Notes

- Information collection state machine
- Field-by-field collection logic
- Real-time validation for each field
- Format validation (phone, email, NID patterns)
- Data extraction from natural language responses
- Confirmation workflow
- Partial save capability (optional)

## Related Requirements
- **FR-6.1**: Mandatory information list
- **FR-6.2**: Validate before saving
- **FR-6.3**: Ask for missing information
- **FR-6.4**: Confirm information
- **FR-6.5**: Optional information
- **FR-8.1**: Phone number validation
- **FR-8.2**: NID validation
- **FR-8.3**: Email validation

## Dependencies
- **Depends on**: US-009 (interest detection), US-004 (policy selection)
- **Blocks**: US-011, US-012, US-013

## Story Points
**Estimate**: 10 points (complex state management and validation)

## Priority
**High** - Core functionality for lead generation

---

## Implementation Considerations

- Design information collection workflow/state machine
- Implement field-by-field validation
- Create natural language extraction for structured data (address, phone, etc.)
- Define validation patterns for each field type
- Consider partial save if customer needs to return later
- Design confirmation UI/format
- Handle interruptions gracefully (customer changes mind)

