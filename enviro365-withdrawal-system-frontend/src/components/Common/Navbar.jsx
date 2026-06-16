/**
 * Navigation Bar Component
 * 
 * @author Asandile
 * @version 1.0
 */

import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

const Navbar = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await logout();
      navigate('/login', { replace: true });
    } catch (err) {
      console.error('Logout error:', err);
      // Force navigation even if logout fails
      navigate('/login', { replace: true });
    }
  };

  if (!isAuthenticated) {
    return null;
  }

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <NavLink to="/dashboard" className="navbar-brand">
          Enviro365
        </NavLink>
        <ul className="nav-menu">
          <li>
            <NavLink to="/dashboard" className="nav-link" end>
              Dashboard
            </NavLink>
          </li>
          <li>
            <NavLink to="/withdraw" className="nav-link">
              Withdraw
            </NavLink>
          </li>
          <li>
            <NavLink to="/history" className="nav-link">
              History
            </NavLink>
          </li>
          <li>
            <NavLink to="/reports" className="nav-link">
              Reports
            </NavLink>
          </li>
        </ul>
        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
          <span style={{ color: 'white', fontSize: '0.9rem' }}>
            {user?.username || 'User'}
          </span>
          <button 
            onClick={handleLogout} 
            className="btn btn-danger" 
            style={{ padding: '0.25rem 0.75rem', fontSize: '0.9rem' }}
          >
            Logout
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;