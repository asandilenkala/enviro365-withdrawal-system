/**
 * Withdrawal Service - Handles withdrawal API calls
 * 
 * @author Asandile
 * @version 1.0
 */

import api from './api';

export const withdrawalService = {
  /**
   * Create a new withdrawal request
   * @param {Object} withdrawalData - Withdrawal request data
   * @returns {Promise} Created withdrawal
   */
  async createWithdrawal(withdrawalData) {
    try {
      const response = await api.post('/withdrawals', withdrawalData);
      if (response.data && response.data.data) {
        return response.data.data;
      }
      return response.data;
    } catch (error) {
      console.error('Error creating withdrawal:', error);
      throw error;
    }
  },

  /**
   * Get withdrawal history for an investor
   * @param {string} investorId - Investor UUID
   * @returns {Promise} List of withdrawals
   */
  async getInvestorWithdrawals(investorId) {
    try {
      const response = await api.get(`/withdrawals/investor/${investorId}`);
      if (response.data && response.data.data) {
        return response.data.data;
      }
      return response.data;
    } catch (error) {
      console.error('Error fetching withdrawals:', error);
      throw error;
    }
  },

  /**
   * Update withdrawal status (admin)
   * @param {string} withdrawalId - Withdrawal UUID
   * @param {string} status - New status
   * @param {string} rejectionReason - Reason for rejection
   * @returns {Promise} Updated withdrawal
   */
  async updateWithdrawalStatus(withdrawalId, status, rejectionReason) {
    try {
      const response = await api.put(`/withdrawals/${withdrawalId}/status`, null, {
        params: { status, rejectionReason }
      });
      if (response.data && response.data.data) {
        return response.data.data;
      }
      return response.data;
    } catch (error) {
      console.error('Error updating withdrawal status:', error);
      throw error;
    }
  }
};