/**
 * Portfolio Summary Component - Shows portfolio statistics
 * 
 * @author Asandile
 * @version 1.0
 */

import React from 'react';
import { formatCurrency } from '../../utils/formatters';

const PortfolioSummary = ({ portfolio }) => {
  const getProductTypeCount = () => {
    if (!portfolio?.products) return {};
    return portfolio.products.reduce((acc, product) => {
      acc[product.productType] = (acc[product.productType] || 0) + 1;
      return acc;
    }, {});
  };

  const productCounts = getProductTypeCount();

  return (
    <div className="card">
      <h3 className="card-title">Portfolio Summary</h3>
      <div className="grid grid-2">
        <div>
          <p><strong>Portfolio Name:</strong> {portfolio.portfolioName}</p>
          <p><strong>Portfolio ID:</strong> {portfolio.portfolioId}</p>
          <p><strong>Total Products:</strong> {portfolio.products?.length || 0}</p>
        </div>
        <div>
          <p><strong>Total Balance:</strong> {formatCurrency(portfolio.totalBalance)}</p>
          <p><strong>Available Balance:</strong> {formatCurrency(portfolio.availableForWithdrawal)}</p>
          <p><strong>Max Withdrawal:</strong> {formatCurrency(portfolio.maxWithdrawalAmount)}</p>
        </div>
      </div>
      
      {Object.keys(productCounts).length > 0 && (
        <div className="mt-2">
          <h4>Product Distribution</h4>
          <div className="grid grid-3">
            {Object.entries(productCounts).map(([type, count]) => (
              <div key={type} className="stat-card">
                <h4>{type}</h4>
                <div className="stat-value">{count}</div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default PortfolioSummary;