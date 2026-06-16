/**
 * Main Application Component for Enviro365 Withdrawal Management System
 * 
 * @author Asandile
 * @version 1.0
 */

import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { AuthProvider, useAuth } from './hooks/useAuth';
import Navbar from './components/Common/Navbar';
import Login from './components/Auth/Login';
import PortfolioDashboard from './components/Portfolio/PortfolioDashboard';
import WithdrawalForm from './components/Withdrawal/WithdrawalForm';
import WithdrawalHistory from './components/Withdrawal/WithdrawalHistory';
import CsvDownloader from './components/Reports/CsvDownloader';
import LoadingSpinner from './components/Common/LoadingSpinner';
import './styles/App.css';

// Protected route component
const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return <LoadingSpinner message="Checking authentication..." />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return children;
};

function AppContent() {
  const [selectedInvestorId, setSelectedInvestorId] = useState('123e4567-e89b-12d3-a456-426614174000');
  const { isAuthenticated, loading } = useAuth();

  // Show loading while checking auth
  if (loading) {
    return <LoadingSpinner message="Loading application..." />;
  }

  // If not authenticated, show only login route
  if (!isAuthenticated) {
    return (
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    );
  }

  // Authenticated routes
  return (
    <>
      <Navbar />
      <main className="main-content">
        <div className="container">
          <Routes>
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route path="/dashboard" element={
              <ProtectedRoute>
                <PortfolioDashboard 
                  investorId={selectedInvestorId}
                  onInvestorChange={setSelectedInvestorId}
                />
              </ProtectedRoute>
            } />
            <Route path="/withdraw" element={
              <ProtectedRoute>
                <WithdrawalForm 
                  investorId={selectedInvestorId}
                  onInvestorChange={setSelectedInvestorId}
                />
              </ProtectedRoute>
            } />
            <Route path="/history" element={
              <ProtectedRoute>
                <WithdrawalHistory 
                  investorId={selectedInvestorId}
                  onInvestorChange={setSelectedInvestorId}
                />
              </ProtectedRoute>
            } />
            <Route path="/reports" element={
              <ProtectedRoute>
                <CsvDownloader 
                  investorId={selectedInvestorId}
                  onInvestorChange={setSelectedInvestorId}
                />
              </ProtectedRoute>
            } />
            <Route path="/login" element={<Login />} />
          </Routes>
        </div>
      </main>
    </>
  );
}

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <AppContent />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;