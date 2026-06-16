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
```bash
cd enviro365-withdrawal-system-backend
mvn clean install
mvn spring-boot:run
