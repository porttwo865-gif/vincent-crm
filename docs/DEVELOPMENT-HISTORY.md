# VincentCRM 开发历史归档

## 项目概述

- **项目名称**：VincentCRM
- **技术栈**：Java 17 + Spring Boot 3.5.14 + MyBatis + Shiro + Redis + MySQL 8.0 + Vue 3 + TypeScript + Naive UI + Vite + pnpm monorepo
- **包名**：cn.vincent.crm.*
- **架构**：Maven 多模块（app / framework / crm） + pnpm monorepo（web / mobile / lib-shared）

## 开发规范

| 项目 | 规范 |
|------|------|
| 依赖注入 | `@Resource`（不用 `@Autowired`） |
| ID 生成 | `IDGenerator.nextStr()` |
| 时间戳 | `System.currentTimeMillis()` |
| 对象拷贝 | `BeanUtils.copyBean(source, TargetClass.class)` |
| 异常处理 | `throw new GenericException(Translator.get("xxx"))` |
| 实体基类 | 所有实体继承 `BaseModel`（id / createUser / createTime / updateUser / updateTime / orgId） |
| 数据权限 | `DataScopeService.getCondition()` |
| 密码加密 | BCrypt |
| 登录加密 | RSA 公钥加密传输 |
| Session 管理 | Shiro + Redis（DefaultWebSessionManager，Cookie Path=/） |

## 已完成任务清单（按时间顺序）

1. 搭建项目脚手架与基础设施（Docker Compose + Maven + 前端脚手架）
2. 实现认证与授权模块（Shiro + Redis Session + RSA 加密登录 + RBAC）
3. 实现系统管理模块（用户 / 角色 / 部门 CRUD + 数据权限）
4. 实现动态表单引擎（ModuleForm + ModuleField + EAV 模式）
5. 修正 Java 版本从 21 改为 17
6. 搭建前端项目脚手架（Vue 3 + Naive UI + Vant + pnpm monorepo）
7. 实现线索管理模块（CRUD + 公海池 + 转化）
8. 实现客户管理模块（CRUD + 联系人 + 公海池）
9. 实现商机管理模块（CRUD + 阶段流转 + 看板排序）
10. 实现产品管理模块（CRUD + 启用 / 禁用）
11. 实现合同、回款计划、回款记录、发票、订单模块
12. 实现跟进计划与跟进记录模块
13. 实现审批流模块（模板配置 + 发起 / 通过 / 驳回 / 撤回）
14. 实现全局搜索与工作台模块
15. 实现个人中心模块（修改信息 / 密码 + 登录日志 + 消息通知）
16. 补充权限和表单 DML 初始化数据
17. 实现前端所有 P0 业务页面
18. 启动 Docker 验证后端应用 + 修复启动问题
19. 前后端联调修复（context-path + proxy + ResponseWrapper + Mapper XML + 登录对接）
20. 生产环境 Docker 镜像构建、CI/CD 配置、Nginx 配置及部署文档
21. 修复登录流程（Shiro Session Cookie 路径问题）
22. P1 联调修复：修复前后端 API 路径不匹配（线索模块 `/lead` → `/clue`、系统管理添加 `/system` 前缀、个人中心端点对齐、联系人模块路径与方法对齐）（2026-05-20）

## Git 提交历史

```
e3f6948 chore(deploy): 添加生产环境Dockerfile、Nginx配置、docker-compose.prod、CI/CD及部署文档
44279fb fix: 前后端联调修复
e1d22b3 fix(backend): 修复后端启动问题
c621ad6 feat(frontend): 实现所有P0业务页面
c99c22a chore(dml): 补充权限和表单初始化数据
8b7e976 feat(workbench,personal): 实现全局搜索、工作台和个人中心模块
5485308 feat(follow,approval): 实现跟进计划/记录模块和审批流模块
ab438ac feat(contract,invoice,order): 实现合同、回款计划、回款记录、发票、订单模块
9a0f0bd feat(opportunity,product): 实现商机管理和产品管理模块
6e21eb8 feat: 初始化项目脚手架与核心业务模块
da6d1d4 fix(auth): 修复登录流程
```

## 已知问题与遗留项

1. 前后端 API 路径命名不完全一致（前端 `/clue` vs 后端 `/lead` 等），需后续统一
2. 前端部分页面使用的 API 字段名可能与后端不完全匹配，需逐页联调
3. 审批流的 `nodes` 字段使用 JSON 字符串存储，后续可考虑优化为关系表
4. 工作台"最近动态"只从跟进记录获取，后续可扩展为完整操作日志

## 技术决策记录

| 决策 | 说明 |
|------|------|
| P0 范围只做核心功能 | 不含 AI / BI 集成 |
| 后端先行开发 | 前端独立并行（Mock API） |
| Java 21 → 17 | 用户要求 |
| Shiro Session + Redis（非 JWT） | 适合传统 CRM 场景 |
| 动态表单使用 EAV 模式 | xxx_field + xxx_field_blob 扩展表 |
| 每个 Task 完成后立即 git commit | 保证版本追溯 |

## 默认账号

- 管理员：`admin` / `CordysCRM`
