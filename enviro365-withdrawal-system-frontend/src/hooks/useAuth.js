import { useState, createContext, useContext, useCallback, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);
  const [userRole, setUserRole] = useState(null);
  const [investorId, setInvestorId] = useState(null);
  const [userId, setUserId] = useState(null);
  const [sessionId, setSessionId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Check existing session on mount
  useEffect(() => {
    const savedSession = localStorage.getItem('session');
    if (savedSession) {
      try {
        const sessionData = JSON.parse(savedSession);
        console.log('Session found in localStorage:', sessionData);
        
        // Set session in API headers
        api.defaults.headers.common['X-Session-Id'] = sessionData.sessionId;
        
        // Validate session with backend
        api.get('/auth/user-info')
          .then(response => {
            if (response.success) {
              const userData = response.data;
              setUser(userData);
              setUserRole(userData.role);
              setInvestorId(userData.investorId);
              setUserId(userData.userId);
              setSessionId(sessionData.sessionId);
              setIsAuthenticated(true);
              console.log('Session validated successfully');
            } else {
              // Session invalid
              localStorage.removeItem('session');
              delete api.defaults.headers.common['X-Session-Id'];
              console.log('Session invalid, cleared from localStorage');
            }
          })
          .catch(() => {
            localStorage.removeItem('session');
            delete api.defaults.headers.common['X-Session-Id'];
            console.log('Session validation failed, cleared from localStorage');
          });
      } catch (e) {
        console.error('Error parsing session:', e);
        localStorage.removeItem('session');
      }
    }
  }, []);

  const login = useCallback(async (username, password) => {
    setLoading(true);
    setError(null);
    
    console.log('Attempting login for user:', username);
    
    try {
      const response = await api.post('/auth/login', {
        username,
        password
      });
      
      if (!response.success) {
        throw new Error(response.message || 'Login failed');
      }
      
      const userData = response.data;
      console.log('Login successful:', userData);
      
      // Store user info
      setUser(userData);
      setUserRole(userData.role);
      setInvestorId(userData.investorId);
      setUserId(userData.userId);
      setSessionId(userData.sessionId);
      setIsAuthenticated(true);
      
      // Store session in localStorage
      localStorage.setItem('session', JSON.stringify({
        sessionId: userData.sessionId,
        userId: userData.userId,
        username: userData.username,
        role: userData.role
      }));
      
      // Set session header for future requests
      api.defaults.headers.common['X-Session-Id'] = userData.sessionId;
      console.log('Session ID set in headers:', userData.sessionId);
      
      return true;
    } catch (err) {
      console.error('Login failed:', err);
      setError(err.message || 'Invalid username or password');
      setIsAuthenticated(false);
      setUser(null);
      setUserRole(null);
      setInvestorId(null);
      setUserId(null);
      setSessionId(null);
      localStorage.removeItem('session');
      delete api.defaults.headers.common['X-Session-Id'];
      return false;
    } finally {
      setLoading(false);
    }
  }, []);

  const logout = useCallback(async () => {
    console.log('Logging out...');
    
    try {
      if (sessionId) {
        await api.post('/auth/logout', {}, {
          headers: {
            'X-Session-Id': sessionId
          }
        });
      }
    } catch (e) {
      console.error('Logout error:', e);
    } finally {
      setIsAuthenticated(false);
      setUser(null);
      setUserRole(null);
      setInvestorId(null);
      setUserId(null);
      setSessionId(null);
      setError(null);
      localStorage.removeItem('session');
      delete api.defaults.headers.common['X-Session-Id'];
      console.log('User logged out');
    }
  }, [sessionId]);

  return (
    <AuthContext.Provider value={{ 
      isAuthenticated, 
      user, 
      userRole, 
      investorId,
      userId,
      sessionId,
      loading, 
      error, 
      login, 
      logout 
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};