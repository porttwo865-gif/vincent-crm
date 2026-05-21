# vincent-crm

VincentCRM 是一套基于 Spring Boot 3 + Vue 3 的全栈 CRM 系统，覆盖线索、客户、商机、合同、产品、跟进、审批等核心业务流程。

## 快速开始（本地开发）

### 环境要求

- JDK 17
- Maven 3.8+
- Node.js 22.16.0
- pnpm 10.4.1
- Docker & Docker Compose

### 启动步骤

```bash
# 1. 启动基础设施（MySQL + Redis）
docker compose up -d

# 2. 启动后端（Spring Boot，默认端口 8080）
cd backend && mvn spring-boot:run -pl app

# 3. 启动前端 Web（Vite 开发服务器）
cd frontend/packages/web && pnpm dev
```

启动完成后，浏览器访问前端开发服务器地址即可登录系统，默认账号见 `docs/DEVELOPMENT-HISTORY.md`。

## 文档

- 产品需求：`docs/PRD.md`
- 技术设计：`docs/TECHNICAL-DESIGN.md`
- 部署说明：`docs/DEPLOYMENT.md`
- 开发历史：`docs/DEVELOPMENT-HISTORY.md`
- 路线图：`docs/ROADMAP.md`
