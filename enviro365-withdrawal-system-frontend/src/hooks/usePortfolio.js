/**
 * Custom hook for portfolio operations
 * 
 * @author Asandile
 * @version 1.0
 */

import { useState, useCallback } from 'react';
import { portfolioService } from '../services/portfolioService';

export const usePortfolio = () => {
  const [portfolio, setPortfolio] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchPortfolio = useCallback(async (investorId) => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await portfolioService.getPortfolioByInvestorId(investorId);
      // Extract data from the response
      if (response && response.data) {
        setPortfolio(response.data);
      } else if (response && response.data === undefined) {
        // If response is already the data
        setPortfolio(response);
      } else {
        setPortfolio(null);
      }
    } catch (err) {
      setError(err.message);
      console.error('Error fetching portfolio:', err);
    } finally {
      setLoading(false);
    }
  }, []);

  return {
    portfolio,
    loading,
    error,
    fetchPortfolio
  };
};