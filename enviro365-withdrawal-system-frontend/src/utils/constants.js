/**
 * Application constants
 * 
 * @author Asandile
 * @version 1.0
 */

export const API_ENDPOINTS = {
  PORTFOLIOS: '/portfolios',
  WITHDRAWALS: '/withdrawals',
  REPORTS: '/reports'
};

export const WITHDRAWAL_STATUS = {
  PENDING: 'PENDING',
  APPROVED: 'APPROVED',
  REJECTED: 'REJECTED',
  PROCESSED: 'PROCESSED',
  CANCELLED: 'CANCELLED'
};

export const WITHDRAWAL_STATUS_LABELS = {
  [WITHDRAWAL_STATUS.PENDING]: 'Pending',
  [WITHDRAWAL_STATUS.APPROVED]: 'Approved',
  [WITHDRAWAL_STATUS.REJECTED]: 'Rejected',
  [WITHDRAWAL_STATUS.PROCESSED]: 'Processed',
  [WITHDRAWAL_STATUS.CANCELLED]: 'Cancelled'
};

export const PRODUCT_TYPES = {
  RETIREMENT: 'RETIREMENT',
  SAVINGS: 'SAVINGS',
  INVESTMENT: 'INVESTMENT',
  BONDS: 'BONDS',
  EQUITIES: 'EQUITIES'
};

export const PRODUCT_TYPE_LABELS = {
  [PRODUCT_TYPES.RETIREMENT]: 'Retirement Fund',
  [PRODUCT_TYPES.SAVINGS]: 'Savings Account',
  [PRODUCT_TYPES.INVESTMENT]: 'Investment Portfolio',
  [PRODUCT_TYPES.BONDS]: 'Government Bonds',
  [PRODUCT_TYPES.EQUITIES]: 'Stock Equities'
};

export const BUSINESS_RULES = {
  RETIREMENT_AGE_THRESHOLD: 65,
  MAX_WITHDRAWAL_PERCENTAGE: 0.90
};

export const ERROR_MESSAGES = {
  NETWORK_ERROR: 'Unable to connect to server. Please check your connection.',
  UNAUTHORIZED: 'Unauthorized access. Please check your credentials.',
  NOT_FOUND: 'Requested resource not found.',
  VALIDATION_ERROR: 'Please check your input and try again.',
  SERVER_ERROR: 'Server error occurred. Please try again later.'
};