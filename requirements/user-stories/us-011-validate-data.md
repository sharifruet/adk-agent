# US-011: Validate Collected Data

## User Story
As a **system**
I want to **validate all collected customer information**
So that **I ensure data quality and prevent invalid or duplicate entries**

## Acceptance Criteria

### AC-011.1: Phone Number Validation
- Given customer provides phone number
- When validating
- Then the system validates:
  - Format matches country-specific patterns
  - Contains only digits and allowed characters (+, -, spaces)
  - Meets minimum/maximum length requirements
  - Includes country code if required
- And invalid formats are rejected with clear error message

### AC-011.2: NID Validation
- Given customer provides National ID
- When validating
- Then the system validates:
  - Format matches country-specific NID pattern
  - Contains correct number of characters/digits
  - Matches checksum/validation algorithm (if applicable)
- And invalid NIDs are rejected with format explanation

### AC-011.3: Email Validation
- Given customer provides email (if optional field collected)
- When validating
- Then the system validates:
  - Contains @ symbol
  - Has valid domain format
  - Follows email format standards (RFC 5322)
- And invalid emails are rejected with format example

### AC-011.4: Name Validation
- Given customer provides full name
- When validating
- Then the system validates:
  - Contains at least first and last name
  - Has reasonable length (not too short, not excessively long)
  - Contains only allowed characters (letters, spaces, hyphens, apostrophes)
- And invalid names are flagged for clarification

### AC-011.5: Address Validation
- Given customer provides address
- When validating
- Then the system validates:
  - Contains required components (street, city, postal code)
  - Has reasonable format and completeness
  - Postal code format matches country standards (if applicable)
- And incomplete addresses are requested for completion

### AC-011.6: Duplicate Detection
- Given customer information is collected
- When checking for duplicates
- Then the system checks:
  - Phone number matches existing lead
  - NID matches existing lead
- And duplicates are detected and flagged
- And system asks customer if they're a returning customer

### AC-011.7: Real-Time Validation
- Given customer provides information during collection
- When input is received
- Then validation occurs immediately
- And feedback is provided instantly
- And customer can correct before proceeding

### AC-011.8: Validation Error Messages
- Given validation fails
- When providing error message
- Then the system provides:
  - Clear explanation of what's wrong
  - Expected format or example
  - Helpful guidance for correction
- And messages are user-friendly, not technical

## Detailed Scenarios

### Scenario 1: Valid Phone Number
**Given**: Customer provides "+1-555-123-4567"  
**When**: System validates  
**Then**: Validation passes, system acknowledges and proceeds

### Scenario 2: Invalid Phone Format
**Given**: Customer provides "123"  
**When**: System validates  
**Then**: Validation fails, system explains: "Please provide a valid phone number with country code. Example: +1-555-123-4567"

### Scenario 3: Duplicate Phone Number
**Given**: Customer provides phone number that exists in database  
**When**: System checks  
**Then**: System detects duplicate, asks: "I found an existing record with this number. Are you a returning customer?"

### Scenario 4: Invalid NID Format
**Given**: Customer provides NID "ABC123" in country requiring 13 digits  
**When**: System validates  
**Then**: Validation fails, system explains expected format and length

### Scenario 5: Incomplete Address
**Given**: Customer provides only "New York"  
**When**: System validates  
**Then**: System requests: "I need your complete address including street address and postal code. Could you provide that?"

## Technical Notes

- Validation rules/patterns for each field type
- Country-specific validation (phone, NID, postal code formats)
- Regular expressions for format validation
- Checksum algorithms for NID (if applicable)
- Duplicate checking query logic
- Real-time validation integration with input handling
- Validation error message templates

## Related Requirements
- **FR-8.1**: Phone number format validation
- **FR-8.2**: NID format validation
- **FR-8.3**: Email format validation
- **FR-8.4**: Duplicate entry checking
- **FR-6.2**: Validate before saving

## Dependencies
- **Depends on**: US-010 (data collection)
- **Blocks**: US-012, US-013 (must validate before confirming and saving)

## Story Points
**Estimate**: 5 points

## Priority
**High** - Essential for data quality

---

## Implementation Considerations

- Define validation schemas/rules for each field
- Implement country-specific validation patterns
- Create validation service/module
- Design duplicate checking queries (index on phone, NID)
- Design user-friendly error messages
- Consider using validation libraries (e.g., python-phone-numbers, email-validator)
- Performance: ensure duplicate checks don't slow down collection process

