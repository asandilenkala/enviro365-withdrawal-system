/**
 * Error Alert Component
 * 
 * @author Asandile
 * @version 1.0
 */

import React, { useState } from 'react';

const ErrorAlert = ({ message, onDismiss }) => {
  const [visible, setVisible] = useState(true);

  const handleDismiss = () => {
    setVisible(false);
    if (onDismiss) onDismiss();
  };

  if (!visible) return null;

  return (
    <div className="alert alert-error">
      <span>❌ {message}</span>
      <button onClick={handleDismiss} className="btn" style={{ background: 'none', padding: 0 }}>
        ✕
      </button>
    </div>
  );
};

export default ErrorAlert;