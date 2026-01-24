package com.i2gether.lic.models;

import java.time.LocalDateTime;
import java.util.UUID;

public record Lead(
    UUID leadId,
    UUID sessionId,
    UUID userId,
    CustomerInfo customerInfo,
    String fullConversationHistory, // Complete conversation for human agent review
    LocalDateTime createdAt,
    String status, // NEW, PENDING_CONTACT, CONTACTED, etc.
    String notes // For human sales agent to add notes
) {
    public Lead {
        if (leadId == null) {
            throw new IllegalArgumentException("Lead ID is required");
        }
        if (sessionId == null) {
            throw new IllegalArgumentException("Session ID is required");
        }
        if (customerInfo == null) {
            throw new IllegalArgumentException("Customer info is required");
        }
        if (status == null || status.isBlank()) {
            status = "NEW";
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    public Lead withStatus(String newStatus) {
        return new Lead(leadId, sessionId, userId, customerInfo, fullConversationHistory, createdAt, newStatus, notes);
    }
    
    public Lead withNotes(String newNotes) {
        return new Lead(leadId, sessionId, userId, customerInfo, fullConversationHistory, createdAt, status, newNotes);
    }
}

