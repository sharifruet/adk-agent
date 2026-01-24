package com.i2gether.lic.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.i2gether.lic.models.InsuranceProduct;
import com.i2gether.lic.models.InsuranceProduct.ProductType;
import com.i2gether.lic.models.InsuranceProduct.PricingInfo;
import com.i2gether.lic.models.InsuranceProduct.EligibilityCriteria;

@Service
public class ProductService {

    private static final String KNOWLEDGE_BASE_PATH = "knowledgebase/";

    /**
     * Get all available insurance products
     */
    public List<InsuranceProduct> getAllProducts() {
        return List.of(
            getTermLifeInsurance(),
            getWholeLifeInsurance(),
            getUniversalLifeInsurance(),
            getVariableLifeInsurance(),
            getGroupLifeInsurance()
        );
    }

    /**
     * Get product by type
     */
    public InsuranceProduct getProductByType(ProductType productType) {
        return switch (productType) {
            case TERM -> getTermLifeInsurance();
            case WHOLE -> getWholeLifeInsurance();
            case UNIVERSAL -> getUniversalLifeInsurance();
            case VARIABLE -> getVariableLifeInsurance();
            case GROUP -> getGroupLifeInsurance();
            // JBC products - return null as they are primarily in knowledge base files
            case TERM_BENEFIT_PENSION, THREE_INSTALLMENT, MULTIPLE_INSTALLMENT,
                 JBC_MONTHLY_SAVINGS, JBC_EXPECTED_MONTHLY_SAVINGS, SOCIAL_SECURITY,
                 PROMILA_DPS, RURAL_LIFE, PERSONAL_PENSION, CHILD_SECURITY -> null;
        };
    }

    /**
     * Search products by keyword
     */
    public List<InsuranceProduct> searchProducts(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return getAllProducts().stream()
            .filter(product -> 
                product.productName().toLowerCase().contains(lowerKeyword) ||
                product.description().toLowerCase().contains(lowerKeyword) ||
                product.productType().name().toLowerCase().contains(lowerKeyword)
            )
            .collect(Collectors.toList());
    }

    /**
     * Get product knowledge base content by file name
     */
    public String getProductKnowledgeBase(String fileName) {
        try {
            Resource resource = new ClassPathResource(KNOWLEDGE_BASE_PATH + fileName);
            if (resource.exists()) {
                return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            }
            return "Product information not available.";
        } catch (IOException e) {
            return "Product information not available.";
        }
    }

    /**
     * Get product knowledge base content by product type
     */
    public String getProductKnowledgeBase(ProductType productType) {
        String fileName = switch (productType) {
            case TERM -> "term-life-insurance.md";
            case WHOLE -> "whole-life-insurance.md";
            case UNIVERSAL -> "universal-life-insurance.md";
            case VARIABLE -> "variable-life-insurance.md";
            case GROUP -> "group-life-insurance.md";
            case TERM_BENEFIT_PENSION -> "term-benefit-pension-insurance.md";
            case THREE_INSTALLMENT -> "three-installment-insurance.md";
            case MULTIPLE_INSTALLMENT -> "multiple-installment-insurance.md";
            case JBC_MONTHLY_SAVINGS -> "jbc-monthly-savings-scheme.md";
            case JBC_EXPECTED_MONTHLY_SAVINGS -> "jbc-expected-monthly-savings.md";
            case SOCIAL_SECURITY -> "social-security-insurance.md";
            case PROMILA_DPS -> "promila-dps-scheme.md";
            case RURAL_LIFE -> "rural-life-insurance.md";
            case PERSONAL_PENSION -> "personal-pension-insurance.md";
            case CHILD_SECURITY -> "child-security-insurance.md";
        };
        
        return getProductKnowledgeBase(fileName);
    }

    /**
     * Get all product knowledge base content (for agent context)
     * Loads all markdown files from the knowledgebase directory
     */
    public String getAllProductKnowledgeBase() {
        StringBuilder knowledgeBase = new StringBuilder();
        knowledgeBase.append("# জীবন বীমা কর্পোরেশন - Insurance Products Knowledge Base\n\n");
        knowledgeBase.append("This knowledge base contains information about all available insurance products from Jiban Bima Corporation (জীবন বীমা কর্পোরেশন).\n\n");
        
        // List of all knowledge base files (in order of importance)
        String[] knowledgeBaseFiles = {
            "term-benefit-pension-insurance.md",
            "three-installment-insurance.md",
            "multiple-installment-insurance.md",
            "jbc-monthly-savings-scheme.md",
            "jbc-expected-monthly-savings.md",
            "social-security-insurance.md",
            "promila-dps-scheme.md",
            "rural-life-insurance.md",
            "personal-pension-insurance.md",
            "child-security-insurance.md",
            // Keep old products for backward compatibility
            "term-life-insurance.md",
            "whole-life-insurance.md",
            "universal-life-insurance.md",
            "variable-life-insurance.md",
            "group-life-insurance.md"
        };
        
        for (String fileName : knowledgeBaseFiles) {
            String content = getProductKnowledgeBase(fileName);
            if (content != null && !content.equals("Product information not available.")) {
                knowledgeBase.append(content);
                knowledgeBase.append("\n\n---\n\n");
            }
        }
        
        return knowledgeBase.toString();
    }

