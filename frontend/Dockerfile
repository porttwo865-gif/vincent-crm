# ============================================================
# VincentCRM 前端多阶段构建 Dockerfile
# Stage 1: Node 构建
# Stage 2: Nginx 运行时
# ============================================================

# -------------------- Stage 1: 构建阶段 --------------------
FROM node:22-alpine AS builder

# 安装 pnpm
RUN corepack enable && corepack prepare pnpm@latest --activate

WORKDIR /build

# 先复制依赖声明文件，利用 Docker 缓存加速依赖安装
COPY package.json pnpm-lock.yaml pnpm-workspace.yaml .npmrc ./
COPY packages/lib-shared/package.json packages/lib-shared/
COPY packages/web/package.json packages/web/

# 安装依赖
RUN pnpm install --frozen-lockfile

# 复制完整源码
COPY packages/lib-shared/ packages/lib-shared/
COPY packages/web/ packages/web/

# 构建 web 应用
RUN pnpm --filter web build

# -------------------- Stage 2: 运行时阶段 --------------------
FROM nginx:alpine

LABEL maintainer="VincentCRM"
LABEL description="VincentCRM 前端服务"

# 删除 Nginx 默认配置
RUN rm /etc/nginx/conf.d/default.conf

# 复制自定义 Nginx 配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 从构建阶段复制构建产物
COPY --from=builder /build/packages/web/dist /usr/share/nginx/html

# 暴露 HTTP 端口
EXPOSE 80

# 健康检查
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
    CMD wget -qO- http://localhost/ || exit 1

CMD ["nginx", "-g", "daemon off;"]
