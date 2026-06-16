/**
 * Withdrawal History Component - Displays all withdrawal requests with filtering
 * 
 * @author Asandile
 * @version 1.0
 */

import React, { useEffect, useState } from 'react';
import { useWithdrawal } from '../../hooks/useWithdrawal';
import LoadingSpinner from '../Common/LoadingSpinner';
import ErrorAlert from '../Common/ErrorAlert';
import InvestorSelector from '../Common/InvestorSelector';
import { formatCurrency, formatDate } from '../../utils/formatters';

const WithdrawalHistory = ({ investorId, onInvestorChange }) => {
  const { withdrawals, loading, error, fetchWithdrawals } = useWithdrawal();
  const [filter, setFilter] = useState('all');
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    if (investorId) {
      fetchWithdrawals(investorId);
    }
  }, [investorId, fetchWithdrawals]);

  const getFilteredWithdrawals = () => {
    let filtered = withdrawals || [];
    
    if (filter !== 'all') {
      filtered = filtered.filter(w => w.status.toLowerCase() === filter.toLowerCase());
    }
    
    if (searchTerm) {
      filtered = filtered.filter(w => 
        w.productName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        w.amount?.toString().includes(searchTerm) ||
        w.statusDescription?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
    
    return filtered;
  };

  const getStatusBadgeClass = (status) => {
    switch (status?.toLowerCase()) {
      case 'approved':
        return 'badge-approved';
      case 'pending':
        return 'badge-pending';
      case 'rejected':
        return 'badge-rejected';
      case 'processed':
        return 'badge-processed';
      default:
        return 'badge-pending';
    }
  };

  if (loading) return <LoadingSpinner message="Loading withdrawal history..." />;
  if (error) return <ErrorAlert message={error} />;

  const filteredWithdrawals = getFilteredWithdrawals();

  return (
    <div className="withdrawal-history">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1>Withdrawal History</h1>
        <InvestorSelector 
          selectedInvestorId={investorId} 
          onInvestorChange={onInvestorChange} 
        />
      </div>
      
      <div className="card">
        <div className="history-controls" style={{ display: 'flex', gap: '1rem', marginBottom: '1rem' }}>
          <div className="form-group" style={{ flex: 1 }}>
            <label>Filter by Status:</label>
            <select value={filter} onChange={(e) => setFilter(e.target.value)}>
              <option value="all">All</option>
              <option value="pending">Pending</option>
              <option value="approved">Approved</option>
              <option value="rejected">Rejected</option>
              <option value="processed">Processed</option>
            </select>
          </div>
          
          <div className="form-group" style={{ flex: 2 }}>
            <label>Search:</label>
            <input
              type="text"
              placeholder="Search by product, amount, or status..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>
      
        {filteredWithdrawals.length === 0 ? (
          <div className="text-center">
            <p>No withdrawal requests found.</p>
          </div>
        ) : (
          <div className="table-container">
            <table className="history-table">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Product</th>
                  <th>Amount</th>
                  <th>Status</th>
                  <th>Rejection Reason</th>
                </tr>
              </thead>
              <tbody>
                {filteredWithdrawals.map(withdrawal => (
                  <tr key={withdrawal.withdrawalId}>
                    <td>{formatDate(withdrawal.createdAt)}</td>
                    <td>{withdrawal.productName}</td>
                    <td className="amount">{formatCurrency(withdrawal.amount)}</td>
                    <td>
                      <span className={`status-badge ${getStatusBadgeClass(withdrawal.status)}`}>
                        {withdrawal.statusDescription}
                      </span>
                    </td>
                    <td>{withdrawal.rejectionReason || '-'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
      
      <div className="summary-stats">
        <div className="stat-card">
          <h4>Total Withdrawals</h4>
          <div className="stat-value">{withdrawals?.length || 0}</div>
        </div>
        <div className="stat-card">
          <h4>Approved Amount</h4>
          <div className="stat-value">
            {formatCurrency(
              withdrawals?.filter(w => w.status === 'APPROVED' || w.status === 'PROCESSED')
                .reduce((sum, w) => sum + (w.amount || 0), 0) || 0
            )}
          </div>
        </div>
        <div className="stat-card">
          <h4>Pending Requests</h4>
          <div className="stat-value">
            {withdrawals?.filter(w => w.status === 'PENDING').length || 0}
          </div>
        </div>
      </div>
    </div>
  );
};

export default WithdrawalHistory;