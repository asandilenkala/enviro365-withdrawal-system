/**
 * Investor Selector Component - Allows switching between investors
 * 
 * @author Asandile
 * @version 1.0
 */

import React from 'react';

const InvestorSelector = ({ selectedInvestorId, onInvestorChange }) => {
  // Sample investors from the database
  const investors = [
    { id: '123e4567-e89b-12d3-a456-426614174000', name: 'John Smith' },
    { id: '223e4567-e89b-12d3-a456-426614174001', name: 'Sarah Johnson' },
    { id: '323e4567-e89b-12d3-a456-426614174002', name: 'Michael Williams' },
    { id: '423e4567-e89b-12d3-a456-426614174003', name: 'Emily Brown' }
  ];

  const handleChange = (e) => {
    onInvestorChange(e.target.value);
  };

  return (
    <div className="investor-selector" style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
      <label htmlFor="investorSelect" style={{ fontWeight: 'bold' }}>
        Select Investor:
      </label>
      <select
        id="investorSelect"
        value={selectedInvestorId}
        onChange={handleChange}
        style={{
          padding: '0.5rem',
          borderRadius: '4px',
          border: '1px solid #ddd',
          fontSize: '1rem',
          minWidth: '200px'
        }}
      >
        {investors.map(investor => (
          <option key={investor.id} value={investor.id}>
            {investor.name}
          </option>
        ))}
      </select>
    </div>
  );
};

export default InvestorSelector;