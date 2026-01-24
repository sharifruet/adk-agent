package com.i2gether.lic.models;

import java.util.List;
import java.util.Map;

public record CustomerInfo(
    String fullName,
    String phoneNumber,
    String email,
    // Additional context from conversation
    String conversationSummary,
    List<String> mentionedProducts,
    List<String> customerConcerns,
    Map<String, String> additionalNotes
) {
    public CustomerInfo {
        // Validate required fields are not null
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
    }
}

