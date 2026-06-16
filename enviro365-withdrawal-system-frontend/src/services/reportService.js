/**
 * Report Service - Handles report generation API calls
 * 
 * @author Asandile
 * @version 1.0
 */

import api from './api';

export const reportService = {
  /**
   * Download withdrawal report for an investor
   * @param {string} investorId - Investor UUID
   * @param {Object} filters - Filter criteria
   * @returns {Promise} CSV file blob
   */
  async downloadWithdrawalReport(investorId, filters = {}) {
    try {
      const response = await api.post(`/reports/withdrawals/investor/${investorId}`, filters, {
        responseType: 'blob'
      });
      
      // Create blob link to download
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `withdrawal_report_${investorId}_${new Date().toISOString().split('T')[0]}.csv`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
      
      return response;
    } catch (error) {
      console.error('Error downloading report:', error);
      throw error;
    }
  },

  /**
   * Download all withdrawals report (admin)
   * @param {Object} filters - Filter criteria
   * @returns {Promise} CSV file blob
   */
  async downloadAllWithdrawalsReport(filters = {}) {
    try {
      const response = await api.post('/reports/withdrawals/all', filters, {
        responseType: 'blob'
      });
      
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `all_withdrawals_report_${new Date().toISOString().split('T')[0]}.csv`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
      
      return response;
    } catch (error) {
      console.error('Error downloading report:', error);
      throw error;
    }
  }
};