#!/bin/bash
# clean.sh - Clean Docker containers, images, and volumes

echo "========================================="
echo "Cleaning Enviro365 Docker Resources"
echo "========================================="
echo ""
echo "This will remove:"
echo "  - All containers"
echo "  - All images"
echo "  - All volumes"
echo "  - All networks"
echo ""
read -p "Are you sure? (y/N): " confirm

if [[ $confirm == [yY] || $confirm == [yY][eE][sS] ]]; then
    echo "Stopping and removing containers..."
    docker-compose down -v --rmi all
    echo ""
    echo "Removing networks..."
    docker network prune -f
    echo ""
    echo "========================================="
    echo "Clean complete!"
    echo "========================================="
else
    echo "Clean cancelled."
fi