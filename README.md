I'll provide the complete README.md with all endpoints, users, passwords, roles, and Docker instructions.

## Complete README.md

```markdown
# Enviro365 Withdrawal Management System

A full-stack application for managing investor withdrawals with business rule validation.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Installation](#installation)
- [Configuration](#configuration)
- [Database](#database)
- [Users & Credentials](#users--credentials)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Docker Deployment](#docker-deployment)
- [AI Usage Disclosure](#ai-usage-disclosure)
- [Screenshots](#screenshots)
- [License](#license)

## Overview

Enviro365 Investments is automating its withdrawal notice process to eliminate manual errors, improve efficiency and deliver a better investor experience. This system allows investors to view portfolios, submit withdrawals, validate rules, and download reports.

### Business Rules
1. **Retirement Withdrawal**: Only allowed if investor age > 65
2. **Balance Check**: Withdrawal must not exceed product balance
3. **90% Limit**: Withdrawal must not exceed 90% of non-retirement balance
4. **Proper Error Handling**: All validation errors are returned with clear messages

## Features

### Backend
- Retrieve investor portfolio with products
- Create withdrawal notices with balance calculations
- Export CSV statements with filtering
- Business rule validation
- Global exception handling
- Security with session-based authentication
- H2 database with UUID-based IDs
- REST API with Swagger documentation
- Role-based access control (ADMIN / INVESTOR)

### Frontend
- Portfolio dashboard with real-time balance calculations
- Withdrawal form with real-time validation
- Withdrawal history table with filtering and search
- CSV download button with date and status filters
- Responsive design
- Session management

## Technology Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17+ | Programming language |
| Spring Boot | 3.5.15 | Framework |
| Spring Security | 6.5.11 | Authentication & Authorization |
| Spring Data JPA | 3.5.12 | Database operations |
| H2 Database | 2.3.232 | In-memory database |
| MapStruct | 1.5.5.Final | DTO mapping |
| Lombok | 1.18.30 | Boilerplate reduction |
| Maven | 3.8+ | Build tool |
| SpringDoc OpenAPI | 2.6.0 | API documentation |

### Frontend
| Technology | Version | Purpose |
|------------|---------|---------|
| React | 18.2.0 | UI library |
| React Router DOM | 6.20.0 | Navigation |
| Axios | 1.6.2 | HTTP client |
| CSS3 | - | Styling |

### DevOps
| Technology | Purpose |
|------------|---------|
| Docker | Containerization |
| Docker Compose | Multi-container orchestration |

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Frontend (React)                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │   Dashboard   │  │ Withdrawal   │  │    History       │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
│  ┌────────────────────────────────────────────────────────┐  │
│  │              CSV Report Downloader                     │  │
│  └────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    API Gateway (Port 8080)                  │
│  ┌────────────────────────────────────────────────────────┐  │
│  │              Spring Security (Session Auth)            │  │
│  └────────────────────────────────────────────────────────┘  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │  Portfolio   │  │  Withdrawal  │  │    Report        │  │
│  │  Controller  │  │  Controller  │  │   Controller     │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
│  ┌────────────────────────────────────────────────────────┐  │
│  │              Service Layer                             │  │
│  │  PortfolioService  │  WithdrawalService  │  ReportService │
│  │  ValidationService │  SessionService     │  UserContext   │
│  └────────────────────────────────────────────────────────┘  │
│  ┌────────────────────────────────────────────────────────┐  │
│  │              Repository Layer (JPA)                    │  │
│  └────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                    H2 Database (In-Memory)                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │   Investors  │  │  Portfolios  │  │    Products      │  │
│  └──────────────┘  └──────────────┘  └──────────────────┘  │
│  ┌──────────────┐  ┌──────────────┐                        │
│  │    Users     │  │  Withdrawals │                        │
│  └──────────────┘  └──────────────┘                        │
└─────────────────────────────────────────────────────────────┘
```

## Installation

### Prerequisites
- Java 17 or higher
- Node.js 18 or higher
- Maven 3.8 or higher
- Docker (optional)
- Git

### Clone the Repository
```bash
git clone <repository-url>
cd enviro365-withdrawal-system
```

### Backend Setup
```bash
cd enviro365-withdrawal-system-backend

# Build the application
mvn clean install

# Run the application
mvn spring-boot:run

# Or run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Frontend Setup
```bash
cd enviro365-withdrawal-system-frontend

