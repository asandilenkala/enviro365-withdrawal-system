#!/bin/bash
# restart.sh - Restart Docker containers

echo "========================================="
echo "Restarting Enviro365 containers..."
echo "========================================="

docker-compose restart

echo ""
echo "Containers restarted."
echo ""
echo "Check status: docker-compose ps"