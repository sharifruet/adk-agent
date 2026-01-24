package com.i2gether.lic.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.i2gether.lic.models.CustomerInfo;
import com.i2gether.lic.models.Lead;
import com.i2gether.lic.services.LeadManagementService;

@RestController
@RequestMapping("/api/v1/agent")
public class LeadController {

    private final LeadManagementService leadManagementService;

    LeadController(LeadManagementService leadManagementService) {
        this.leadManagementService = leadManagementService;
    }

    /**
     * Submit customer information to create a lead
     */
    @PostMapping("/leads")
    public LeadResponse submitLead(@RequestBody LeadRequest request) {
        UUID userId = request.userId() != null ? request.userId() : UUID.randomUUID();
        
        // Create customer info from request
        CustomerInfo customerInfo = new CustomerInfo(
            request.name(),
            request.phone(),
            request.email(),
            null, // conversationSummary - will be generated from history
            null, // mentionedProducts
            null, // customerConcerns
            null  // additionalNotes
        );
        
        // Create lead with conversation history
        Lead lead = leadManagementService.createLead(
            request.sessionId(),
            userId,
            customerInfo
        );
        
        return new LeadResponse(
            lead.leadId(),
            "Lead created successfully. A human agent will contact you soon.",
            lead.status()
        );
    }

    /**
     * Get conversation history for a session
     */
    @GetMapping("/session/{sessionId}")
    public SessionResponse getSessionHistory(@PathVariable UUID sessionId) {
        List<String> history = leadManagementService.getConversationHistory(sessionId);
        return new SessionResponse(sessionId, history);
    }

    /**
     * Get all leads (for human agents/admin)
     */
    @GetMapping("/admin/leads")
    public List<Lead> getAllLeads() {
        return leadManagementService.getAllLeads();
    }

    /**
     * Get a specific lead by ID (for human agents/admin)
     */
    @GetMapping("/admin/leads/{leadId}")
    public Lead getLead(@PathVariable UUID leadId) {
        Lead lead = leadManagementService.getLead(leadId);
        if (lead == null) {
            throw new IllegalArgumentException("Lead not found: " + leadId);
        }
        return lead;
    }

    // Request/Response DTOs
    public record LeadRequest(
        UUID sessionId,
        UUID userId,
        String name,
        String phone,
        String email
    ) {}

    public record LeadResponse(
        UUID leadId,
        String message,
        String status
    ) {}

    public record SessionResponse(
        UUID sessionId,
        List<String> conversationHistory
    ) {}
}

