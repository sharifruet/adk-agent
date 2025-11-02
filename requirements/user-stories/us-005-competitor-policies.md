# US-005: Provide Competitor Policy Information

## User Story
As a **potential customer**
I want to **learn about competitor policies for comparison**
So that **I can make an informed decision by understanding all available options**

## Acceptance Criteria

### AC-005.1: Accurate Competitor Information
- Given customer asks about competitor policies
- When providing competitor information
- Then the system provides accurate, factual information
- And the system maintains fairness (no false claims)
- And the system acknowledges when information may not be current

### AC-005.2: Strategic Comparison
- Given competitor policies are being discussed
- When presenting competitor information
- Then the system uses competitor information strategically
- And the system highlights company policy advantages naturally
- And comparisons are fair and honest

### AC-005.3: On-Demand Provision
- Given competitor information is available
- When presenting information
- Then the system only discusses competitors when:
  - Customer explicitly asks about competitor
  - Customer requests comparison
  - Customer shows interest in competitor offerings
- And the system doesn't proactively mention competitors unless strategically beneficial

### AC-005.4: Limited Information Handling
- Given customer asks about specific competitor policy
- When system doesn't have detailed information
- Then the system acknowledges the limitation honestly
- And the system offers to help with what is known
- And the system may suggest speaking with sales team for detailed competitor analysis

### AC-005.5: Competitor Policy Database
- Given the system needs competitor information
- When accessing data
- Then the system maintains a database of competitor policy information
- And information includes: policy names, coverage ranges, typical premiums, key features
- And information is updated regularly

## Detailed Scenarios

### Scenario 1: Direct Competitor Question
**Given**: Customer asks "How does your policy compare to Company X?"  
**When**: System responds  
**Then**: System provides accurate comparison, highlights company advantages, remains fair and factual

### Scenario 2: Proactive Mention for Strategic Advantage
**Given**: Customer mentions seeing better rates elsewhere  
**When**: System responds  
**Then**: System acknowledges competitor, explains company value proposition, addresses specific concern (rates) while highlighting other benefits

### Scenario 3: Limited Information
**Given**: Customer asks about niche competitor policy  
**When**: System doesn't have detailed information  
**Then**: System admits limitation, provides general comparison if possible, offers to connect with specialist

### Scenario 4: Unfair Comparison Request
**Given**: Customer asks system to criticize competitor  
**When**: System should respond  
**Then**: System declines to make negative claims, focuses on company strengths, maintains professional tone

## Technical Notes

- Competitor policy database schema
- Comparison algorithm/logic
- Update mechanism for competitor data
- Disclaimer handling for information accuracy
- Integration with company policy database for side-by-side comparison

## Related Requirements
- **FR-2.2.1**: Accurate competitor information
- **FR-2.2.2**: Fairness and accuracy
- **FR-2.2.3**: Strategic use of competitor info
- **FR-2.2.4**: On-demand provision
- **FR-2.2.5**: Limited information acknowledgment
- **FR-9.2**: Maintain competitor policy database

## Dependencies
- **Depends on**: US-004 (policy presentation)
- **Blocks**: US-006 (policy comparison)

## Story Points
**Estimate**: 5 points

## Priority
**Medium** - Important for competitive sales but secondary to company policy presentation

---

## Implementation Considerations

- Design competitor policy database similar to company policy database
- Define comparison framework and criteria
- Create comparison templates
- Implement fair comparison logic that highlights strengths without making false claims
- Consider data source for competitor information (manual entry, web scraping with disclaimers, partnerships)
- Legal/compliance review may be needed for competitive statements

