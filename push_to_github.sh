#!/bin/bash

# ==========================================
# 开源数据采集系统 - GitHub推送脚本
# ==========================================

# 请修改以下配置
GITHUB_USERNAME="YOUR_USERNAME"  # 替换为您的GitHub用户名
GITHUB_EMAIL="your-email@example.com"  # 替换为您的邮箱
GITHUB_TOKEN="YOUR_TOKEN"  # 替换为您的GitHub Personal Access Token

# 仓库名称
REPO_NAME="data-collection-api"
REPO_URL="https://github.com/${GITHUB_USERNAME}/${REPO_NAME}.git"

echo "=========================================="
echo "开始推送代码到GitHub"
echo "=========================================="

# 进入项目目录
cd /workspace/data-collection-api

# 配置Git
echo "1. 配置Git..."
git config user.email "${GITHUB_EMAIL}"
git config user.name "${GITHUB_USERNAME}"

# 检查是否已初始化Git
if [ ! -d ".git" ]; then
    echo "2. 初始化Git仓库..."
    git init
    git add .
fi

# 提交代码
echo "3. 提交代码..."
git add .
git commit -m "Initial commit: 开源数据采集系统后端服务

功能模块:
- 用户认证模块: JWT认证、验证码、UKey设备支持
- 信源管理模块: 人物、组织、网站、社交账号管理
- 任务管理模块: 任务创建、调度、监控
- 节点管理模块: 分布式节点管理、心跳监控
- 数据中心模块: 数据检索、导出
- 监控预警模块: 异常检测、预警通知
- 统计分析模块: 数据统计、趋势分析

技术栈:
- Spring Boot 3.2.1 + Java 17
- Spring Data JPA + MySQL/H2
- Spring Security + JWT
- Swagger/OpenAPI 3

文档:
- 数据库设计文档
- 接口文档 (67个API)
- 详细设计文档
- 数据库SQL脚本"

# 添加远程仓库
echo "4. 添加远程仓库..."
git remote remove origin 2>/dev/null
git remote add origin https://oauth2:${GITHUB_TOKEN}@github.com/${GITHUB_USERNAME}/${REPO_NAME}.git

# 设置分支并推送
echo "5. 推送到GitHub..."
git branch -M main
git push -u origin main

echo "=========================================="
echo "✅ 推送完成！"
echo "=========================================="
echo "仓库地址: https://github.com/${GITHUB_USERNAME}/${REPO_NAME}"
