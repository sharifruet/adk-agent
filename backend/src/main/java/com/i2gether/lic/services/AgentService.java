package com.i2gether.lic.services;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.google.adk.agents.BaseAgent;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.i2gether.lic.models.ConversationState;
import com.i2gether.lic.models.UserRequest;
import com.i2gether.lic.models.UserResponse;

@Service
public class AgentService {

    private final InMemoryRunner runner;
    private final ConcurrentMap<String, Session> inMemorySessionCache = new ConcurrentHashMap<>();
    private final LeadManagementService leadManagementService;

    AgentService(BaseAgent baseAgent, LeadManagementService leadManagementService) {
        this.runner = new InMemoryRunner(baseAgent);
        this.leadManagementService = leadManagementService;
    }

    public UserResponse interact(UserRequest request) {
        UUID userId = request.userId() != null ? request.userId() : UUID.randomUUID();
        UUID sessionId = request.sessionId() != null ? request.sessionId() : UUID.randomUUID();

        // Store user message in conversation history
        leadManagementService.addConversationMessage(sessionId, "User: " + request.question());

        String cacheKey = userId + ":" + sessionId;
        Session session = inMemorySessionCache.computeIfAbsent(cacheKey, key ->
            runner.sessionService()
                .createSession(runner.appName(), userId.toString(), null, sessionId.toString())
                .blockingGet()
        );

        Content userMessage = Content.fromParts(Part.fromText(request.question()));
        StringBuilder answerBuilder = new StringBuilder();
        runner.runAsync(userId.toString(), session.id(), userMessage)
            .blockingForEach(event -> event.content().ifPresent(content -> {
                String text = content.text();
                if (text != null && !text.isBlank()) {
                    answerBuilder.append(text);
                }
            }));

        String answer = answerBuilder.toString();
        
        // Store agent response in conversation history
        leadManagementService.addConversationMessage(sessionId, "Agent: " + answer);

        // Detect interest signals and determine if lead capture is needed
        boolean requiresLeadCapture = detectInterestSignal(sessionId, answer);
        
        // Determine conversation state based on content
        ConversationState conversationState = determineConversationState(request.question(), answer);

        return new UserResponse(userId, sessionId, answer, conversationState, requiresLeadCapture);
    }

    /**
     * Detect if lead capture should be initiated.
     * Triggers only when the agent is asking for contact information in this reply AND the
     * customer has shown purchase intent somewhere in the session. The agent's ask is required
     * because intent alone ("I'll take this policy") may refer to no product yet, in which case
     * the agent asks which product is meant instead of collecting contact details.
     */
    private boolean detectInterestSignal(UUID sessionId, String agentAnswer) {
        String agentLower = agentAnswer.toLowerCase(Locale.ROOT);

        // Check if agent is explicitly asking for contact information
        String[] leadCaptureKeywords = {
            "could i get your name",
            "can i get your name",
            "may i have your name",
            "could i get your phone",
            "can i get your phone",
            "may i have your phone",
            "could i get your email",
            "can i get your email",
            "may i have your email",
            "contact information",
            "phone number",
            "email address"
        };
        
        boolean agentAsking = false;
        for (String keyword : leadCaptureKeywords) {
            if (agentLower.contains(keyword)) {
                agentAsking = true;
                break;
            }
        }
        if (!agentAsking) {
            return false;
        }

        // Check if user showed purchase intent in any message of this session, since the
        // product may have been clarified in a later message than the one expressing intent
        String[] purchaseIntent = {
            "sign up", "sign me up", "apply", "purchase", "buy", "subscribe", "get started",
            "proceed", "move forward", "ready", "interested in getting",
            "i'll take", "i will take", "i want this policy", "i want this insurance"
        };
        for (String message : leadManagementService.getConversationHistory(sessionId)) {
            if (!message.startsWith("User: ")) {
                continue;
            }
            String userLower = message.toLowerCase(Locale.ROOT);
            for (String intent : purchaseIntent) {
                if (userLower.contains(intent)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Determine conversation state based on content
     */
    private ConversationState determineConversationState(String userQuestion, String agentAnswer) {
        String combined = (userQuestion + " " + agentAnswer).toLowerCase(Locale.ROOT);
        
        // Check for greeting patterns
        if (combined.contains("hello") || combined.contains("hi") || 
            combined.contains("greeting") || combined.contains("welcome")) {
            return ConversationState.GREETING;
        }
        
        // Check for needs assessment
        if (combined.contains("need") || combined.contains("looking for") ||
            combined.contains("situation") || combined.contains("family") ||
            combined.contains("dependents") || combined.contains("budget")) {
            return ConversationState.NEEDS_ASSESSMENT;
        }
        
        // Check for product recommendation
        if (combined.contains("term life") || combined.contains("whole life") ||
            combined.contains("universal life") || combined.contains("variable life") ||
            combined.contains("recommend") || combined.contains("suitable") ||
            combined.contains("product") || combined.contains("policy")) {
            return ConversationState.PRODUCT_RECOMMENDATION;
        }
        
        // Check for lead capture
        if (combined.contains("name") || combined.contains("phone") ||
            combined.contains("email") || combined.contains("contact")) {
            return ConversationState.LEAD_CAPTURE;
        }
        
        // Default to needs assessment if unclear
        return ConversationState.NEEDS_ASSESSMENT;
    }
}