    // Product definitions
    private InsuranceProduct getTermLifeInsurance() {
        return new InsuranceProduct(
            "term-life-001",
            "Term Life Insurance",
            ProductType.TERM,
            "Affordable life insurance coverage for a specific period, typically 10, 20, or 30 years. Provides financial protection with fixed premiums during the term.",
            Arrays.asList(
                "Fixed premiums during term",
                "Temporary coverage (10, 20, or 30 years)",
                "Affordable rates",
                "No cash value accumulation",
                "Convertible to permanent insurance"
            ),
            Arrays.asList(
                "Financial protection for dependents",
                "Budget-friendly option",
                "Maximum coverage for minimum cost",
                "Simple and straightforward",
                "Flexible coverage amounts"
            ),
            new PricingInfo(
                "Based on age, health, coverage amount, and term length",
                "Monthly, quarterly, or annual",
                "Younger, healthier individuals pay lower premiums"
            ),
            new EligibilityCriteria(
                18,
                75,
                "Medical exam may be required for larger coverage amounts",
                "Must demonstrate ability to pay premiums"
            ),
            Arrays.asList(
                "What happens when the term ends?",
                "Can I cancel my policy?",
                "Is term life insurance worth it?",
                "What if I outlive the term?"
            )
        );
    }

    private InsuranceProduct getWholeLifeInsurance() {
        return new InsuranceProduct(
            "whole-life-001",
            "Whole Life Insurance",
            ProductType.WHOLE,
            "Permanent life insurance that provides lifetime coverage with fixed premiums and cash value accumulation. Combines death benefit protection with a savings component.",
            Arrays.asList(
                "Permanent lifetime coverage",
                "Fixed premiums for life",
                "Cash value accumulation",
                "Guaranteed death benefit",
                "Potential dividends"
            ),
            Arrays.asList(
                "Lifetime protection",
                "Cash value growth",
                "Tax advantages",
                "Estate planning tool",
                "Stable and predictable"
            ),
            new PricingInfo(
                "Higher than term insurance, fixed for life",
                "Monthly, quarterly, or annual",
                "Premium is 5-10 times higher than term insurance"
            ),
            new EligibilityCriteria(
                0,
                85,
                "Medical exam usually required",
                "Must demonstrate ability to pay higher premiums and financial stability"
            ),
            Arrays.asList(
                "How does cash value work?",
                "Can I stop paying premiums?",
                "Is whole life a good investment?",
                "What happens to cash value when I die?"
            )
        );
    }

    private InsuranceProduct getUniversalLifeInsurance() {
        return new InsuranceProduct(
            "universal-life-001",
            "Universal Life Insurance",
            ProductType.UNIVERSAL,
            "Flexible permanent life insurance with adjustable premiums and death benefits. Offers cash value growth with interest-based returns.",
            Arrays.asList(
                "Flexible premium payments",
                "Adjustable death benefit",
                "Cash value with interest growth",
                "Permanent coverage",
                "Transparent policy structure"
            ),
            Arrays.asList(
                "Premium flexibility",
                "Cash value growth potential",
                "Tax advantages",
                "Lifetime coverage",
                "Control over policy"
            ),
            new PricingInfo(
                "Minimum premium required, can pay more to build cash value",
                "Flexible payment schedule",
                "Premiums can be adjusted based on financial situation"
            ),
            new EligibilityCriteria(
                18,
                80,
                "Medical exam usually required",
                "Must demonstrate ability to pay premiums and understand policy mechanics"
            ),
            Arrays.asList(
                "How flexible are the premiums?",
                "What happens if I don't pay enough?",
                "Can I change the death benefit?",
                "How does interest work?"
            )
        );
    }

    private InsuranceProduct getVariableLifeInsurance() {
        return new InsuranceProduct(
            "variable-life-001",
            "Variable Life Insurance",
            ProductType.VARIABLE,
            "Permanent life insurance that combines death benefit protection with investment options. Cash value can be invested in various sub-accounts for potential growth.",
            Arrays.asList(
                "Investment component",
                "Market-linked returns",
                "Permanent coverage",
                "Flexible premiums",
                "Investment control"
            ),
            Arrays.asList(
                "Growth potential",
                "Investment control",
                "Tax advantages",
                "Lifetime coverage",
                "Wealth building tool"
            ),
            new PricingInfo(
                "Higher premiums, minimum required to maintain coverage",
                "Flexible payment schedule",
                "Can pay additional premiums to invest more"
            ),
            new EligibilityCriteria(
                18,
                75,
                "Medical exam required",
                "Must demonstrate ability to pay premiums, risk tolerance, and investment knowledge"
            ),
            Arrays.asList(
                "Can I lose money with variable life insurance?",
                "How do I choose investments?",
                "Is this a good investment?",
                "What happens if investments perform poorly?"
            )
        );
    }

    private InsuranceProduct getGroupLifeInsurance() {
        return new InsuranceProduct(
            "group-life-001",
            "Group Life Insurance",
            ProductType.GROUP,
            "Life insurance coverage provided by an employer or organization. Offers affordable group rates and easy enrollment, typically with basic coverage amounts.",
            Arrays.asList(
                "Employer-sponsored",
                "Affordable group rates",
                "Easy enrollment",
                "Basic coverage (1-2x salary)",
                "May include portability options"
            ),
            Arrays.asList(
                "Low cost",
                "No medical exam typically",
                "Convenient enrollment",
                "Immediate coverage",
                "Supplemental options available"
            ),
            new PricingInfo(
                "Group rates, often employer pays basic coverage",
                "Payroll deduction",
                "Very affordable due to group rates"
            ),
            new EligibilityCriteria(
                18,
                null, // Varies by employer
                "Usually no health questions for basic coverage",
                "Must be employee of participating organization"
            ),
            Arrays.asList(
                "What happens if I leave my job?",
                "Is group life insurance enough?",
                "Can I keep my group policy?",
                "How much does it cost?"
            )
        );
    }
}

