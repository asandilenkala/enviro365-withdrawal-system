/**
 * Withdrawal Status Component - Shows detailed status of a withdrawal
 * 
 * @author Asandile
 * @version 1.0
 */

import React from 'react';

const WithdrawalStatus = ({ withdrawal }) => {
  if (!withdrawal) return null;

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'Pending';
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const getStatusIcon = (status) => {
    switch (status?.toLowerCase()) {
      case 'approved':
        return '✅';
      case 'pending':
        return '⏳';
      case 'rejected':
        return '❌';
      case 'processed':
        return '💰';
      default:
        return '📝';
    }
  };

  return (
    <div className="card">
      <h3 className="card-title">
        {getStatusIcon(withdrawal.status)} Withdrawal Status
      </h3>
      <div className="grid grid-2">
        <div>
          <p><strong>Request ID:</strong> {withdrawal.withdrawalId}</p>
          <p><strong>Product:</strong> {withdrawal.productName}</p>
          <p><strong>Amount:</strong> <span className="amount">{formatCurrency(withdrawal.amount)}</span></p>
        </div>
        <div>
          <p><strong>Status:</strong> 
            <span className={`status-badge ${getStatusBadgeClass(withdrawal.status)}`} style={{ marginLeft: '0.5rem' }}>
              {withdrawal.statusDescription}
            </span>
          </p>
          <p><strong>Requested:</strong> {formatDate(withdrawal.createdAt)}</p>
          <p><strong>Processed:</strong> {formatDate(withdrawal.processedAt)}</p>
        </div>
      </div>
      {withdrawal.rejectionReason && (
        <div className="alert alert-error mt-2">
          <strong>Rejection Reason:</strong> {withdrawal.rejectionReason}
        </div>
      )}
      {withdrawal.status === 'PENDING' && (
        <div className="alert alert-info mt-2">
          Your withdrawal request is being reviewed. You will be notified once processed.
        </div>
      )}
    </div>
  );
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

export default WithdrawalStatus;