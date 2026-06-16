// src/components/Auth/Login.jsx
/**
 * Login Component - Handles user authentication
 * 
 * @author Asandile
 * @version 1.0
 */

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import ErrorAlert from '../Common/ErrorAlert';

const Login = () => {
  const { login, loading } = useAuth();
  const navigate = useNavigate();
  const [credentials, setCredentials] = useState({
    username: 'enviro_admin',
    password: 'enviro365_2024'
  });
  const [loginError, setLoginError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCredentials(prev => ({ ...prev, [name]: value }));
    // Clear error when user starts typing
    if (loginError) {
      setLoginError(null);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoginError(null);
    
    try {
      const success = await login(credentials.username, credentials.password);
      
      if (success) {
        // Navigate to dashboard on successful login
        navigate('/dashboard', { replace: true });
      } else {
        setLoginError('Invalid username or password. Please try again.');
      }
    } catch (err) {
      console.error('Login error:', err);
      setLoginError(err.message || 'An error occurred during login. Please try again.');
    }
  };

  return (
    <div className="login-container" style={{ 
      display: 'flex', 
      justifyContent: 'center', 
      alignItems: 'center', 
      minHeight: '100vh',
      background: 'linear-gradient(135deg, #4CAF50, #388E3C)'
    }}>
      <div className="login-card" style={{
        background: 'white',
        borderRadius: '8px',
        padding: '2rem',
        width: '100%',
        maxWidth: '400px',
        boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
      }}>
        <div className="login-header" style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <h1 style={{ color: '#4CAF50', marginBottom: '0.5rem' }}>Enviro365</h1>
          <h2 style={{ fontSize: '1.1rem', color: '#666', fontWeight: 'normal' }}>
            Withdrawal Management System
          </h2>
        </div>
        
        {loginError && <ErrorAlert message={loginError} />}
        
        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="username" style={{ fontWeight: '500' }}>Username</label>
            <input
              type="text"
              id="username"
              name="username"
              value={credentials.username}
              onChange={handleChange}
              placeholder="Enter your username"
              required
              autoFocus
              style={{
                width: '100%',
                padding: '0.5rem',
                border: '1px solid #ddd',
                borderRadius: '4px',
                fontSize: '1rem'
              }}
            />
          </div>
          
          <div className="form-group" style={{ marginTop: '1rem' }}>
            <label htmlFor="password" style={{ fontWeight: '500' }}>Password</label>
            <input
              type="password"
              id="password"
              name="password"
              value={credentials.password}
              onChange={handleChange}
              placeholder="Enter your password"
              required
              style={{
                width: '100%',
                padding: '0.5rem',
                border: '1px solid #ddd',
                borderRadius: '4px',
                fontSize: '1rem'
              }}
            />
          </div>
          
          <button 
            type="submit" 
            className="btn btn-primary"
            disabled={loading}
            style={{
              width: '100%',
              padding: '0.75rem',
              marginTop: '1.5rem',
              backgroundColor: '#4CAF50',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              fontSize: '1rem',
              fontWeight: 'bold',
              cursor: 'pointer',
              transition: 'background-color 0.3s'
            }}
          >
            {loading ? 'Logging in...' : 'Sign In'}
          </button>
          
          <div className="login-footer" style={{
            marginTop: '1.5rem',
            paddingTop: '1rem',
            borderTop: '1px solid #eee',
            textAlign: 'center',
            fontSize: '0.875rem',
            color: '#666'
          }}>
            <p style={{ marginBottom: '0.5rem', fontWeight: '500' }}>Test Credentials:</p>
            <div>
              <small>Admin: <strong>enviro_admin</strong> / <strong>enviro365_2024</strong></small>
              <br />
              <small>Investor: <strong>john_smith</strong> / <strong>enviro365_2024</strong></small>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;