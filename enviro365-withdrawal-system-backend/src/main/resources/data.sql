-- ===============================================
-- Sample Data for Enviro365 Withdrawal Management System
-- ===============================================

-- Insert sample investors
INSERT INTO investors (id, first_name, last_name, email, phone_number, date_of_birth, created_at, updated_at) VALUES
('123e4567-e89b-12d3-a456-426614174000', 'John', 'Smith', 'john.smith@email.com', '+1 555-0100', '1970-05-15 00:00:00', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('223e4567-e89b-12d3-a456-426614174001', 'Sarah', 'Johnson', 'sarah.johnson@email.com', '+1 555-0101', '1985-08-22 00:00:00', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('323e4567-e89b-12d3-a456-426614174002', 'Michael', 'Williams', 'michael.williams@email.com', '+1 555-0102', '1955-03-10 00:00:00', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('423e4567-e89b-12d3-a456-426614174003', 'Emily', 'Brown', 'emily.brown@email.com', '+1 555-0103', '1990-11-30 00:00:00', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Insert portfolios
INSERT INTO portfolios (id, portfolio_name, total_balance, investor_id) VALUES
('123e4567-e89b-12d3-a456-426614174100', 'John Smith Retirement Portfolio', 250000.00, '123e4567-e89b-12d3-a456-426614174000'),
('223e4567-e89b-12d3-a456-426614174101', 'Sarah Johnson Growth Portfolio', 175000.00, '223e4567-e89b-12d3-a456-426614174001'),
('323e4567-e89b-12d3-a456-426614174102', 'Michael Williams Conservative Portfolio', 500000.00, '323e4567-e89b-12d3-a456-426614174002'),
('423e4567-e89b-12d3-a456-426614174103', 'Emily Brown Aggressive Portfolio', 95000.00, '423e4567-e89b-12d3-a456-426614174003');

-- Insert products for John Smith (age 54)
INSERT INTO products (id, product_name, product_type, balance, portfolio_id) VALUES
('123e4567-e89b-12d3-a456-426614174200', 'Retirement Fund 2030', 'RETIREMENT', 150000.00, '123e4567-e89b-12d3-a456-426614174100'),
('123e4567-e89b-12d3-a456-426614174201', 'S&P 500 Index Fund', 'EQUITIES', 75000.00, '123e4567-e89b-12d3-a456-426614174100'),
('123e4567-e89b-12d3-a456-426614174202', 'High Yield Savings', 'SAVINGS', 25000.00, '123e4567-e89b-12d3-a456-426614174100');

-- Insert products for Sarah Johnson (age 39)
INSERT INTO products (id, product_name, product_type, balance, portfolio_id) VALUES
('223e4567-e89b-12d3-a456-426614174200', 'Tech Growth Fund', 'INVESTMENT', 100000.00, '223e4567-e89b-12d3-a456-426614174101'),
('223e4567-e89b-12d3-a456-426614174201', 'Emergency Savings', 'SAVINGS', 50000.00, '223e4567-e89b-12d3-a456-426614174101'),
('223e4567-e89b-12d3-a456-426614174202', 'Bond Portfolio', 'BONDS', 25000.00, '223e4567-e89b-12d3-a456-426614174101');

-- Insert products for Michael Williams (age 69 - eligible for retirement withdrawal)
INSERT INTO products (id, product_name, product_type, balance, portfolio_id) VALUES
('323e4567-e89b-12d3-a456-426614174200', 'Retirement Income Fund', 'RETIREMENT', 300000.00, '323e4567-e89b-12d3-a456-426614174102'),
('323e4567-e89b-12d3-a456-426614174201', 'Dividend Stocks', 'EQUITIES', 150000.00, '323e4567-e89b-12d3-a456-426614174102'),
('323e4567-e89b-12d3-a456-426614174202', 'Treasury Bonds', 'BONDS', 50000.00, '323e4567-e89b-12d3-a456-426614174102');

-- Insert products for Emily Brown (age 34)
INSERT INTO products (id, product_name, product_type, balance, portfolio_id) VALUES
('423e4567-e89b-12d3-a456-426614174200', 'Crypto Fund', 'INVESTMENT', 50000.00, '423e4567-e89b-12d3-a456-426614174103'),
('423e4567-e89b-12d3-a456-426614174201', 'Small Cap Fund', 'EQUITIES', 35000.00, '423e4567-e89b-12d3-a456-426614174103'),
('423e4567-e89b-12d3-a456-426614174202', 'Money Market', 'SAVINGS', 10000.00, '423e4567-e89b-12d3-a456-426614174103');

-- Insert sample withdrawals (using empty string '' instead of NULL for rejection_reason)
INSERT INTO withdrawals (id, amount, product_id, product_name, status, rejection_reason, investor_id, created_at, processed_at) VALUES
('123e4567-e89b-12d3-a456-426614174300', 10000.00, '123e4567-e89b-12d3-a456-426614174201', 'S&P 500 Index Fund', 'PROCESSED', '', '123e4567-e89b-12d3-a456-426614174000', '2024-01-15 10:30:00', '2024-01-16 14:20:00'),
('123e4567-e89b-12d3-a456-426614174301', 5000.00, '123e4567-e89b-12d3-a456-426614174202', 'High Yield Savings', 'APPROVED', '', '123e4567-e89b-12d3-a456-426614174000', '2024-02-01 09:15:00', '2024-02-02 11:00:00'),
('223e4567-e89b-12d3-a456-426614174300', 15000.00, '223e4567-e89b-12d3-a456-426614174200', 'Tech Growth Fund', 'PENDING', '', '223e4567-e89b-12d3-a456-426614174001', '2024-03-10 14:45:00', NULL),
('323e4567-e89b-12d3-a456-426614174300', 50000.00, '323e4567-e89b-12d3-a456-426614174200', 'Retirement Income Fund', 'APPROVED', '', '323e4567-e89b-12d3-a456-426614174002', '2024-01-20 11:20:00', '2024-01-21 09:30:00'),
('323e4567-e89b-12d3-a456-426614174301', 25000.00, '323e4567-e89b-12d3-a456-426614174201', 'Dividend Stocks', 'REJECTED', 'Insufficient documentation provided', '323e4567-e89b-12d3-a456-426614174002', '2024-02-15 08:00:00', '2024-02-16 16:45:00');

-- Update portfolio total balances
UPDATE portfolios SET total_balance = 250000.00 WHERE id = '123e4567-e89b-12d3-a456-426614174100';
UPDATE portfolios SET total_balance = 175000.00 WHERE id = '223e4567-e89b-12d3-a456-426614174101';
UPDATE portfolios SET total_balance = 500000.00 WHERE id = '323e4567-e89b-12d3-a456-426614174102';
UPDATE portfolios SET total_balance = 95000.00 WHERE id = '423e4567-e89b-12d3-a456-426614174103';