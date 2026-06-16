/**
 * Validation utilities for form inputs
 * 
 * @author Asandile
 * @version 1.0
 */

/**
 * Validate withdrawal amount
 * @param {number} amount - Withdrawal amount
 * @param {number} balance - Product balance
 * @param {boolean} isRetirement - Is retirement product
 * @param {number} age - Investor age
 * @returns {string|null} Error message or null if valid
 */
export const validateWithdrawalAmount = (amount, balance, isRetirement, age) => {
  if (!amount || amount <= 0) {
    return 'Please enter a valid withdrawal amount';
  }
  
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

/**
 * Validate email format
 * @param {string} email - Email address
 * @returns {boolean} Is valid email
 */
export const isValidEmail = (email) => {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(email);
};

/**
 * Validate phone number
 * @param {string} phone - Phone number
 * @returns {boolean} Is valid phone
 */
export const isValidPhone = (phone) => {
  const regex = /^[\+]?[(]?[0-9]{1,4}[)]?[-\s\.]?[(]?[0-9]{1,4}[)]?[-\s\.]?[0-9]{1,5}[-\s\.]?[0-9]{1,5}$/;
  return regex.test(phone);
};

/**
 * Format currency for error messages
 * @param {number} amount - Amount to format
 * @returns {string} Formatted currency
 */
const formatCurrency = (amount) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD'
  }).format(amount);
};