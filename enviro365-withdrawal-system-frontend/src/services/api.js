import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api/v1';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  },
  timeout: 30000,
});

// Request interceptor - add session ID to every request
api.interceptors.request.use(
  (config) => {
    console.log(`Making ${config.method.toUpperCase()} request to ${config.url}`);
    
    // Get session from localStorage
    const session = localStorage.getItem('session');
    if (session) {
      try {
        const sessionData = JSON.parse(session);
        if (sessionData.sessionId) {
          // Add session ID to headers
          config.headers['X-Session-Id'] = sessionData.sessionId;
          console.log('Added session ID to request:', sessionData.sessionId);
        }
      } catch (e) {
        console.error('Error parsing session:', e);
      }
    } else {
      console.warn('No session found in localStorage');
    }
    
    return config;
  },
  (error) => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    if (error.response) {
      // If unauthorized, clear session
      if (error.response.status === 401) {
        console.log('Unauthorized - clearing session');
        localStorage.removeItem('session');
        delete api.defaults.headers.common['X-Session-Id'];
        // Redirect to login if not already there
        if (window.location.pathname !== '/') {
          window.location.href = '/';
        }
      }
      const errorMessage = error.response.data?.message || 'An error occurred';
      console.error('API Error:', errorMessage);
      return Promise.reject(new Error(errorMessage));
    } else if (error.request) {
      console.error('No response from server');
      return Promise.reject(new Error('Unable to connect to server. Please check your connection.'));
    } else {
      console.error('Error:', error.message);
      return Promise.reject(error);
    }
  }
);

export default api;