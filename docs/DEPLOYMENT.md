# VincentCRM 部署文档

## 1. 概述

VincentCRM 是一个客户关系管理系统，采用前后端分离架构：

| 组件 | 技术栈 | 说明 |
|------|--------|------|
| 前端 | Vue 3 + Vite + Naive UI | 桌面端 Web 应用，Nginx 托管 |
| 后端 | Spring Boot 3.5.14 + Java 17 | RESTful API 服务 |
| 数据库 | MySQL 8.0 | 业务数据存储，UTF8MB4 编码 |
| 缓存 | Redis 7 | 会话缓存与数据缓存 |

所有组件通过 Docker 容器化部署，使用 Docker Compose 编排。

**架构示意：**

```
客户端 → Nginx(前端 + 反向代理) → 后端 API → MySQL
                                              → Redis
```

---

## 2. 环境要求

| 项目 | 要求 |
|------|------|
| Docker | 24.0+ |
| Docker Compose | v2.0+（Docker CLI 内置版本） |
| 服务器配置 | 最低 2C4G，推荐 4C8G |
| 磁盘空间 | 最低 20GB（含镜像 + 数据） |
| 操作系统 | Linux（推荐 Ubuntu 22.04 / Debian 12） |
| 网络 | 服务器需可访问 Docker Hub 或私有镜像仓库 |

---

## 3. 快速部署

### 3.1 克隆代码

```bash
git clone https://github.com/your-org/vincent-crm.git
cd vincent-crm
```

### 3.2 配置环境变量

```bash
cp .env.example .env
```

编辑 `.env` 文件，修改以下关键配置：

```env
# 修改为强密码
MYSQL_ROOT_PASSWORD=your_strong_password

# 保持默认或修改
MYSQL_DATABASE=vincent_crm

# Redis 密码（生产环境建议设置）
REDIS_PASSWORD=your_redis_password

# JWT 密钥（必须修改为随机字符串）
JWT_SECRET=your_random_secret_key
```

### 3.3 启动服务

```bash
docker compose -f docker-compose.prod.yml up -d --build
```

首次构建需要下载依赖和编译，预计耗时 5-15 分钟。

### 3.4 访问系统

- **访问地址：** `http://your-server-ip`
- **默认账号：** `admin`
- **默认密码：** `CordysCRM`

> ⚠️ 首次登录后请立即修改默认密码。

---

## 4. 配置说明

### 4.1 环境变量清单

| 变量名 | 默认值 | 说明 |
|--------|--------|------|
| `MYSQL_ROOT_PASSWORD` | 无（必填） | MySQL root 用户密码 |
| `MYSQL_DATABASE` | `vincent_crm` | 数据库名称 |
| `REDIS_PASSWORD` | 空 | Redis 密码，生产环境建议设置 |
| `JWT_SECRET` | 无（必填） | JWT 令牌签名密钥 |
| `JAVA_OPTS` | `-Xms256m -Xmx512m` | 后端 JVM 启动参数 |
| `WEB_PORT` | `80` | 前端对外映射端口 |

### 4.2 MySQL 配置

- 字符集：`utf8mb4`
- 排序规则：`utf8mb4_unicode_ci`
- 认证插件：`mysql_native_password`
- 数据持久化：Docker 卷 `mysql-data`

如需自定义 MySQL 配置，可在 `docker-compose.prod.yml` 的 `mysql` 服务中添加挂载：

```yaml
volumes:
  - ./my.cnf:/etc/mysql/conf.d/my.cnf
```

### 4.3 Redis 配置

- 默认无密码，生产环境建议通过 `REDIS_PASSWORD` 设置
- 持久化方式：RDB（默认）
- 数据持久化：Docker 卷 `redis-data`

### 4.4 后端 JVM 参数调优

根据服务器内存大小调整 `JAVA_OPTS` 环境变量：

| 服务器内存 | 推荐配置 |
|-----------|----------|
| 4GB | `-Xms256m -Xmx512m` |
| 8GB | `-Xms512m -Xmx1024m` |
| 16GB+ | `-Xms1024m -Xmx2048m` |

在 `.env` 文件中设置：

```env
JAVA_OPTS=-Xms512m -Xmx1024m -XX:+UseG1GC
```

---

## 5. 端口说明

| 端口 | 服务 | 对外暴露 | 说明 |
|------|------|---------|------|
| 80 | Nginx（前端） | 是 | Web UI 入口 |
| 8082 | Spring Boot（后端） | 否 | API 服务，仅容器内部访问 |
| 3306 | MySQL | 否 | 数据库，仅容器内部访问 |
| 6379 | Redis | 否 | 缓存，仅容器内部访问 |

