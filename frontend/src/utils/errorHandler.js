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

