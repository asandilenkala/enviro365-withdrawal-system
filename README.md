Here's the final, clean README.md without duplication:

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
| Java | 21 | Programming language |
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
- Java 21 or higher
- Node.js 18 or higher
- Maven 3.8 or higher
- Docker (optional)

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
    "userId": "123e4567-...",
    "username": "enviro_admin",
    "email": "admin@enviro365.com",
    "role": "ADMIN",
    "sessionId": "abc-123-def-456",
    "authenticated": true
  },
  "timestamp": "2026-06-16T18:41:10.814179"
}
```

#### Get Current User Info
```http
GET /auth/user-info
Headers: X-Session-Id: {session_id}
```

#### Logout
```http
POST /auth/logout
Headers: X-Session-Id: {session_id}
```

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

#### Get All Portfolios (Admin Only)
```http
GET /portfolios
Headers: X-Session-Id: {session_id}
```

### 3. Withdrawal Endpoints

#### Create Withdrawal Request
```http
POST /withdrawals
Headers: X-Session-Id: {session_id}
```

**Request Body:**
```json
{
  "investorId": "123e4567-...",
  "productId": "123e4567-...",
  "productName": "S&P 500 Index Fund",
  "amount": 5000.00,
  "notes": "Emergency withdrawal"
}
```

#### Get Withdrawal History
```http
GET /withdrawals/investor/{investorId}
Headers: X-Session-Id: {session_id}
```

#### Update Withdrawal Status (Admin Only)
```http
PUT /withdrawals/{withdrawalId}/status?status={status}&rejectionReason={reason}
Headers: X-Session-Id: {session_id}
```

### 4. Report Endpoints

#### Generate Investor Withdrawal Report
```http
POST /reports/withdrawals/investor/{investorId}
Headers: X-Session-Id: {session_id}
```

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

#### Generate All Withdrawals Report (Admin Only)
```http
POST /reports/withdrawals/all
Headers: X-Session-Id: {session_id}
```

### Error Responses

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
mvn test
mvn test -Dtest=PortfolioServiceTest
mvn test jacoco:report
```

### API Testing with cURL
```bash
# Login
curl -X POST "http://localhost:8080/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"enviro_admin","password":"enviro365_2024"}'

# Get Portfolio
curl -X GET "http://localhost:8080/api/v1/portfolios/investor/123e4567-e89b-12d3-a456-426614174000" \
  -H "X-Session-Id: $SESSION_ID"

# Create Withdrawal
curl -X POST "http://localhost:8080/api/v1/withdrawals" \
  -H "Content-Type: application/json" \
  -H "X-Session-Id: $SESSION_ID" \
  -d '{
    "investorId": "123e4567-...",
    "productId": "123e4567-...",
    "productName": "S&P 500 Index Fund",
    "amount": 5000.00
  }'
```

### Frontend Testing
```bash
cd enviro365-withdrawal-system-frontend
npm test
npm test -- --coverage
```

## Docker Deployment

### Docker Compose (Recommended)

**Build and Start:**
```bash
docker-compose up --build
```

**Stop Services:**
```bash
docker-compose down
```

**Stop and Remove Volumes:**
```bash
docker-compose down -v
```

**View Logs:**
```bash
docker-compose logs -f
docker-compose logs -f backend
docker-compose logs -f frontend
```

### Individual Docker Builds
```bash
# Backend
cd enviro365-withdrawal-system-backend
docker build -t enviro365-backend .
docker run -p 8080:8080 enviro365-backend

# Frontend
cd enviro365-withdrawal-system-frontend
docker build -t enviro365-frontend .
docker run -p 3000:80 enviro365-frontend
```

### Access Points After Deployment

| Service | URL |
|---------|-----|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
| H2 Console | http://localhost:8080/h2-console |
| Swagger UI | http://localhost:8080/swagger-ui.html |

## AI Usage Disclosure

This project was developed with assistance from **DeepSeek** AI tool. The AI was used for:

### Backend Assistance
- Configuration files (SecurityConfig, CorsConfig)
- Exception handling (GlobalExceptionHandler)
- Docker files (Dockerfiles and docker-compose.yml)

### My Own Work
- Project structure and organization
- Business logic (Service layer and validation rules)
- Complete React frontend components
- Database schema design
- Integration between frontend and backend
- Unit tests
- Documentation

All AI-generated code has been reviewed, tested, and fully understood before implementation.

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
│   │   │   │   ├── config/
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── exception/
│   │   │   │   ├── mapper/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   ├── service/
│   │   │   │   └── util/
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── data.sql
│   │   │       └── schema.sql
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
├── enviro365-withdrawal-system-frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── hooks/
│   │   ├── services/
│   │   ├── styles/
│   │   └── utils/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
├── docker-compose.yml
├── .gitignore
└── README.md
```

## License

Proprietary - Enviro365 Investments

## Author

**Asandile**
- Email: asandile@enviro365.com

---

**Last Updated:** June 2026
**Version:** 1.0.0
```

This README is now clean, organized, and contains no duplicated content. All sections are properly structured with the most important information highlighted.