> ⚠️ 生产环境中，8082、3306、6379 端口不应对外暴露。如需远程调试，请通过 SSH 隧道访问。

---

## 6. CI/CD 说明

### 6.1 GitHub Actions 工作流

项目配置了 `.github/workflows/deploy.yml` 工作流，包含以下阶段：

1. **build-backend** — 编译后端 Java 项目，上传 JAR 产物
2. **build-frontend** — 编译前端 Vue 项目，上传构建产物
3. **docker-build** — 构建 Docker 镜像并推送到 Registry（仅 main 分支推送触发）
4. **deploy** — SSH 部署到生产服务器（仅 main 分支推送触发）

### 6.2 Docker Registry 配置

在 GitHub 仓库的 Settings → Secrets and variables → Actions 中配置以下密钥：

| 密钥名 | 说明 |
|--------|------|
| `DOCKER_REGISTRY` | Docker Registry 地址（如 `ghcr.io`、`registry.cn-hangzhou.aliyuncs.com`） |
| `DOCKER_USERNAME` | Registry 用户名 |
| `DOCKER_PASSWORD` | Registry 密码或 Token |
| `DEPLOY_HOST` | 部署服务器 IP |
| `DEPLOY_USER` | SSH 登录用户名 |
| `DEPLOY_SSH_KEY` | SSH 私钥 |
| `DEPLOY_PATH` | 项目部署路径 |

### 6.3 自动部署触发条件

- **Pull Request** 到 main 分支：仅触发构建验证，不部署
- **Push** 到 main 分支：触发构建 → 镜像推送 → 自动部署

---

## 7. Nginx 配置说明

Nginx 配置文件位于 `frontend/nginx.conf`，主要功能：

### 7.1 前端路由处理

```nginx
location / {
    try_files $uri $uri/ /index.html;
}
```

支持 Vue Router 的 history 模式，所有未匹配的路径回退到 `index.html`。

### 7.2 API 反向代理

```nginx
location /api/ {
    proxy_pass http://backend:8082/;
}
```

将 `/api/` 前缀的请求代理到后端服务，自动剥离 `/api` 前缀。例如：
- 客户端请求 `GET /api/account/page` → 后端接收 `GET /account/page`

### 7.3 静态资源缓存策略

```nginx
location /assets/ {
    expires 30d;
    add_header Cache-Control "public, immutable";
}
```

Vite 构建的静态资源（JS/CSS/图片）放置在 `/assets/` 目录，文件名包含哈希值，可安全设置长期缓存。

### 7.4 HTTPS 配置

生产环境建议启用 HTTPS，使用 Certbot 申请 Let's Encrypt 免费证书：

```bash
# 安装 Certbot
apt-get update && apt-get install -y certbot

# 申请证书（先停止 80 端口服务）
docker compose -f docker-compose.prod.yml stop frontend
certbot certonly --standalone -d your-domain.com

# 修改 nginx.conf 添加 HTTPS 配置
```

在 `nginx.conf` 中添加 HTTPS server 块：

```nginx
server {
    listen 443 ssl http2;
    server_name your-domain.com;

    ssl_certificate /etc/letsencrypt/live/your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/your-domain.com/privkey.pem;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

    # 其余配置与 HTTP 相同
    root /usr/share/nginx/html;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://backend:8082/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /assets/ {
        expires 30d;
        add_header Cache-Control "public, immutable";
    }
}

# HTTP 重定向到 HTTPS
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}
```

在 `docker-compose.prod.yml` 中挂载证书目录：

```yaml
frontend:
  volumes:
    - /etc/letsencrypt:/etc/letsencrypt:ro
  ports:
    - "80:80"
    - "443:443"
```

---

## 8. 常见问题

### 8.1 启动失败排查

```bash
# 查看所有容器状态
docker compose -f docker-compose.prod.yml ps

# 查看后端日志
docker compose -f docker-compose.prod.yml logs backend

# 查看前端日志
docker compose -f docker-compose.prod.yml logs frontend

# 查看完整日志（最近 100 行）
docker compose -f docker-compose.prod.yml logs --tail=100
```

### 8.2 数据库连接问题

**症状：** 后端日志显示 `Communications link failure` 或连接超时。

**排查步骤：**

