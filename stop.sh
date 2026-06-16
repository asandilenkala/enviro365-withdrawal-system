#!/bin/bash
# stop.sh - Stop Docker containers

echo "========================================="
echo "Stopping Enviro365 containers..."
echo "========================================="

docker-compose down

echo ""
echo "Containers stopped."
echo ""
echo "To remove volumes (cleanup):"
echo "  docker-compose down -v"