#!/bin/bash
# start.sh - Quick start script for Docker deployment

echo "========================================="
echo "Enviro365 Withdrawal Management System"
echo "========================================="
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "Error: Docker is not running. Please start Docker first."
    exit 1
fi

# Check if docker-compose exists
if ! command -v docker-compose &> /dev/null; then
    echo "Error: docker-compose not found. Please install docker-compose."
    exit 1
fi

echo "Building and starting containers..."
echo ""

# Build and start with docker-compose
docker-compose up -d --build

# Wait for services to be ready
echo "Waiting for services to be ready..."
sleep 5

# Check if containers are running
if docker ps | grep -q "enviro365-backend" && docker ps | grep -q "enviro365-frontend"; then
    echo ""
    echo "========================================="
    echo "Deployment Complete!"
    echo "========================================="
    echo ""
    echo "Access the application:"
    echo "  Frontend:      http://localhost:3000"
    echo "  Backend API:   http://localhost:8080"
    echo "  H2 Console:    http://localhost:8080/h2-console"
    echo "  Swagger UI:    http://localhost:8080/swagger-ui.html"
    echo ""
    echo "Default Credentials:"
    echo "  Username: enviro_admin"
    echo "  Password: enviro365_2024"
    echo ""
    echo "Useful Commands:"
    echo "  View logs:     docker-compose logs -f"
    echo "  Stop:          ./stop.sh"
    echo "  Restart:       docker-compose restart"
else
    echo ""
    echo "========================================="
    echo "Error: Containers failed to start!"
    echo "========================================="
    echo "Check logs with: docker-compose logs"
    exit 1
fi