1. 确认 MySQL 容器健康：`docker compose -f docker-compose.prod.yml ps mysql`
2. 检查数据库密码是否一致：`.env` 中的 `MYSQL_ROOT_PASSWORD` 与后端环境变量匹配
3. 等待 MySQL 完全启动（约 30 秒），后端配置了 `depends_on` 健康检查
4. 手动测试数据库连接：

```bash
docker compose -f docker-compose.prod.yml exec mysql \
    mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" -e "SELECT 1"
```

### 8.3 前端页面空白

**可能原因：**

1. 构建产物未正确生成 — 检查前端容器日志
2. Nginx 配置错误 — 检查 `nginx.conf` 是否正确挂载
3. API 请求 404 — 确认后端服务正常运行

**排查步骤：**

```bash
# 检查前端容器内文件
docker compose -f docker-compose.prod.yml exec frontend ls /usr/share/nginx/html

# 检查 Nginx 配置
docker compose -f docker-compose.prod.yml exec frontend nginx -t

# 检查 API 连通性
docker compose -f docker-compose.prod.yml exec frontend wget -qO- http://backend:8082/
```

### 8.4 容器日志查看方法

```bash
# 实时跟踪某个服务日志
docker compose -f docker-compose.prod.yml logs -f backend

# 查看最近 N 行日志
docker compose -f docker-compose.prod.yml logs --tail=50 backend

# 查看指定时间范围日志
docker compose -f docker-compose.prod.yml logs --since=30m backend
```

---

## 9. 备份与恢复

### 9.1 数据库备份

```bash
# 备份全部数据库
docker compose -f docker-compose.prod.yml exec mysql \
    mysqldump -uroot -p"${MYSQL_ROOT_PASSWORD}" \
    --single-transaction --routines --triggers \
    vincent_crm > backup_$(date +%Y%m%d_%H%M%S).sql

# 备份并压缩
docker compose -f docker-compose.prod.yml exec mysql \
    mysqldump -uroot -p"${MYSQL_ROOT_PASSWORD}" \
    --single-transaction --routines --triggers \
    vincent_crm | gzip > backup_$(date +%Y%m%d_%H%M%S).sql.gz
```

### 9.2 数据卷备份

```bash
# 查看数据卷位置
docker volume inspect vincent-crm_mysql-data
docker volume inspect vincent-crm_redis-data

# 备份 MySQL 数据卷
docker run --rm -v vincent-crm_mysql-data:/data -v $(pwd):/backup \
    alpine tar czf /backup/mysql-volume_$(date +%Y%m%d).tar.gz -C /data .

# 备份 Redis 数据卷
docker run --rm -v vincent-crm_redis-data:/data -v $(pwd):/backup \
    alpine tar czf /backup/redis-volume_$(date +%Y%m%d).tar.gz -C /data .
```

### 9.3 恢复步骤

**恢复数据库：**

```bash
# 从 SQL 文件恢复
docker compose -f docker-compose.prod.yml exec -T mysql \
    mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" vincent_crm < backup_20260120_120000.sql
```

**恢复数据卷：**

```bash
# 停止服务
docker compose -f docker-compose.prod.yml down

# 恢复 MySQL 数据卷
docker run --rm -v vincent-crm_mysql-data:/data -v $(pwd):/backup \
    alpine sh -c "cd /data && tar xzf /backup/mysql-volume_20260120.tar.gz"

# 重新启动
docker compose -f docker-compose.prod.yml up -d
```

---

## 10. 升级流程

### 10.1 拉取最新代码

```bash
cd vincent-crm
git pull origin main
```

### 10.2 重新构建并重启

```bash
# 重新构建镜像并重启服务
docker compose -f docker-compose.prod.yml up -d --build

# 仅重新构建后端
docker compose -f docker-compose.prod.yml up -d --build backend

# 仅重新构建前端
docker compose -f docker-compose.prod.yml up -d --build frontend
```

### 10.3 升级前备份

建议在每次升级前执行数据库备份：

```bash
docker compose -f docker-compose.prod.yml exec mysql \
    mysqldump -uroot -p"${MYSQL_ROOT_PASSWORD}" \
    --single-transaction vincent_crm > pre_upgrade_$(date +%Y%m%d).sql
```

### 10.4 回滚

如升级后出现问题：

```bash
# 回退到之前的代码版本
git checkout <previous-commit-hash>

# 重新构建
docker compose -f docker-compose.prod.yml up -d --build

# 如需恢复数据库
docker compose -f docker-compose.prod.yml exec -T mysql \
    mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" vincent_crm < pre_upgrade_20260120.sql
```
