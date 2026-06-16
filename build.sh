#!/bin/bash
# build.sh - Build and run Docker containers

echo "========================================="
echo "Enviro365 Withdrawal Management System"
echo "========================================="

# Clean old containers and images
echo "Cleaning old containers..."
docker-compose down -v 2>/dev/null

# Remove old images
echo "Removing old images..."
docker rmi enviro365-backend enviro365-frontend 2>/dev/null

# Build and start
echo "Building and starting containers..."
docker-compose up -d --build

echo ""
echo "Waiting for services to start..."
sleep 15

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
echo "  Stop:          docker-compose down"
echo "  Status:        docker-compose ps"
echo "  Rebuild:       docker-compose up -d --build --force-recreate"