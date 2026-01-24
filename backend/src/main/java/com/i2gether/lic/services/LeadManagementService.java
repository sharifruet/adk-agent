package com.i2gether.lic.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.i2gether.lic.models.CustomerInfo;
import com.i2gether.lic.models.Lead;

@Service
public class LeadManagementService {

    private final Map<UUID, Lead> leads = new ConcurrentHashMap<>();
    private final Map<UUID, List<String>> conversationHistory = new ConcurrentHashMap<>();

    /**
     * Store a conversation message for a session
     */
    public void addConversationMessage(UUID sessionId, String message) {
        conversationHistory.computeIfAbsent(sessionId, k -> new ArrayList<>()).add(message);
    }

    /**
     * Get full conversation history for a session
     */
    public List<String> getConversationHistory(UUID sessionId) {
        return conversationHistory.getOrDefault(sessionId, new ArrayList<>());
    }

    /**
     * Create a lead from customer information and conversation history
     */
    public Lead createLead(UUID sessionId, UUID userId, CustomerInfo customerInfo) {
        UUID leadId = UUID.randomUUID();
        List<String> history = getConversationHistory(sessionId);
        String fullConversation = String.join("\n", history);
        
        Lead lead = new Lead(
            leadId,
            sessionId,
            userId,
            customerInfo,
            fullConversation,
            LocalDateTime.now(),
            "NEW",
            null
        );
        
        leads.put(leadId, lead);
        return lead;
    }

    /**
     * Get a lead by ID
     */
    public Lead getLead(UUID leadId) {
        return leads.get(leadId);
    }

    /**
     * Get all leads (for human agent review)
     */
    public List<Lead> getAllLeads() {
        return new ArrayList<>(leads.values());
    }

    /**
     * Get leads by status
     */
    public List<Lead> getLeadsByStatus(String status) {
        return leads.values().stream()
            .filter(lead -> status.equals(lead.status()))
            .toList();
    }

    /**
     * Update lead status (for human agents)
     */
    public Lead updateLeadStatus(UUID leadId, String newStatus) {
        Lead lead = leads.get(leadId);
        if (lead == null) {
            throw new IllegalArgumentException("Lead not found: " + leadId);
        }
        Lead updatedLead = lead.withStatus(newStatus);
        leads.put(leadId, updatedLead);
        return updatedLead;
    }

    /**
     * Add notes to a lead (for human agents)
     */
    public Lead addNotesToLead(UUID leadId, String notes) {
        Lead lead = leads.get(leadId);
        if (lead == null) {
            throw new IllegalArgumentException("Lead not found: " + leadId);
        }
        String updatedNotes = lead.notes() == null ? notes : lead.notes() + "\n" + notes;
        Lead updatedLead = lead.withNotes(updatedNotes);
        leads.put(leadId, updatedLead);
        return updatedLead;
    }
}

