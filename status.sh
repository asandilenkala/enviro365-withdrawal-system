#!/bin/bash
# status.sh - Check Docker container status

echo "========================================="
echo "Enviro365 Container Status"
echo "========================================="
echo ""

docker-compose ps

echo ""
echo "========================================="
echo "Container Health"
echo "========================================="

# Check backend health
if curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health | grep -q "200"; then
    echo "✓ Backend: Healthy"
else
    echo "✗ Backend: Unhealthy"
fi

# Check frontend
if curl -s -o /dev/null -w "%{http_code}" http://localhost:3000 | grep -q "200"; then
    echo "✓ Frontend: Healthy"
else
    echo "✗ Frontend: Unhealthy"
fi