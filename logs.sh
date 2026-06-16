#!/bin/bash
# logs.sh - View Docker logs

echo "========================================="
echo "Enviro365 Logs"
echo "========================================="
echo ""
echo "Options:"
echo "  1) All logs (follow)"
echo "  2) Backend only"
echo "  3) Frontend only"
echo "  4) Exit"
echo ""
read -p "Select option (1-4): " option

case $option in
    1)
        docker-compose logs -f
        ;;
    2)
        docker-compose logs -f backend
        ;;
    3)
        docker-compose logs -f frontend
        ;;
    4)
        exit 0
        ;;
    *)
        echo "Invalid option"
        ;;
esac