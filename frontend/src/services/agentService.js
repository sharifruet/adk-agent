import axios from 'axios';
import { handleApiError } from '../utils/errorHandler';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 30000, // 30 second timeout
});

/**
 * Send a message to the agent and get a response
 */
export const interactWithAgent = async (question, userId = null, sessionId = null) => {
  try {
    const response = await api.post('/api/v1/agent/interact', {
      question,
      userId,
      sessionId,
    });
    return response.data;
  } catch (error) {
    console.error('Error interacting with agent:', error);
    const errorMessage = handleApiError(error);
    const enhancedError = new Error(errorMessage);
    enhancedError.originalError = error;
    throw enhancedError;
  }
};

/**
 * Submit customer information to create a lead
 */
export const submitLead = async (sessionId, customerInfo, userId = null) => {
  try {
    const response = await api.post('/api/v1/agent/leads', {
      sessionId,
      userId,
      name: customerInfo.name,
      phone: customerInfo.phone,
      email: customerInfo.email,
    });
    return response.data;
  } catch (error) {
    console.error('Error submitting lead:', error);
    const errorMessage = handleApiError(error);
    const enhancedError = new Error(errorMessage);
    enhancedError.originalError = error;
    throw enhancedError;
  }
};

/**
 * Get conversation history for a session
 */
export const getSessionHistory = async (sessionId) => {
  try {
    const response = await api.get(`/api/v1/agent/session/${sessionId}`);
    return response.data;
  } catch (error) {
    console.error('Error getting session history:', error);
    const errorMessage = handleApiError(error);
    const enhancedError = new Error(errorMessage);
    enhancedError.originalError = error;
    throw enhancedError;
  }
};

/**
 * Get all insurance products
 */
export const getProducts = async () => {
  try {
    const response = await api.get('/api/v1/agent/products');
    return response.data;
  } catch (error) {
    console.error('Error getting products:', error);
    throw error;
  }
};

/**
 * Get a specific product by type
 */
export const getProductByType = async (productType) => {
  try {
    const response = await api.get(`/api/v1/agent/products/${productType}`);
    return response.data;
  } catch (error) {
    console.error('Error getting product:', error);
    throw error;
  }
};

export default {
  interactWithAgent,
  submitLead,
  getSessionHistory,
  getProducts,
  getProductByType,
};