# Install dependencies
npm install

# Start development server
npm start

# Build for production
npm run build
```

## Configuration

### Backend Configuration (application.yml)
```yaml
spring:
  application:
    name: enviro365-withdrawal-system-backend
  server:
    port: ${SERVER_PORT:8080}
  datasource:
    url: ${DB_URL:jdbc:h2:mem:enviro365db}
    driver-class-name: org.h2.Driver
    username: ${DB_USERNAME:sa}
    password: ${DB_PASSWORD:password}
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    defer-datasource-initialization: true
  sql:
    init:
      mode: always
      data-locations: classpath:data.sql
      schema-locations: classpath:schema.sql
```

### Environment Variables (.env)

**Backend (.env)**
```env
# Server Configuration
SERVER_PORT=8080

# Database Configuration
DB_URL=jdbc:h2:mem:enviro365db
DB_USERNAME=enviro_database
DB_PASSWORD=enviro365_secure_password_2024

# CORS Configuration
CORS_ORIGINS=http://localhost:3000,http://localhost:3001

# File Export Settings
CSV_EXPORT_DIR=./exports
CSV_MAX_SIZE=10MB

# Logging
LOG_LEVEL=DEBUG
```

**Frontend (.env)**
```env
# API Configuration
REACT_APP_API_URL=http://localhost:8080/api/v1
REACT_APP_API_TIMEOUT=30000

# Application Settings
REACT_APP_VERSION=1.0.0
REACT_APP_ENVIRONMENT=development

# Feature Flags
REACT_APP_ENABLE_CSV_EXPORT=true
REACT_APP_ENABLE_REAL_TIME_VALIDATION=true
```

## Database

### H2 Console Access
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:enviro365db`
- **Username**: `enviro_database`
- **Password**: `enviro365_secure_password_2024`

### Database Schema
```sql
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
    CONSTRAINT fk_user_investor FOREIGN KEY (investor_id) REFERENCES investors(id)
);

-- Portfolios table
CREATE TABLE portfolios (
    id UUID PRIMARY KEY,
    portfolio_name VARCHAR(255) NOT NULL,
    total_balance DECIMAL(19,2) NOT NULL DEFAULT 0,
    investor_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_portfolio_investor FOREIGN KEY (investor_id) REFERENCES investors(id)
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
    CONSTRAINT fk_product_portfolio FOREIGN KEY (portfolio_id) REFERENCES portfolios(id)
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
    CONSTRAINT fk_withdrawal_investor FOREIGN KEY (investor_id) REFERENCES investors(id)
);
```

## Users & Credentials

### Default Users

| Username | Password | Role | Investor | Permissions |
|----------|----------|------|----------|-------------|
| `enviro_admin` | `enviro365_2024` | **ADMIN** | None | Full system access, view all portfolios, approve/reject withdrawals |
| `john_smith` | `enviro365_2024` | **INVESTOR** | John Smith | View own portfolio, submit withdrawals, view own history |
| `sarah_johnson` | `enviro365_2024` | **INVESTOR** | Sarah Johnson | View own portfolio, submit withdrawals, view own history |
| `michael_williams` | `enviro365_2024` | **INVESTOR** | Michael Williams | View own portfolio, submit withdrawals, view own history |
| `emily_brown` | `enviro365_2024` | **INVESTOR** | Emily Brown | View own portfolio, submit withdrawals, view own history |

### Investor Details

| Investor | Age | Retirement Eligible | Portfolio Value |
|----------|-----|-------------------|-----------------|
| John Smith | 54 | No (Age < 65) | R250,000.00 |
| Sarah Johnson | 39 | No (Age < 65) | R175,000.00 |
| Michael Williams | 69 | Yes (Age > 65) | R500,000.00 |
| Emily Brown | 34 | No (Age < 65) | R95,000.00 |

### Test Withdrawal Scenarios

| Scenario | Investor | Product | Amount | Expected Result |
|----------|----------|---------|--------|-----------------|
| Valid withdrawal | John Smith | S&P 500 Index Fund | R5,000.00 | Success |
| Exceeds balance | John Smith | High Yield Savings | R30,000.00 | Rejected (exceeds balance) |
| Exceeds 90% | Sarah Johnson | Tech Growth Fund | R95,000.00 | Rejected (exceeds 90%) |
| Retirement age < 65 | John Smith | Retirement Fund 2030 | R10,000.00 | Rejected (age restriction) |
| Retirement age > 65 | Michael Williams | Retirement Income Fund | R50,000.00 | Success |

