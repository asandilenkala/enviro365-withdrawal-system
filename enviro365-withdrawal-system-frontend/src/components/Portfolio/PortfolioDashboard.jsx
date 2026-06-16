/**
 * Portfolio Dashboard Component - Displays investor portfolio with balance calculations
 * 
 * @author Asandile
 * @version 1.0
 */

import React, { useEffect, useState } from 'react';
import { usePortfolio } from '../../hooks/usePortfolio';
import LoadingSpinner from '../Common/LoadingSpinner';
import ErrorAlert from '../Common/ErrorAlert';
import InvestorSelector from '../Common/InvestorSelector';
import PortfolioSummary from './PortfolioSummary';
import ProductList from './ProductList';
import { formatCurrency } from '../../utils/formatters';

const PortfolioDashboard = ({ investorId, onInvestorChange }) => {
  const { portfolio, loading, error, fetchPortfolio } = usePortfolio();
  const [selectedProduct, setSelectedProduct] = useState(null);

  useEffect(() => {
    if (investorId) {
      fetchPortfolio(investorId);
    }
  }, [investorId, fetchPortfolio]);

  if (loading) return <LoadingSpinner message="Loading portfolio details..." />;
  if (error) return <ErrorAlert message={error} />;
  if (!portfolio) return <div className="text-center">No portfolio data available</div>;

  return (
    <div className="portfolio-dashboard">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1>Portfolio Dashboard</h1>
        <InvestorSelector 
          selectedInvestorId={investorId} 
          onInvestorChange={onInvestorChange} 
        />
      </div>
      
      <div className="card">
        <div className="investor-info">
          <h2>Welcome, {portfolio.investor?.fullName}</h2>
          <p>Email: {portfolio.investor?.email}</p>
          <p>Age: {portfolio.investor?.age} years</p>
          {portfolio.investor?.canWithdrawRetirement && (
            <span className="status-badge badge-approved">Retirement Withdrawal Eligible</span>
          )}
        </div>
      </div>

      <PortfolioSummary portfolio={portfolio} />
      
      <div className="card">
        <h3 className="card-title">Investment Products</h3>
        <ProductList 
          products={portfolio.products} 
          onProductSelect={setSelectedProduct}
          selectedProduct={selectedProduct}
          investorAge={portfolio.investor?.age}
        />
      </div>

      <div className="card">
        <h3 className="card-title">Withdrawal Information</h3>
        <div className="summary-stats">
          <div className="stat-card">
            <h4>Total Balance</h4>
            <div className="stat-value">{formatCurrency(portfolio.totalBalance)}</div>
          </div>
          <div className="stat-card">
            <h4>Available for Withdrawal</h4>
            <div className="stat-value">{formatCurrency(portfolio.availableForWithdrawal)}</div>
          </div>
          <div className="stat-card">
            <h4>Maximum Withdrawal (90%)</h4>
            <div className="stat-value">{formatCurrency(portfolio.maxWithdrawalAmount)}</div>
          </div>
        </div>
        <div className="alert alert-info">
          <small>* Retirement products require age &gt; 65 for withdrawal</small>
          <br />
          <small>* Maximum withdrawal is 90% of non-retirement balance</small>
        </div>
      </div>
    </div>
  );
};

export default PortfolioDashboard;