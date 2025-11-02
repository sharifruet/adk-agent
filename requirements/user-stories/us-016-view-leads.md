# US-016: View Collected Leads

## User Story
As an **admin/sales manager**
I want to **view all collected leads**
So that **I can manage leads, assign them to sales team, and track conversions**

## Acceptance Criteria

### AC-016.1: Lead List Display
- Given admin accesses lead management interface
- When viewing leads
- Then the system displays list of all leads with:
  - Full Name
  - Phone Number (masked for privacy, full on detail view)
  - Policy of Interest
  - Status (new, contacted, converted, not_interested)
  - Created Date
  - Last Updated Date
- And list is sortable and filterable

### AC-016.2: Lead Filtering
- Given lead list is displayed
- When admin wants to filter
- Then the system allows filtering by:
  - Status
  - Policy of Interest
  - Date range (created date)
  - Date range (updated date)
- And filters can be combined

### AC-016.3: Lead Search
- Given lead list is displayed
- When admin searches
- Then the system allows searching by:
  - Name
  - Phone number
  - NID (with proper access control)
  - Email
- And search is fast and accurate

### AC-016.4: Lead Detail View
- Given admin selects a lead
- When viewing lead details
- Then the system displays all lead information:
  - Full Name
  - Phone Number (full)
  - National ID (encrypted, requires decryption permission)
  - Address
  - Policy of Interest
  - Email
  - Preferred contact time
  - Notes
  - Conversation ID (link to transcript)
  - Status
  - Timestamps
- And information is clearly formatted

### AC-016.5: Status Update
- Given admin views lead
- When updating status
- Then the system allows changing status:
  - new → contacted
  - contacted → converted
  - contacted → not_interested
  - Any status can be updated
- And status changes are logged with timestamp and user

### AC-016.6: Lead Assignment (Future)
- Given lead needs assignment
- When assigning to sales agent (future feature)
- Then the system allows assigning lead to specific sales agent
- And assignment is tracked

### AC-016.7: Pagination
- Given many leads exist
- When displaying list
- Then the system implements pagination:
  - Configurable page size (10, 25, 50, 100)
  - Page navigation
  - Total count display
- And pagination is performant

### AC-016.8: Access Control
- Given admin accesses leads
- When viewing sensitive information
- Then the system implements access control:
  - Authentication required
  - Role-based permissions
  - Sensitive data (NID) requires special permission
- And access is logged

## Detailed Scenarios

### Scenario 1: View All New Leads
**Given**: Admin accesses system  
**When**: Filters for status="new"  
**Then**: System displays all new leads, sorted by creation date (newest first), shows key information

### Scenario 2: Search for Specific Lead
**Given**: Admin knows customer name "John Doe"  
**When**: Searches "John Doe"  
**Then**: System finds matching leads, displays results, allows clicking for detail view

### Scenario 3: Update Lead Status
**Given**: Admin views lead detail  
**When**: Changes status from "new" to "contacted"  
**Then**: Status updates, timestamp recorded, change logged, list refreshes

### Scenario 4: View Lead with Conversation
**Given**: Admin views lead  
**When**: Clicks conversation link  
**Then**: System opens conversation transcript (US-017), shows full conversation history

## Technical Notes

- Lead list API with filtering, searching, pagination
- Lead detail API
- Status update API with logging
- Access control and authentication
- Data masking for privacy (phone numbers in list view)
- Encryption/decryption for sensitive fields (NID)

## Related Requirements
- **FR-7.1**: Store lead information (prerequisite)
- Use case UC-5: Admin views collected leads

## Dependencies
- **Depends on**: US-013 (lead storage)
- **Blocks**: None (standalone admin feature)

## Story Points
**Estimate**: 8 points

## Priority
**High** - Essential for lead management and sales operations

---

## Implementation Considerations

- Design lead list API with filters, search, pagination
- Implement access control (authentication, authorization)
- Create admin UI (web interface recommended)
- Design data masking for list view
- Implement status update workflow
- Create audit log for status changes
- Consider real-time updates if multiple admins use system
- Performance: index database fields used for filtering/searching