## API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Authentication

All endpoints except `/auth/login` require authentication via Session ID header.

**Headers:**
```
X-Session-Id: {session_id_from_login}
Content-Type: application/json
```

---

### 1. Authentication Endpoints

#### Login
```http
POST /auth/login
```

**Request Body:**
```json
{
  "username": "enviro_admin",
  "password": "enviro365_2024"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "userId": "123e4567-e89b-12d3-a456-426614174500",
    "username": "enviro_admin",
    "email": "admin@enviro365.com",
    "role": "ADMIN",
    "investorId": null,
    "investorName": null,
    "sessionId": "abc-123-def-456",
    "authenticated": true,
    "message": "Login successful"
  },
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

#### Get Current User Info
```http
GET /auth/user-info
Headers: X-Session-Id: {session_id}
```

**Response:**
```json
{
  "success": true,
  "message": "User info retrieved successfully",
  "data": {
    "userId": "123e4567-e89b-12d3-a456-426614174500",
    "username": "enviro_admin",
    "email": "admin@enviro365.com",
    "role": "ADMIN",
    "investorId": null,
    "investorName": null,
    "sessionId": "abc-123-def-456",
    "authenticated": true
  },
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

#### Logout
```http
POST /auth/logout
Headers: X-Session-Id: {session_id}
```

**Response:**
```json
{
  "success": true,
  "message": "Logout successful",
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

---

### 2. Portfolio Endpoints

#### Get Portfolio by Investor ID
```http
GET /portfolios/investor/{investorId}
Headers: X-Session-Id: {session_id}
```

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| investorId | UUID | Investor's UUID |

**Response:**
```json
{
  "success": true,
  "message": "Portfolio retrieved successfully",
  "data": {
    "portfolioId": "123e4567-e89b-12d3-a456-426614174100",
    "portfolioName": "John Smith Retirement Portfolio",
    "totalBalance": 250000.00,
    "availableForWithdrawal": 100000.00,
    "maxWithdrawalAmount": 90000.00,
    "products": [
      {
        "productId": "123e4567-e89b-12d3-a456-426614174200",
        "productName": "Retirement Fund 2030",
        "productType": "RETIREMENT",
        "balance": 150000.00,
        "isRetirementProduct": true
      }
    ],
    "investor": {
      "investorId": "123e4567-e89b-12d3-a456-426614174000",
      "fullName": "John Smith",
      "email": "john.smith@email.com",
      "age": 54,
      "canWithdrawRetirement": false
    }
  },
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

#### Get All Portfolios (Admin Only)
```http
GET /portfolios
Headers: X-Session-Id: {session_id}
```

**Response:**
```json
{
  "success": true,
  "message": "Portfolios retrieved successfully",
  "data": [
    {
      "portfolioId": "123e4567-e89b-12d3-a456-426614174100",
      "portfolioName": "John Smith Retirement Portfolio",
      "totalBalance": 250000.00,
      "availableForWithdrawal": 100000.00,
      "maxWithdrawalAmount": 90000.00,
      "investor": {
        "investorId": "123e4567-e89b-12d3-a456-426614174000",
        "fullName": "John Smith",
        "email": "john.smith@email.com",
        "age": 54,
        "canWithdrawRetirement": false
      }
    }
  ],
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

---

### 3. Withdrawal Endpoints

#### Create Withdrawal Request
```http
POST /withdrawals
Headers: X-Session-Id: {session_id}
```

**Request Body:**
```json
{
  "investorId": "123e4567-e89b-12d3-a456-426614174000",
  "productId": "123e4567-e89b-12d3-a456-426614174201",
  "productName": "S&P 500 Index Fund",
  "amount": 5000.00,
  "notes": "Emergency withdrawal"
}
```

**Response (Success):**
```json
{
  "success": true,
  "message": "Withdrawal request created successfully",
  "data": {
    "withdrawalId": "123e4567-e89b-12d3-a456-426614174300",
    "amount": 5000.00,
    "formattedAmount": "R5,000.00",
    "productId": "123e4567-e89b-12d3-a456-426614174201",
    "productName": "S&P 500 Index Fund",
    "status": "PENDING",
    "statusDescription": "Pending Approval",
    "rejectionReason": null,
    "investorName": "John Smith",
    "createdAt": "2026-06-16T18:41:10",
    "processedAt": null,
    "canBeProcessed": true
  },
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

**Response (Validation Error):**
```json
{
  "success": false,
  "message": "Withdrawal amount exceeds product balance of R75,000.00",
  "errorCode": "INSUFFICIENT_BALANCE",
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

#### Get Withdrawal History
```http
GET /withdrawals/investor/{investorId}
Headers: X-Session-Id: {session_id}
```

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| investorId | UUID | Investor's UUID |

**Response:**
```json
{
  "success": true,
  "message": "Withdrawal history retrieved successfully",
  "data": [
    {
      "withdrawalId": "123e4567-e89b-12d3-a456-426614174300",
      "amount": 10000.00,
      "formattedAmount": "R10,000.00",
      "productId": "123e4567-e89b-12d3-a456-426614174201",
      "productName": "S&P 500 Index Fund",
      "status": "PROCESSED",
      "statusDescription": "Processed",
      "rejectionReason": "",
      "investorName": "John Smith",
      "createdAt": "2024-01-15T10:30:00",
      "processedAt": "2024-01-16T14:20:00",
      "canBeProcessed": false
    }
  ],
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

#### Update Withdrawal Status (Admin Only)
```http
PUT /withdrawals/{withdrawalId}/status?status={status}&rejectionReason={reason}
Headers: X-Session-Id: {session_id}
```

**Query Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| withdrawalId | UUID | Withdrawal UUID |
| status | String | APPROVED, REJECTED, PROCESSED, CANCELLED |
| rejectionReason | String | Reason for rejection (required if status=REJECTED) |

**Response:**
```json
{
  "success": true,
  "message": "Withdrawal status updated successfully",
  "data": {
    "withdrawalId": "123e4567-e89b-12d3-a456-426614174300",
    "amount": 10000.00,
    "formattedAmount": "R10,000.00",
    "status": "APPROVED",
    "statusDescription": "Approved",
    "rejectionReason": "",
    "processedAt": "2026-06-16T18:41:10"
  },
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

---

### 4. Report Endpoints

#### Generate Investor Withdrawal Report
```http
POST /reports/withdrawals/investor/{investorId}
Headers: X-Session-Id: {session_id}
Content-Type: application/json
```

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| investorId | UUID | Investor's UUID |

**Request Body (Optional Filters):**
```json
{
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-12-31T23:59:59",
  "status": "APPROVED",
  "minAmount": 1000.00,
  "maxAmount": 50000.00
}
```

**Response:** CSV file download

#### Generate All Withdrawals Report (Admin Only)
```http
POST /reports/withdrawals/all
Headers: X-Session-Id: {session_id}
Content-Type: application/json
```

**Request Body (Optional Filters):** Same as above

**Response:** CSV file download

---

### Error Responses

#### 400 Bad Request - Validation Error
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "amount": "Withdrawal amount must be greater than 0",
    "productId": "Please select a product"
  },
  "errorCode": "VALIDATION_ERROR",
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

#### 401 Unauthorized
```json
{
  "success": false,
  "message": "Invalid or expired session",
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

#### 403 Forbidden
```json
{
  "success": false,
  "message": "Access denied. Admin role required",
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

#### 404 Not Found
```json
{
  "success": false,
  "message": "Investor not found with id: 123e4567-e89b-12d3-a456-426614174000",
  "errorCode": "RESOURCE_NOT_FOUND",
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

#### 500 Internal Server Error
```json
{
  "success": false,
  "message": "An unexpected error occurred. Please try again later.",
  "errorCode": "INTERNAL_SERVER_ERROR",
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

### Status Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 201 | Created |
| 400 | Bad Request / Validation Error |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 500 | Internal Server Error |

## Testing

### Backend Tests
```bash
cd enviro365-withdrawal-system-backend

# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=PortfolioServiceTest

# Run with coverage report
mvn test jacoco:report
```

### API Testing with cURL

**1. Login**
```bash
curl -X POST "http://localhost:8080/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"enviro_admin","password":"enviro365_2024"}'
```

**2. Get Portfolio (as Admin)**
```bash
# First, save the session ID from login response
SESSION_ID="your-session-id-from-login"

curl -X GET "http://localhost:8080/api/v1/portfolios/investor/123e4567-e89b-12d3-a456-426614174000" \
  -H "X-Session-Id: $SESSION_ID"
```

**3. Create Withdrawal**
```bash
curl -X POST "http://localhost:8080/api/v1/withdrawals" \
  -H "Content-Type: application/json" \
  -H "X-Session-Id: $SESSION_ID" \
  -d '{
    "investorId": "123e4567-e89b-12d3-a456-426614174000",
    "productId": "123e4567-e89b-12d3-a456-426614174201",
    "productName": "S&P 500 Index Fund",
    "amount": 5000.00,
    "notes": "Test withdrawal"
  }'
```

**4. Get Withdrawal History**
```bash
curl -X GET "http://localhost:8080/api/v1/withdrawals/investor/123e4567-e89b-12d3-a456-426614174000" \
  -H "X-Session-Id: $SESSION_ID"
```

**5. Generate Report**
```bash
curl -X POST "http://localhost:8080/api/v1/reports/withdrawals/investor/123e4567-e89b-12d3-a456-426614174000" \
  -H "Content-Type: application/json" \
  -H "X-Session-Id: $SESSION_ID" \
  -d '{"status":"APPROVED"}' \
  --output withdrawal_report.csv
```

### Frontend Testing
```bash
cd enviro365-withdrawal-system-frontend

# Run tests
npm test

# Run with coverage
npm test -- --coverage
```

## Docker Deployment

### Docker Compose (Recommended)

**1. Build and Start All Services**
```bash
docker-compose up --build
```

**2. Stop All Services**
```bash
docker-compose down
```

**3. Stop and Remove Volumes**
```bash
docker-compose down -v
```

**4. View Logs**
```bash
docker-compose logs -f
docker-compose logs -f backend
docker-compose logs -f frontend
```

### Individual Docker Builds

**Backend:**
```bash
cd enviro365-withdrawal-system-backend
docker build -t enviro365-backend .
docker run -p 8080:8080 enviro365-backend
```

**Frontend:**
```bash
cd enviro365-withdrawal-system-frontend
docker build -t enviro365-frontend .
docker run -p 3000:80 enviro365-frontend
```

### Access Points After Docker Deployment

| Service | URL |
|---------|-----|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
| H2 Console | http://localhost:8080/h2-console |
| Swagger UI | http://localhost:8080/swagger-ui.html |

## AI Usage Disclosure

This project was developed with assistance from AI tools including:
- **GitHub Copilot** - Code completion and suggestions
- **ChatGPT** - Architectural decisions, code review, and debugging
- All AI-generated code has been reviewed and understood by the developer

## Screenshots

### Login Screen
![Login Screen](screenshots/login.png)

### Portfolio Dashboard
![Portfolio Dashboard](screenshots/dashboard.png)

### Withdrawal Form
![Withdrawal Form](screenshots/withdrawal.png)

### Withdrawal History
![Withdrawal History](screenshots/history.png)

### CSV Export
![CSV Export](screenshots/report.png)

## Project Structure

```
enviro365-withdrawal-system/
├── enviro365-withdrawal-system-backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/enviro/assessment/junior/asandile/
│   │   │   │   ├── config/         # Configuration classes
│   │   │   │   ├── controller/     # REST Controllers
│   │   │   │   ├── dto/           # Data Transfer Objects
│   │   │   │   ├── exception/     # Exception handlers
│   │   │   │   ├── mapper/        # MapStruct mappers
│   │   │   │   ├── model/         # Entity models
│   │   │   │   ├── repository/    # JPA repositories
│   │   │   │   ├── service/       # Business logic
│   │   │   │   └── util/          # Utility classes
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── data.sql
│   │   │       └── schema.sql
│   │   └── test/                   # Unit tests
│   ├── Dockerfile
│   └── pom.xml
├── enviro365-withdrawal-system-frontend/
│   ├── src/
│   │   ├── components/    # React components
│   │   ├── hooks/         # Custom hooks
│   │   ├── services/      # API services
│   │   ├── styles/        # CSS styles
│   │   └── utils/         # Utilities
│   ├── public/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
├── docker-compose.yml
├── .gitignore
└── README.md
```

### Project Structure with Single Docker Management

```
enviro365-withdrawal-system/
├── enviro365-withdrawal-system-backend/
│   ├── src/
│   ├── Dockerfile          # Backend Dockerfile
│   └── pom.xml
├── enviro365-withdrawal-system-frontend/
│   ├── src/
│   ├── Dockerfile          # Frontend Dockerfile
│   ├── nginx.conf
│   └── package.json
├── docker-compose.yml      # Single compose file at root
├── .dockerignore           # Root level
├── start.sh
├── stop.sh
└── README.md
```

## License

Proprietary - Enviro365 Investments

## Author

**Asandile**
- Email: asandile@enviro365.com

## Support

For support, please contact the development team or create an issue in the repository.

---

**Last Updated:** June 2026
**Version:** 1.0.0


## Docker Compose File (docker-compose.yml)

yaml
version: '3.8'

services:
  backend:
    build:
      context: ./enviro365-withdrawal-system-backend
      dockerfile: Dockerfile
    container_name: enviro365-backend
    ports:
      - "8080:8080"
    environment:
      - SERVER_PORT=8080
      - DB_URL=jdbc:h2:mem:enviro365db
      - DB_USERNAME=enviro_database
      - DB_PASSWORD=enviro365_secure_password_2024
      - CORS_ORIGINS=http://localhost:3000,http://localhost:3001
      - CSV_EXPORT_DIR=./exports
      - CSV_MAX_SIZE=10MB
    volumes:
      - ./exports:/app/exports
      - ./logs:/app/logs
    networks:
      - enviro365-network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s

  frontend:
    build:
      context: ./enviro365-withdrawal-system-frontend
      dockerfile: Dockerfile
    container_name: enviro365-frontend
    ports:
      - "3000:80"
    depends_on:
      backend:
        condition: service_healthy
    environment:
      - REACT_APP_API_URL=http://localhost:8080/api/v1
    networks:
      - enviro365-network

networks:
  enviro365-network:
    driver: bridge
# Enviro365 Withdrawal Management System

A full-stack application for managing investor withdrawals with business rule validation.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Installation](#installation)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Docker Deployment](#docker-deployment)
- [AI Usage Disclosure](#ai-usage-disclosure)
- [Screenshots](#screenshots)
- [License](#license)

## Overview

Enviro365 Investments is automating its withdrawal notice process to eliminate manual errors, improve efficiency and deliver a better investor experience. This system allows investors to view portfolios, submit withdrawals, validate rules, and download reports.

## Features

### Backend
- Retrieve investor portfolio with products
- Create withdrawal notices with balance calculations
- Export CSV statements with filtering
- Business rule validation
- Global exception handling
- Security with session-based authentication
- H2 database with UUID-based IDs
- REST API with Swagger documentation

### Frontend
- Portfolio dashboard
- Withdrawal form with real-time validation
- Withdrawal history table with filtering
- CSV download button
- Responsive design

### Business Rules
- Retirement withdrawal only allowed if age > 65
- Withdrawal must not exceed balance
- Withdrawal must not exceed 90% of balance
- Proper error handling and user feedback

## Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.x** - Framework
- **Spring Security** - Authentication
- **Spring Data JPA** - Database operations
- **H2 Database** - In-memory database
- **MapStruct** - DTO mapping
- **Lombok** - Boilerplate reduction
- **Maven** - Build tool

### Frontend
- **React 18** - UI library
- **React Router DOM** - Navigation
- **Axios** - HTTP client
- **CSS3** - Styling

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

## Installation

### Prerequisites
- Java 17+
- Node.js 18+
- Maven 3.8+
- Docker (optional)

### Backend Setup
bash
cd enviro365-withdrawal-system-backend
mvn clean install
mvn spring-boot:run

---

## AI Usage Disclosure

This project was developed with assistance from **DeepSeek** AI tool. The AI was used for:

### Backend Assistance
- **Configuration files** - SecurityConfig, CorsConfig, and application.yml structure
- **Exception handling** - GlobalExceptionHandler and custom exception classes
- **Docker files** - Dockerfiles and docker-compose.yml configuration

### My Own Work
- **Project structure** - All directory organization and package structure
- **Business logic** - Service layer implementation and validation rules
- **Frontend** - Complete React components, hooks, and styling
- **Database** - Schema design, entity relationships, and data.sql
- **Integration** - Connecting frontend to backend APIs
- **Testing** - Unit tests for services and controllers
- **Documentation** - README and API documentation

### Understanding
I reviewed, tested, and fully understand all AI-generated code before implementation. All code follows the project's coding standards and business requirements.

---

**DeepSeek** was the only AI tool used in this project.
