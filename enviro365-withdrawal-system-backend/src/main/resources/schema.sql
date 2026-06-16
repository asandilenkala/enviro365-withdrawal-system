-- ===============================================
-- Database Schema for Enviro365 Withdrawal Management System
-- ===============================================

DROP TABLE IF EXISTS withdrawals CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS portfolios CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS investors CASCADE;

-- Investors table
CREATE TABLE investors (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    date_of_birth TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Users table for authentication
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    investor_id UUID,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_investor FOREIGN KEY (investor_id) REFERENCES investors(id) ON DELETE CASCADE
);

-- Portfolios table
CREATE TABLE portfolios (
    id UUID PRIMARY KEY,
    portfolio_name VARCHAR(255) NOT NULL,
    total_balance DECIMAL(19,2) NOT NULL DEFAULT 0,
    investor_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_portfolio_investor FOREIGN KEY (investor_id) REFERENCES investors(id) ON DELETE CASCADE
);

-- Products table
CREATE TABLE products (
    id UUID PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_type VARCHAR(50) NOT NULL,
    balance DECIMAL(19,2) NOT NULL DEFAULT 0,
    portfolio_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_product_portfolio FOREIGN KEY (portfolio_id) REFERENCES portfolios(id) ON DELETE CASCADE
);

-- Withdrawals table
CREATE TABLE withdrawals (
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

-- Indexes
CREATE INDEX idx_investor_email ON investors(email);
CREATE INDEX idx_portfolio_investor ON portfolios(investor_id);
CREATE INDEX idx_product_portfolio ON products(portfolio_id);
CREATE INDEX idx_withdrawal_investor ON withdrawals(investor_id);
CREATE INDEX idx_withdrawal_status ON withdrawals(status);
CREATE INDEX idx_withdrawal_created_at ON withdrawals(created_at);
CREATE INDEX idx_user_username ON users(username);
CREATE INDEX idx_user_investor ON users(investor_id);