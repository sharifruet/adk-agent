package com.i2gether.lic.models;

import java.util.UUID;

import org.checkerframework.checker.nullness.qual.Nullable;

public record UserResponse(
    UUID userId,
    UUID sessionId,
    String answer,
    @Nullable ConversationState conversationState,
    @Nullable Boolean requiresLeadCapture // Indicates if agent is asking for customer info
) {
    public UserResponse {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (sessionId == null) {
            throw new IllegalArgumentException("Session ID is required");
        }
        if (answer == null) {
            answer = "";
        }
    }
    
    // Convenience constructor for simple responses
    public UserResponse(UUID userId, UUID sessionId, String answer) {
        this(userId, sessionId, answer, null, null);
    }
}
