/**
 * Success Alert Component
 * 
 * @author Asandile
 * @version 1.0
 */

import React, { useState, useEffect } from 'react';

const SuccessAlert = ({ message, onDismiss, autoDismiss = 5000 }) => {
  const [visible, setVisible] = useState(true);

  useEffect(() => {
    if (autoDismiss) {
      const timer = setTimeout(() => {
        setVisible(false);
        if (onDismiss) onDismiss();
      }, autoDismiss);
      return () => clearTimeout(timer);
    }
  }, [autoDismiss, onDismiss]);

  const handleDismiss = () => {
    setVisible(false);
    if (onDismiss) onDismiss();
  };

  if (!visible) return null;

  return (
    <div className="alert alert-success">
      <span>✅ {message}</span>
      <button onClick={handleDismiss} className="btn" style={{ background: 'none', padding: 0 }}>
        ✕
      </button>
    </div>
  );
};

export default SuccessAlert;