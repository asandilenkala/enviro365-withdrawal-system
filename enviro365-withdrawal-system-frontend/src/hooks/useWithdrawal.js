/**
 * Custom hook for withdrawal operations
 * 
 * @author Asandile
 * @version 1.0
 */

import { useState, useCallback } from 'react';
import { withdrawalService } from '../services/withdrawalService';

export const useWithdrawal = () => {
  const [withdrawals, setWithdrawals] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const fetchWithdrawals = useCallback(async (investorId) => {
    setLoading(true);
    setError(null);
    
    try {
      const data = await withdrawalService.getInvestorWithdrawals(investorId);
      setWithdrawals(data || []);
    } catch (err) {
      setError(err.message);
      console.error('Error fetching withdrawals:', err);
    } finally {
      setLoading(false);
    }
  }, []);

  const submitWithdrawal = useCallback(async (withdrawalData) => {
    setLoading(true);
    setError(null);
    setSuccess(false);
    
    try {
      const result = await withdrawalService.createWithdrawal(withdrawalData);
      setSuccess(true);
      return result;
    } catch (err) {
      setError(err.message);
      console.error('Error submitting withdrawal:', err);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  return {
    withdrawals,
    loading,
    error,
    success,
    fetchWithdrawals,
    submitWithdrawal
  };
};