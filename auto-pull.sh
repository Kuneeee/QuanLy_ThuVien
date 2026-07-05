#!/bin/bash

# Script tự động pull code từ repository
echo "Đang kiểm tra và pull code mới nhất..."

# Kiểm tra xem có phải git repository không
if [ -d ".git" ]; then
    echo "Repository được tìm thấy. Đang pull code..."
    
    # Pull code từ origin main
    git pull origin main
    
    if [ $? -eq 0 ]; then
        echo "✅ Pull code thành công!"
    else
        echo "❌ Có lỗi xảy ra khi pull code"
    fi
else
    echo "❌ Không phải git repository"
fi

echo "Hoàn thành!"
