/**
 * Withdrawal Form Component - Handles withdrawal requests with validation
 * 
 * @author Asandile
 * @version 1.0
 */

import React, { useState, useEffect } from 'react';
import { useWithdrawal } from '../../hooks/useWithdrawal';
import { usePortfolio } from '../../hooks/usePortfolio';
import LoadingSpinner from '../Common/LoadingSpinner';
import ErrorAlert from '../Common/ErrorAlert';
import SuccessAlert from '../Common/SuccessAlert';
import InvestorSelector from '../Common/InvestorSelector';
import { formatCurrency } from '../../utils/formatters';

const WithdrawalForm = ({ investorId, onInvestorChange }) => {
  const { submitWithdrawal, loading, error: withdrawalError, success } = useWithdrawal();
  const { portfolio, fetchPortfolio } = usePortfolio();
  
  const [formData, setFormData] = useState({
    productId: '',
    productName: '',
    amount: '',
    notes: ''
  });
  
  const [validationErrors, setValidationErrors] = useState({});
  const [selectedProductBalance, setSelectedProductBalance] = useState(0);
  const [isRetirementProduct, setIsRetirementProduct] = useState(false);

  useEffect(() => {
    if (investorId) {
      fetchPortfolio(investorId);
    }
  }, [investorId, fetchPortfolio]);

  const handleProductChange = (e) => {
    const productId = e.target.value;
    const selectedProduct = portfolio?.products?.find(p => p.productId === productId);
    
    if (selectedProduct) {
      setFormData({
        ...formData,
        productId: selectedProduct.productId,
        productName: selectedProduct.productName
      });
      setSelectedProductBalance(selectedProduct.balance);
      setIsRetirementProduct(selectedProduct.isRetirementProduct);
      
      if (validationErrors.amount) {
        setValidationErrors({ ...validationErrors, amount: null });
      }
    }
  };

  const validateWithdrawalAmount = (amount, balance, isRetirement, age) => {
    if (amount > balance) {
      return `Withdrawal amount exceeds product balance of ${formatCurrency(balance)}`;
    }
    
    if (isRetirement && age <= 65) {
      return 'Retirement withdrawal only allowed if age > 65';
    }
    
    if (!isRetirement) {
      const maxAmount = balance * 0.9;
      if (amount > maxAmount) {
        return `Withdrawal cannot exceed 90% of balance. Maximum allowed: ${formatCurrency(maxAmount)}`;
      }
    }
    
    return null;
  };

  const handleAmountChange = (e) => {
    const amount = parseFloat(e.target.value) || 0;
    setFormData({ ...formData, amount: e.target.value });
    
    const error = validateWithdrawalAmount(
      amount, 
      selectedProductBalance, 
      isRetirementProduct,
      portfolio?.investor?.age
    );
    
    setValidationErrors({
      ...validationErrors,
      amount: error
    });
  };

  const validateForm = (data, balance, isRetirement, age) => {
    const errors = {};
    
    if (!data.productId) {
      errors.productId = 'Please select a product';
    }
    
    if (!data.amount || data.amount <= 0) {
      errors.amount = 'Please enter a valid withdrawal amount';
    } else {
      const amountError = validateWithdrawalAmount(
        parseFloat(data.amount), 
        balance, 
        isRetirement, 
        age
      );
      if (amountError) errors.amount = amountError;
    }
    
    return errors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const errors = validateForm(formData, selectedProductBalance, isRetirementProduct, portfolio?.investor?.age);
    
    if (Object.keys(errors).length > 0) {
      setValidationErrors(errors);
      return;
    }
    
    const withdrawalData = {
      investorId: investorId,
      productId: formData.productId,
      productName: formData.productName,
      amount: parseFloat(formData.amount),
      notes: formData.notes
    };
    
    await submitWithdrawal(withdrawalData);
    
    if (!withdrawalError) {
      setFormData({
        productId: '',
        productName: '',
        amount: '',
        notes: ''
      });
      setValidationErrors({});
      fetchPortfolio(investorId);
    }
  };

  if (loading && !success) return <LoadingSpinner message="Processing withdrawal..." />;

  return (
    <div className="withdrawal-form-container">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1>Request Withdrawal</h1>
        <InvestorSelector 
          selectedInvestorId={investorId} 
          onInvestorChange={onInvestorChange} 
        />
      </div>
      
      {success && (
        <SuccessAlert 
          message="Withdrawal request submitted successfully! Please check history for status updates."
        />
      )}
      
      {withdrawalError && <ErrorAlert message={withdrawalError} />}
      
      <div className="card">
        <form onSubmit={handleSubmit} className="withdrawal-form">
          <div className="form-group">
            <label htmlFor="product">Select Product *</label>
            <select
              id="product"
              value={formData.productId}
              onChange={handleProductChange}
              className={validationErrors.productId ? 'error' : ''}
              required
            >
              <option value="">-- Select a product --</option>
              {portfolio?.products?.map(product => (
                <option key={product.productId} value={product.productId}>
                  {product.productName} - {product.productType} ({formatCurrency(product.balance)})
                  {product.isRetirementProduct && ' (Retirement)'}
                </option>
              ))}
            </select>
            {validationErrors.productId && (
              <span className="error-message">{validationErrors.productId}</span>
            )}
          </div>
          
          <div className="form-group">
            <label htmlFor="amount">Withdrawal Amount *</label>
            <div className="amount-input-wrapper">
              <span className="currency-symbol">R</span>
              <input
                type="number"
                id="amount"
                step="0.01"
                min="0.01"
                value={formData.amount}
                onChange={handleAmountChange}
                placeholder="0.00"
                className={validationErrors.amount ? 'error' : ''}
                required
              />
            </div>
            {validationErrors.amount && (
              <span className="error-message">{validationErrors.amount}</span>
            )}
            {selectedProductBalance > 0 && (
              <div className="balance-info">
                <small>Available balance: {formatCurrency(selectedProductBalance)}</small>
                {!isRetirementProduct && (
                  <small>Maximum allowed (90%): {formatCurrency(selectedProductBalance * 0.9)}</small>
                )}
              </div>
            )}
          </div>
          
          <div className="form-group">
            <label htmlFor="notes">Notes (Optional)</label>
            <textarea
              id="notes"
              rows="3"
              value={formData.notes}
              onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
              placeholder="Add any additional information about your withdrawal request..."
              maxLength="500"
            />
            <small className="character-count">
              {formData.notes.length}/500 characters
            </small>
          </div>
          
          <div className="form-actions" style={{ display: 'flex', gap: '1rem', marginTop: '1rem' }}>
            <button 
              type="submit" 
              className="btn btn-primary"
              disabled={loading || Object.keys(validationErrors).length > 0}
            >
              {loading ? 'Submitting...' : 'Submit Withdrawal Request'}
            </button>
            <button 
              type="button" 
              className="btn btn-secondary"
              onClick={() => {
                setFormData({
                  productId: '',
                  productName: '',
                  amount: '',
                  notes: ''
                });
                setValidationErrors({});
              }}
            >
              Clear Form
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default WithdrawalForm;