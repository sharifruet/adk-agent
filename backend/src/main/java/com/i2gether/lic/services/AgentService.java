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
            .blockingForEach(event -> {
                String content = event.stringifyContent();
                if (content != null && !content.isBlank()) {
                    answerBuilder.append(content);
                }
            });

        String answer = answerBuilder.toString();
        
        // Store agent response in conversation history
        leadManagementService.addConversationMessage(sessionId, "Agent: " + answer);

        // Detect interest signals and determine if lead capture is needed
        boolean requiresLeadCapture = detectInterestSignal(request.question(), answer);
        
        // Determine conversation state based on content
        ConversationState conversationState = determineConversationState(request.question(), answer);

        return new UserResponse(userId, sessionId, answer, conversationState, requiresLeadCapture);
    }

    /**
     * Detect if customer is showing CLEAR PURCHASE INTENT and lead capture should be initiated.
     * Only triggers on explicit purchase/subscription intent, not general information requests.
     */
    private boolean detectInterestSignal(String userQuestion, String agentAnswer) {
        String userLower = userQuestion.toLowerCase(Locale.ROOT);
        String agentLower = agentAnswer.toLowerCase(Locale.ROOT);
        String combined = (userQuestion + " " + agentAnswer).toLowerCase(Locale.ROOT);
        
        // STRONG purchase intent signals - only these should trigger lead capture
        String[] strongPurchaseIntent = {
            "i want to sign up",
            "i want to apply",
            "i want to purchase",
            "i want to buy",
            "i want to subscribe",
            "i'd like to sign up",
            "i'd like to apply",
            "i'd like to purchase",
            "i'd like to buy",
            "i'd like to subscribe",
            "i would like to sign up",
            "i would like to apply",
            "i would like to purchase",
            "i would like to buy",
            "i would like to subscribe",
            "sign me up",
            "i'm ready to apply",
            "i'm ready to purchase",
            "i'm ready to buy",
            "i'm ready to sign up",
            "i'm ready to subscribe",
            "let's proceed",
            "let's move forward",
            "i'll take it",
            "i'll take this",
            "i want this policy",
            "i want this insurance",
            "how do i apply",
            "how do i sign up",
            "how do i purchase",
            "how do i buy",
            "how can i apply",
            "how can i sign up",
            "how can i purchase",
            "how can i buy",
            "i'm ready",
            "ready to apply",
            "ready to purchase",
            "ready to buy",
            "ready to sign up"
        };
        
        // Check for strong purchase intent in user's question
        for (String intent : strongPurchaseIntent) {
            if (userLower.contains(intent)) {
                return true;
            }
        }
        
        // Check if agent is explicitly asking for contact information (only if user showed purchase intent first)
        // This is a secondary check - agent should only ask after user shows intent
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
        
        // Only trigger if agent is asking AND user showed some purchase intent
        boolean agentAsking = false;
        for (String keyword : leadCaptureKeywords) {
            if (agentLower.contains(keyword)) {
                agentAsking = true;
                break;
            }
        }
        
        // If agent is asking, check if user showed any purchase intent
        if (agentAsking) {
            String[] anyPurchaseHint = {
                "sign up", "apply", "purchase", "buy", "subscribe", "get started", 
                "proceed", "move forward", "ready", "interested in getting"
            };
            for (String hint : anyPurchaseHint) {
                if (userLower.contains(hint)) {
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

