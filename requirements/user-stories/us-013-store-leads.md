# US-013: Store Lead Information

## User Story
As a **system**
I want to **store collected lead information persistently**
So that **leads can be retrieved, managed, and assigned to sales team**

## Acceptance Criteria

### AC-013.1: Database Storage
- Given customer information is confirmed
- When saving lead
- Then the system stores information in database with:
  - Full Name
  - Phone Number
  - National ID (encrypted)
  - Address
  - Policy of Interest
  - Email (if provided)
  - Preferred contact time (if provided)
  - Notes/comments (if provided)
  - Conversation ID (link to conversation log)
  - Timestamp (created_at, updated_at)
  - Status (new, contacted, converted, not_interested)
- And all mandatory fields are stored

### AC-013.2: Text File Storage (Phase 1)
- Given lead information is saved
- When storing (Phase 1 option)
- Then the system also supports storing in text files:
  - Format: JSON or CSV
  - One file per day or cumulative file
  - Includes all lead fields
  - Includes headers and structure
- And text file format is readable and importable

### AC-013.3: Data Persistence
- Given lead is saved
- When system restarts or encounters error
- Then lead information is not lost
- And data is persisted to durable storage
- And system recovers gracefully

### AC-013.4: Timestamp Recording
- Given lead is created or updated
- When storing
- Then the system records:
  - created_at: When lead was first created
  - updated_at: When lead was last modified
- And timestamps are accurate and timezone-aware

### AC-013.5: Conversation Linkage
- Given lead is saved
- When storing
- Then the system links lead to conversation:
  - Conversation ID stored in lead record
  - Link enables retrieval of full conversation transcript
- And linkage is bidirectional (can find lead from conversation, conversation from lead)

### AC-013.6: Status Management
- Given lead is created
- When storing
- Then the system sets initial status to "new"
- And status can be updated later (by admin or system)
- And status changes are tracked

### AC-013.7: Save Confirmation
- Given lead is saved successfully
- When save completes
- Then the system provides confirmation to customer
- And customer receives clear message about next steps
- And system thanks customer for their interest

## Detailed Scenarios

### Scenario 1: Successful Database Save
**Given**: All information confirmed  
**When**: System saves to database  
**Then**: Record created with all fields, timestamps set, status="new", conversation_id linked, customer receives confirmation

### Scenario 2: Text File Save (Phase 1)
**Given**: System configured for text file storage  
**When**: Lead is saved  
**Then**: Information appended to daily JSON/CSV file with proper format, all fields included

### Scenario 3: Save Failure Handling
**Given**: Database connection fails during save  
**When**: Save attempt fails  
**Then**: System retries (with backoff), logs error, notifies customer of delay, ensures eventual persistence

### Scenario 4: Duplicate Prevention
**Given**: Customer information matches existing lead  
**When**: System attempts save  
**Then**: System detects duplicate, updates existing record or creates new one based on business rules, links to conversation

## Technical Notes

- Database schema design (Lead table)
- Text file format and storage location
- Transaction handling for data consistency
- Encryption for sensitive fields (NID, phone)
- Retry logic for save failures
- Backup and recovery procedures
- Status enum/state management

## Related Requirements
- **FR-7.1**: Store in database
- **FR-7.2**: Support text file storage
- **FR-7.4**: Timestamp all records
- **FR-7.5**: Ensure data persistence
- **NFR-10**: Encrypt sensitive data

## Dependencies
- **Depends on**: US-012 (confirmation)
- **Blocks**: US-016, US-017 (viewing leads and conversations)

## Story Points
**Estimate**: 8 points

## Priority
**High** - Core functionality for lead management

---

## Implementation Considerations

- Design database schema (SQL or NoSQL)
- Implement encryption for PII (NID, phone)
- Choose text file format (JSON recommended for structure, CSV for human-readable)
- Implement save retry logic with exponential backoff
- Design backup strategy
- Consider data migration from text files to database later
- Implement proper error handling and logging

