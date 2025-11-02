# US-019: Manage Policy Information

## User Story
As an **admin**
I want to **manage policy information in the system**
So that **I can keep policy details up-to-date and add new policies**

## Acceptance Criteria

### AC-019.1: View Policies
- Given admin accesses policy management
- When viewing policies
- Then the system displays list of all policies:
  - Policy name
  - Policy type
  - Company (own company or competitor)
  - Coverage range
  - Premium range
  - Status (active, inactive)
  - Last updated date
- And list is sortable and searchable

### AC-019.2: View Policy Details
- Given admin selects a policy
- When viewing details
- Then the system displays complete policy information:
  - Policy name and type
  - Company
  - Coverage amount range (min, max)
  - Premium range (min, max, factors affecting cost)
  - Age requirements (min_age, max_age)
  - Benefits (array)
  - Features (array)
  - Description
  - Medical examination requirements
  - Eligibility criteria
  - Created date, updated date
- And information is clearly formatted

### AC-019.3: Create New Policy
- Given admin wants to add new policy
- When creating policy
- Then the system allows entering:
  - Policy name (required)
  - Policy type (term, whole life, universal, etc.)
  - Company (own company or competitor name)
  - Coverage range (required)
  - Premium range (required)
  - Age requirements (required)
  - Benefits (array)
  - Features (array)
  - Description
  - Medical requirements
  - Other eligibility criteria
- And system validates required fields
- And policy is saved to database

### AC-019.4: Update Policy Information
- Given admin views policy details
- When updating information
- Then the system allows editing all policy fields
- And system validates changes
- And updated_at timestamp is updated
- And change history is logged (optional)

### AC-019.5: Delete/Deactivate Policy
- Given admin wants to remove policy
- When deleting/deactivating
- Then the system:
  - Allows deactivating policy (sets status to inactive)
  - Prevents deletion if policy is referenced by leads
  - Shows warning if policy has active references
  - Optionally allows soft delete (archive)
- And system handles references gracefully

### AC-019.6: Policy Validation
- Given admin creates or updates policy
- When saving
- Then the system validates:
  - Required fields are present
  - Coverage range is valid (min < max)
  - Premium range is valid (min < max)
  - Age requirements are valid (min < max, reasonable ranges)
  - Data types are correct
- And validation errors are clearly displayed

### AC-019.7: Search and Filter Policies
- Given admin views policy list
- When searching or filtering
- Then the system allows:
  - Search by name
  - Filter by type
  - Filter by company
  - Filter by status
- And filters can be combined

### AC-019.8: Access Control
- Given policy management is accessed
- When performing operations
- Then the system enforces access control:
  - Authentication required
  - Admin role required
  - Actions are logged
- And unauthorized access is prevented

## Detailed Scenarios

### Scenario 1: Add New Company Policy
**Given**: Admin accesses policy management  
**When**: Creates new term life policy with all details  
**Then**: System validates, saves to database, policy becomes available for AI agent to present

### Scenario 2: Update Premium Range
**Given**: Policy premium rates have changed  
**When**: Admin updates premium range  
**Then**: System saves update, updates timestamp, change is immediately available in conversations

### Scenario 3: Deactivate Competitor Policy
**Given**: Competitor policy is no longer relevant  
**When**: Admin deactivates policy  
**Then**: System sets status to inactive, policy no longer shown to customers, existing references preserved

### Scenario 4: Delete Policy with Active Leads
**Given**: Admin tries to delete policy  
**When**: Policy has leads referencing it  
**Then**: System warns about active references, prevents deletion, suggests deactivation instead

## Technical Notes

- Policy CRUD API (Create, Read, Update, Delete)
- Policy database schema
- Validation rules and schemas
- Reference checking (leads using policy)
- Soft delete vs hard delete logic
- Access control and logging
- Cache invalidation (if policies are cached)

## Related Requirements
- **FR-9.1**: Maintain company policy database
- **FR-9.2**: Maintain competitor policy database
- **FR-9.3**: Include policy details
- **FR-9.4**: Allow policy updates

## Dependencies
- **Depends on**: None (standalone admin feature, but foundational for US-004)
- **Blocks**: US-004 (policies must exist to be presented)

## Story Points
**Estimate**: 8 points

## Priority
**High** - Foundational for policy presentation features

---

## Implementation Considerations

- Design policy database schema
- Implement CRUD operations with proper validation
- Create admin UI for policy management
- Implement reference checking before deletion
- Consider versioning for policy changes (optional)
- Implement caching strategy for policy data (for performance in conversations)
- Design data migration for initial policy data load

