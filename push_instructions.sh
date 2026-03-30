#!/bin/bash

# ==========================================
# 开源数据采集系统 - GitHub推送脚本
# ==========================================
# 您的GitHub用户名: WangLee266
# 仓库名称: data-collection-api
# ==========================================

echo "=========================================="
echo "推送代码到 GitHub"
echo "=========================================="
echo ""
echo "请按以下步骤操作："
echo ""
echo "步骤1: 在GitHub上创建仓库"
echo "  1. 访问: https://github.com/new"
echo "  2. Repository name: data-collection-api"
echo "  3. Description: 开源数据采集系统后端服务"
echo "  4. 选择 Public"
echo "  5. 【重要】不要勾选任何初始化选项"
echo "  6. 点击 Create repository"
echo ""
echo "步骤2: 创建仓库后，执行以下命令推送代码"
echo ""

# 显示推送命令
cat << 'PUSH_COMMANDS'
cd /workspace/data-collection-api

# 方法1: 使用GitHub CLI (推荐，需要先登录)
# gh auth login
# gh repo create data-collection-api --public --source=. --push

# 方法2: 使用Personal Access Token
# 1. 访问 https://github.com/settings/tokens
# 2. Generate new token (classic)
# 3. 勾选 repo 权限
# 4. 生成Token后执行:

git remote add origin https://github.com/WangLee266/data-collection-api.git
git branch -M main
git push -u origin main

# 如果需要Token认证:
# git remote set-url origin https://oauth2:YOUR_TOKEN@github.com/WangLee266/data-collection-api.git
# git push -u origin main
PUSH_COMMANDS

echo ""
echo "=========================================="
echo "仓库地址: https://github.com/WangLee266/data-collection-api"
echo "=========================================="
