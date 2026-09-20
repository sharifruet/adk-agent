import { createContext, useContext, useState, useCallback } from 'react';
import { interactWithAgent, submitLead } from '../services/agentService';
import { classifyError } from '../utils/errorHandler';

const ConversationContext = createContext(null);

const USER_KEY = 'lic-agent-userId';
const SESSION_KEY = 'lic-agent-sessionId';

export const useConversation = () => {
  const context = useContext(ConversationContext);
  if (!context) {
    throw new Error('useConversation must be used within ConversationProvider');
  }
  return context;
};

export const ConversationProvider = ({ children }) => {
  const [messages, setMessages] = useState([]);
  const [userId, setUserId] = useState(() => localStorage.getItem(USER_KEY) || null);
  const [sessionId, setSessionId] = useState(() => localStorage.getItem(SESSION_KEY) || null);
  const [isLoading, setIsLoading] = useState(false);
  // { kind: 'network' | 'server' | 'client' | 'unknown', detail: string } | null
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
      localStorage.setItem(USER_KEY, id);
    }
  }, []);

  const updateSessionId = useCallback((id) => {
    setSessionId(id);
    if (id) {
      localStorage.setItem(SESSION_KEY, id);
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
      setError({ kind: classifyError(err), detail: err.message });
      console.error('Error sending message:', err);
    } finally {
      setIsLoading(false);
    }
  }, [userId, sessionId, updateUserId, updateSessionId]);

  // The form owns its own pending/success state, so this neither toggles
  // isLoading (which would show the typing dots) nor closes the form.
  const submitCustomerInfo = useCallback(async (info) => {
    if (!sessionId) {
      throw new Error('No active session');
    }

    const response = await submitLead(sessionId, info, userId);
    setCustomerInfo(info);
    return response;
  }, [sessionId, userId]);

  // Starts a fresh conversation. The backend session is dropped too, so the
  // agent does not carry memory of the cleared messages into the new chat.
  const clearConversation = useCallback(() => {
    setMessages([]);
    setError(null);
    setConversationState(null);
    setRequiresLeadCapture(false);
    setCustomerInfo({ name: '', phone: '', email: '' });
    setSessionId(null);
    localStorage.removeItem(SESSION_KEY);
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
