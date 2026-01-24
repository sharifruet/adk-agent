package com.i2gether.lic.models;

import java.util.List;

public record InsuranceProduct(
    String productId,
    String productName,
    ProductType productType,
    String description,
    List<String> features,
    List<String> benefits,
    PricingInfo pricing,
    EligibilityCriteria eligibility,
    List<String> faqs
) {
    public enum ProductType {
        TERM,
        WHOLE,
        UNIVERSAL,
        VARIABLE,
        GROUP,
        TERM_BENEFIT_PENSION,
        THREE_INSTALLMENT,
        MULTIPLE_INSTALLMENT,
        JBC_MONTHLY_SAVINGS,
        JBC_EXPECTED_MONTHLY_SAVINGS,
        SOCIAL_SECURITY,
        PROMILA_DPS,
        RURAL_LIFE,
        PERSONAL_PENSION,
        CHILD_SECURITY
    }
    
    public record PricingInfo(
        String premiumRange,
        String paymentFrequency,
        String notes
    ) {}
    
    public record EligibilityCriteria(
        Integer minAge,
        Integer maxAge,
        String healthRequirements,
        String otherRequirements
    ) {}
}

