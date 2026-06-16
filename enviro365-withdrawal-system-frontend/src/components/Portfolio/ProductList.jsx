/**
 * Product List Component - Displays all products in portfolio
 * 
 * @author Asandile
 * @version 1.0
 */

import React from 'react';
import { formatCurrency } from '../../utils/formatters';

const ProductList = ({ products, onProductSelect, selectedProduct, investorAge }) => {
  const canWithdrawFromProduct = (product) => {
    if (product.isRetirementProduct && investorAge <= 65) {
      return false;
    }
    return true;
  };

  const getWithdrawalStatus = (product) => {
    if (product.isRetirementProduct && investorAge <= 65) {
      return { text: 'Age Restriction', className: 'badge-rejected' };
    }
    return { text: 'Available', className: 'badge-approved' };
  };

  if (!products || products.length === 0) {
    return <div className="text-center">No products found in this portfolio.</div>;
  }

  return (
    <div className="product-list">
      {products.map(product => {
        const status = getWithdrawalStatus(product);
        const canWithdraw = canWithdrawFromProduct(product);
        
        return (
          <div 
            key={product.productId}
            className={`product-item ${selectedProduct?.productId === product.productId ? 'selected' : ''}`}
            onClick={() => canWithdraw && onProductSelect(product)}
            style={{ cursor: canWithdraw ? 'pointer' : 'not-allowed', opacity: canWithdraw ? 1 : 0.6 }}
          >
            <div className="product-name">
              {product.productName}
              {product.isRetirementProduct && <span className="status-badge badge-info" style={{ marginLeft: '0.5rem' }}>Retirement</span>}
            </div>
            <div className="product-details">
              <span>Type: {product.productType}</span>
              <span className="amount">Balance: {formatCurrency(product.balance)}</span>
              <span className={`status-badge ${status.className}`}>{status.text}</span>
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default ProductList;