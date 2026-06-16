/**
 * Loading Spinner Component
 * 
 * @author Asandile
 * @version 1.0
 */

import React from 'react';

const LoadingSpinner = ({ message = 'Loading...' }) => {
  return (
    <div className="spinner-container">
      <div className="spinner"></div>
      <p style={{ marginLeft: '1rem' }}>{message}</p>
    </div>
  );
};

export default LoadingSpinner;