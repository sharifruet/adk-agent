/**
 * Utility functions for error handling
 */

export const handleApiError = (error) => {
  if (error.response) {
    // Server responded with error status
    const status = error.response.status;
    const message = error.response.data?.message || error.response.data?.error || 'An error occurred';

    switch (status) {
      case 400:
        return `Bad Request: ${message}`;
      case 401:
        return 'Unauthorized. Please check your credentials.';
      case 403:
        return 'Forbidden. You do not have permission.';
      case 404:
        return 'Not Found. The requested resource was not found.';
      case 500:
        return `Server Error: ${message}`;
      default:
        return `Error ${status}: ${message}`;
    }
  } else if (error.request) {
    // Request was made but no response received
    return 'Network error. Please check your connection and try again.';
  } else {
    // Something else happened
    return error.message || 'An unexpected error occurred';
  }
};

export const isNetworkError = (error) => {
  return !error.response && error.request;
};

export const isServerError = (error) => {
  return error.response && error.response.status >= 500;
};

export const isClientError = (error) => {
  return error.response && error.response.status >= 400 && error.response.status < 500;
};

/**
 * Coarse category the UI can translate: 'network' | 'server' | 'client' | 'unknown'.
 * Accepts either a raw axios error or one of the enhanced errors thrown by
 * agentService (which carry the axios error as `originalError`).
 */
export const classifyError = (error) => {
  const source = error?.originalError || error;
  if (!source) return 'unknown';
  if (isNetworkError(source)) return 'network';
  if (isServerError(source)) return 'server';
  if (isClientError(source)) return 'client';
  return 'unknown';
};
