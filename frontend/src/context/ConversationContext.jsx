import { createContext, useContext, useState, useCallback } from 'react';
import { interactWithAgent, submitLead } from '../services/agentService';

const ConversationContext = createContext(null);

export const useConversation = () => {
  const context = useContext(ConversationContext);
  if (!context) {
    throw new Error('useConversation must be used within ConversationProvider');
  }
  return context;
};

export const ConversationProvider = ({ children }) => {
  const [messages, setMessages] = useState([]);
  const [userId, setUserId] = useState(() => {
    // Try to restore from localStorage
    const saved = localStorage.getItem('lic-agent-userId');
    return saved || null;
  });
  const [sessionId, setSessionId] = useState(() => {
    // Try to restore from localStorage
    const saved = localStorage.getItem('lic-agent-sessionId');
    return saved || null;
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [conversationState, setConversationState] = useState(null);
  const [requiresLeadCapture, setRequiresLeadCapture] = useState(false);
  const [customerInfo, setCustomerInfo] = useState({
    name: '',
    phone: '',
    email: '',
  });

  // Save to localStorage when userId or sessionId changes
  const updateUserId = useCallback((id) => {
    setUserId(id);
    if (id) {
      localStorage.setItem('lic-agent-userId', id);
    }
  }, []);

  const updateSessionId = useCallback((id) => {
    setSessionId(id);
    if (id) {
      localStorage.setItem('lic-agent-sessionId', id);
    }
  }, []);

  const sendMessage = useCallback(async (question) => {
    if (!question.trim()) return;

    // Add user message to UI immediately
    const userMessage = {
      id: Date.now(),
      type: 'user',
      content: question,
      timestamp: new Date(),
    };
    setMessages((prev) => [...prev, userMessage]);
    setIsLoading(true);
    setError(null);

    try {
      const response = await interactWithAgent(question, userId, sessionId);

      // Update userId and sessionId from response
      if (response.userId && response.userId !== userId) {
        updateUserId(response.userId);
      }
      if (response.sessionId && response.sessionId !== sessionId) {
        updateSessionId(response.sessionId);
      }

      // Add agent response
      const agentMessage = {
        id: Date.now() + 1,
        type: 'agent',
        content: response.answer,
        timestamp: new Date(),
      };
      setMessages((prev) => [...prev, agentMessage]);

      // Update conversation state
      if (response.conversationState) {
        setConversationState(response.conversationState);
      }

      // Check if lead capture is required (handle both boolean and string responses)
      if (response.requiresLeadCapture === true || response.requiresLeadCapture === 'true') {
        setRequiresLeadCapture(true);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to send message. Please try again.');
      console.error('Error sending message:', err);
    } finally {
      setIsLoading(false);
    }
  }, [userId, sessionId, updateUserId, updateSessionId]);

  const submitCustomerInfo = useCallback(async (info) => {
    if (!sessionId) {
      throw new Error('No active session');
    }

    setIsLoading(true);
    setError(null);

    try {
      const response = await submitLead(sessionId, info, userId);
      setCustomerInfo(info);
      setRequiresLeadCapture(false);
      return response;
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit information. Please try again.');
      throw err;
    } finally {
      setIsLoading(false);
    }
  }, [sessionId, userId]);

  const clearConversation = useCallback(() => {
    setMessages([]);
    setError(null);
    setConversationState(null);
    setRequiresLeadCapture(false);
    setCustomerInfo({ name: '', phone: '', email: '' });
    // Optionally clear session
    // setSessionId(null);
    // setUserId(null);
    // localStorage.removeItem('lic-agent-sessionId');
    // localStorage.removeItem('lic-agent-userId');
  }, []);

  const value = {
    messages,
    userId,
    sessionId,
    isLoading,
    error,
    conversationState,
    requiresLeadCapture,
    customerInfo,
    sendMessage,
    submitCustomerInfo,
    clearConversation,
    setRequiresLeadCapture,
  };

  return (
    <ConversationContext.Provider value={value}>
      {children}
    </ConversationContext.Provider>
  );
};

