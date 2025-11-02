# US-006: Compare Policy Options

## User Story
As a **potential customer**
I want to **compare different policy options side-by-side**
So that **I can easily see differences and choose the best option for my needs**

## Acceptance Criteria

### AC-006.1: Comparison Request Handling
- Given customer wants to compare policies
- When customer requests comparison
- Then the system enables side-by-side comparison
- And the system can compare 2-4 policies at once
- And the system presents comparison in clear, structured format

### AC-006.2: Consistent Comparison Criteria
- Given policies are being compared
- When presenting comparison
- Then the system uses consistent criteria for all policies:
  - Coverage amounts and flexibility
  - Premium costs (with factors affecting cost)
  - Policy duration/term
  - Benefits and features
  - Eligibility requirements
  - Cash value/returns (if applicable)
- And all policies are evaluated on same criteria

### AC-006.3: Clear Difference Highlighting
- Given policies are being compared
- When showing differences
- Then the system highlights differences clearly
- And the system explains which policy might be better for specific needs
- And differences are explained in simple language

### AC-006.4: Recommendation Based on Comparison
- Given comparison is complete
- When presenting results
- Then the system provides recommendation based on comparison
- And the system explains reasoning for recommendation
- And recommendation considers customer's stated needs and profile

### AC-006.5: Company vs Competitor Comparison
- Given customer wants to compare company and competitor policies
- When performing comparison
- Then the system handles both company and competitor policies
- And the system maintains fairness in comparison
- And the system highlights company advantages where appropriate

### AC-006.6: Comparison Format
- Given policies are being compared
- When presenting comparison
- Then the system uses clear format (table, structured text, or visual)
- And the format is easy to understand
- And important differences stand out

## Detailed Scenarios

### Scenario 1: Compare 2 Company Policies
**Given**: Customer is considering Term Life vs Whole Life  
**When**: Customer requests comparison  
**Then**: System shows side-by-side comparison table with all criteria, highlights key differences, provides recommendation based on customer profile

### Scenario 2: Compare Company vs Competitor
**Given**: Customer wants to compare company policy with competitor  
**When**: Customer requests comparison  
**Then**: System provides fair comparison, highlights company advantages naturally, explains which might be better for customer's situation

### Scenario 3: Multiple Policy Comparison
**Given**: Customer wants to compare 3-4 policies  
**When**: Customer requests comparison  
**Then**: System presents all policies with consistent criteria, summarizes key differences, recommends top 1-2 options

### Scenario 4: Feature-Based Comparison
**Given**: Customer asks "What's the difference between these policies?"  
**When**: System responds  
**Then**: System focuses on differences most relevant to customer's needs, explains practical impact of differences

## Technical Notes

- Comparison engine/algorithm
- Comparison template/formatter
- Criteria standardization across policy types
- Recommendation algorithm based on customer profile + policy features
- Visual comparison support (if UI allows)

## Related Requirements
- **FR-2.4.1**: Side-by-side comparison (2-4 policies)
- **FR-2.4.2**: Consistent comparison criteria
- **FR-2.4.3**: Highlight differences and provide recommendations
- **FR-2.4.4**: Company vs competitor comparison

## Dependencies
- **Depends on**: US-004, US-005
- **Blocks**: US-010 (customer needs to select policy of interest)

## Story Points
**Estimate**: 6 points

## Priority
**Medium-High** - Helps customers make informed decisions

---

## Implementation Considerations

- Design comparison data structure
- Create comparison rendering logic (table generation, structured text)
- Define comparison criteria weights/scoring for recommendations
- Handle edge cases: comparing different policy types, missing data
- Consider UI component for visual comparison if web interface

