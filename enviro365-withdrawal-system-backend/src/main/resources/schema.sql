-- ===============================================
-- Database schema for Enviro365 Withdrawal Management System
-- ===============================================

-- Investors table
CREATE TABLE IF NOT EXISTS investors (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    date_of_birth TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Portfolios table
CREATE TABLE IF NOT EXISTS portfolios (
    id UUID PRIMARY KEY,
    portfolio_name VARCHAR(255) NOT NULL,
    total_balance DECIMAL(19,2) NOT NULL DEFAULT 0,
    investor_id UUID NOT NULL,
    CONSTRAINT fk_portfolio_investor FOREIGN KEY (investor_id) REFERENCES investors(id) ON DELETE CASCADE
);

-- Products table
CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_type VARCHAR(50) NOT NULL,
    balance DECIMAL(19,2) NOT NULL DEFAULT 0,
    portfolio_id UUID NOT NULL,
    CONSTRAINT fk_product_portfolio FOREIGN KEY (portfolio_id) REFERENCES portfolios(id) ON DELETE CASCADE
);

-- Withdrawals table (rejection_reason allows empty string, processed_at allows NULL)
CREATE TABLE IF NOT EXISTS withdrawals (
    id UUID PRIMARY KEY,
    amount DECIMAL(19,2) NOT NULL,
    product_id VARCHAR(255) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    rejection_reason VARCHAR(255) DEFAULT '',
    investor_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    processed_at TIMESTAMP NULL,
    CONSTRAINT fk_withdrawal_investor FOREIGN KEY (investor_id) REFERENCES investors(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_investor_email ON investors(email);
CREATE INDEX idx_portfolio_investor ON portfolios(investor_id);
CREATE INDEX idx_product_portfolio ON products(portfolio_id);
CREATE INDEX idx_withdrawal_investor ON withdrawals(investor_id);
CREATE INDEX idx_withdrawal_status ON withdrawals(status);
CREATE INDEX idx_withdrawal_created_at ON withdrawals(created_at);