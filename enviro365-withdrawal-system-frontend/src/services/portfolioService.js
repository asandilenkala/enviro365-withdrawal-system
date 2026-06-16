/**
 * Portfolio Service - Handles portfolio API calls
 * 
 * @author Asandile
 * @version 1.0
 */

import api from './api';

export const portfolioService = {
  /**
   * Get portfolio by investor ID
   * @param {string} investorId - Investor UUID
   * @returns {Promise} Portfolio data
   */
  async getPortfolioByInvestorId(investorId) {
    try {
      const response = await api.get(`/portfolios/investor/${investorId}`);
      // The API returns { success, message, data, timestamp }
      // We need to extract the data property
      if (response.data && response.data.data) {
        return response.data;
      }
      return response;
    } catch (error) {
      console.error('Error fetching portfolio:', error);
      throw error;
    }
  },

  /**
   * Get all portfolios (admin)
   * @returns {Promise} List of all portfolios
   */
  async getAllPortfolios() {
    try {
      const response = await api.get('/portfolios');
      return response;
    } catch (error) {
      console.error('Error fetching all portfolios:', error);
      throw error;
    }
  }
};