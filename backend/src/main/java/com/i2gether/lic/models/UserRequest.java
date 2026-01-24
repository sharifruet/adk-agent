package com.i2gether.lic.models;

import java.util.Map;
import java.util.UUID;

import org.checkerframework.checker.nullness.qual.Nullable;

public record UserRequest(
    @Nullable UUID userId,
    @Nullable UUID sessionId,
    String question,
    @Nullable Map<String, String> context, // For storing conversation context
    @Nullable CustomerInfo customerInfo // For submitting customer information
) {
    public UserRequest {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Question is required");
        }
    }
    
    // Convenience constructor for simple requests
    public UserRequest(@Nullable UUID userId, @Nullable UUID sessionId, String question) {
        this(userId, sessionId, question, null, null);
    }
}
