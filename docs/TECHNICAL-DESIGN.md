# VincentCRM 技术方案设计

> **项目名称**：VincentCRM — 开源 AI 驱动的客户关系管理系统  
> **文档版本**：v1.0  
> **最后更新**：2026-05-19  
> **文档状态**：已评审  
> **作者**：架构组

---

## 1. 技术选型与架构设计

### 1.1 技术栈总览

| 层次 | 技术选型 | 版本 | 选型理由 |
|-----|---------|------|---------|
| **后端框架** | Spring Boot | 3.5.14 | 企业级 Java 生态最成熟的微服务框架，社区活跃，与 MyBatis/Shiro 等中间件集成度高 |
| **运行时** | JDK | 21 | LTS 版本，支持虚拟线程（Virtual Threads）提升并发处理能力，Record/Pattern Matching 简化代码 |
| **ORM** | MyBatis | 3.0.5 | 灵活的 SQL 编写，复杂查询性能优于 JPA/Hibernate；与 PageHelper 分页插件无缝集成 |
| **认证鉴权** | Apache Shiro | 2.1.0 | 轻量级，Session 模型天然支持 Web 端；比 Spring Security 更易定制多 Realm（Local/LDAP/OAuth2） |
| **缓存** | Redis + Redisson | 3.52.0 | Redis 作为分布式缓存/Session 存储；Redisson 提供分布式锁、限流器等高级原语 |
| **数据库** | MySQL | 8.0+ | 成熟 RDBMS，支持 JSON 字段、窗口函数；配合 binlog 实现增量备份 |
| **前端框架** | Vue.js 3 + TypeScript | — | 渐进式框架，组合式 API 提升代码复用；TypeScript 保障大型项目类型安全 |
| **桌面端 UI** | Naive UI | — | Vue 3 原生组件库，Tree-Support 完善（部门树/审批流），TypeScript 全覆盖 |
| **移动端 UI** | Vant UI | — | 轻量移动端组件库，适配 iOS Safari / Android Chrome，支持按需引入 |
| **构建工具** | Vite + pnpm | Node 22.16.0 / pnpm 10.4.1 | Vite HMR 极速；pnpm 节省磁盘空间，monorepo workspace 原生支持 |
| **API 文档** | Springdoc OpenAPI | 2.8.16 | 基于 Swagger 3/OpenAPI 3，与 Spring Boot 3 自动装配，零配置生成文档 |
| **Excel 处理** | FastExcel | 1.3.0 | 高性能 Excel 读写，SAX 模式低内存占用，适合 10 万级导入导出 |
| **定时任务** | Quartz | 1.0.0-starter | 企业级调度器，支持 cron 表达式、集群模式，满足线索池自动回收等定时需求 |
| **容器化** | Docker | — | 一键部署，标准化运行环境，配合 Docker Compose 编排 MySQL/Redis/APP |
| **BI 嵌入** | DataEase | — | 开源 BI，通过 iframe + Token 嵌入，零开发成本实现报表看板 |
| **AI 集成** | OpenClaw Agent | — | AI 销售助手，通过 REST API 对接，支持模块上下文关联 |
| **国际化** | i18n 资源包 | — | 前端 vue-i18n + 后端 ResourceBundle，中/英双语支持 |

### 1.2 系统架构描述

系统采用经典三层架构，结合 Docker 容器化部署：

```
┌─────────────────────────────────────────────────────────────────────────┐
│                          接入层 (Access Layer)                         │
│  ┌──────────┐  ┌──────────┐  ┌───────────┐  ┌──────────────────────┐  │
│  │ Web 浏览器│  │ 移动端 H5 │  │ Open API  │  │ 第三方回调(企微/钉钉) │  │
│  └─────┬────┘  └─────┬────┘  └─────┬─────┘  └──────────┬───────────┘  │
│        │              │              │                    │              │
│  ┌─────▼──────────────▼──────────────▼────────────────────▼──────────┐  │
│  │                     Nginx 反向代理                                 │  │
│  │         静态资源托管 / SSL 终止 / 负载均衡 / 限流                    │  │
│  └──────────────────────────┬───────────────────────────────────────┘  │
└─────────────────────────────┼─────────────────────────────────────────┘
                              │
┌─────────────────────────────▼─────────────────────────────────────────┐
│                       业务逻辑层 (Business Layer)                      │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │              Spring Boot 3.5.14 (Java 21)                        │  │
│  │  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌───────────┐          │  │
│  │  │ Shiro    │ │ AspectJ  │ │ Swagger  │ │ Scheduler │          │  │
│  │  │ 认证鉴权  │ │ AOP 日志  │ │ API 文档 │ │ Quartz    │          │  │
│  │  └──────────┘ └──────────┘ └──────────┘ └───────────┘          │  │
│  │                                                                  │  │
│  │  ┌─────────────────────────────────────────────────────────┐     │  │
│  │  │              CRM 业务模块 (13 modules)                   │     │  │
│  │  │  clue / customer / opportunity / contract / order /      │     │  │
│  │  │  product / follow / approval / system / search /         │     │  │
│  │  │  dashboard / home / integration                         │     │  │
│  │  └─────────────────────────────────────────────────────────┘     │  │
│  │                                                                  │  │
│  │  ┌─────────────────────────────────────────────────────────┐     │  │
│  │  │              通用服务层 (Framework)                       │     │  │
│  │  │  DataScope / PermissionCache / ModuleForm / Excel /      │     │  │
│  │  │  FileCenter / IDGenerator / I18n / Log                   │     │  │
│  │  └─────────────────────────────────────────────────────────┘     │  │
│  └──────────────────────────────────────────────────────────────────┘  │
└─────────────────────────────┬─────────────────────────────────────────┘
                              │
┌─────────────────────────────▼─────────────────────────────────────────┐
│                      数据持久层 (Data Layer)                           │
│  ┌──────────────┐  ┌──────────────┐  ┌───────────────────────────┐   │
│  │   MySQL 8.0  │  │    Redis     │  │  对象存储 (Local / S3)    │   │
│  │  主库 + Binlog│  │ Session/缓存 │  │  文件/附件/导出产物       │   │
│  │  每日全量备份  │  │  Redisson    │  │                           │   │
│  └──────────────┘  └──────────────┘  └───────────────────────────┘   │
│                                                                       │
│  ┌──────────────────────────────────────────────────────────────────┐ │
│  │                  外部集成 (External Integration)                  │ │
│  │  DataEase BI / OpenClaw AI / SQLBot / 企微 / 钉钉 / 飞书        │ │
│  └──────────────────────────────────────────────────────────────────┘ │
└───────────────────────────────────────────────────────────────────────┘
```

### 1.3 Maven 模块结构

```
VincentCRM (root)
├── backend
│   ├── app          → Spring Boot 启动模块 (Application.java, AppListener)
│   ├── framework    → 通用框架层 (BaseModel, Security, MyBatis, Excel, File)
│   └── crm          → CRM 核心业务模块
│       └── cn.vincent.crm
│           ├── clue         → 线索管理
│           ├── customer     → 客户/联系人/公海/协作人
│           ├── opportunity  → 商机管理
│           ├── contract     → 合同/回款计划/回款记录/发票
│           ├── order        → 订单管理
│           ├── product      → 产品/价格
│           ├── follow       → 跟进计划/跟进记录
│           ├── approval     → 审批流
│           ├── system       → 组织/用户/角色/权限/字典/表单/日志
│           ├── search       → 全局搜索
│           ├── dashboard    → 仪表板
│           ├── home         → 工作台
│           └── integration  → DataEase / Agent / SSO / SQLBot
└── frontend
    └── packages
        ├── web           → 桌面端 Vue3 + Naive UI
        ├── mobile        → 移动端 Vue3 + Vant UI
        └── lib-shared    → 共享 API / Models / Utils
```

**选型说明**：Maven 多模块而非微服务，原因是当前阶段 200 并发量级下单体足以应对，避免微服务带来的运维复杂度。`framework` 与 `crm` 分离确保通用能力可复用，未来可按模块拆分为独立服务。

---

## 2. 核心模块设计

### 2.1 用户鉴权模块

#### 2.1.1 认证架构

系统支持多源认证，通过 Shiro `LocalRealm` 统一调度：

```
┌─────────────────────────────────────────────────────┐
│                   认证流程 (Authentication)           │
│                                                       │
│  ┌──────────┐    ┌────────────┐    ┌──────────────┐  │
│  │ 账号密码  │───▶│ LocalRealm │───▶│ Shiro Session│  │
│  │ (RSA加密) │    │  本地认证   │    │  鉴权会话    │  │
│  └──────────┘    └────────────┘    └──────┬───────┘  │
│                                            │          │
│  ┌──────────┐    ┌────────────┐           │          │
│  │ SSO 登录  │───▶│ SSOService │───────────│          │
│  │ CAS/OIDC │    │ OAuth2 认证 │           │          │
│  └──────────┘    └────────────┘           │          │
│                                            │          │
│  ┌──────────┐    ┌────────────┐           │          │
│  │ 企微/钉钉 │───▶│ SSOService │───────────│          │
│  │ /飞书扫码 │    │ OAuth2 认证 │           │          │
│  └──────────┘    └────────────┘           │          │
│                                            │          │
│  ┌──────────┐    ┌────────────┐           │          │
│  │ API Key  │───▶│ApiKeyFilter│───────────│          │
│  │ 签名认证  │    │ AK/SK 验签  │           │          │
│  └──────────┘    └────────────┘           │          │
│                                            ▼          │
│                                ┌──────────────────┐   │
│                                │   SessionUser    │   │
│                                │ userId / orgId   │   │
│                                │ permissions      │   │
│                                └──────────────────┘   │
└─────────────────────────────────────────────────────┘
```

#### 2.1.2 认证时序（账号密码登录）

```
Client                AuthFilter           LocalRealm          UserLoginService        Redis
  │                      │                    │                     │                   │
  │  1. GET /rsa/key    │                    │                     │                   │
  │─────────────────────▶│                    │                     │                   │
  │  2. 返回 RSA 公钥    │                    │                     │                   │
  │◀─────────────────────│                    │                     │                   │
  │                      │                    │                     │                   │
  │  3. POST /login     │                    │                     │                   │
  │  (RSA加密密码)       │                    │                     │                   │
  │─────────────────────▶│                    │                     │                   │
  │                      │  4. subject.login()│                     │                   │
  │                      │───────────────────▶│                     │                   │
  │                      │                    │ 5. authenticateUser │                   │
  │                      │                    │────────────────────▶│                   │
  │                      │                    │                     │ 6. 查询用户       │
  │                      │                    │                     │──────────────────▶│
  │                      │                    │                     │ 7. bcrypt 校验密码 │
  │                      │                    │ 8. 返回SessionUser  │                   │
  │                      │                    │◀────────────────────│                   │
  │                      │ 9. 写入 Session    │                     │                   │
  │                      │─────────────────────────────────────────────────────────▶│
  │  10. 返回 SessionID + CSRF Token         │                     │                   │
  │◀─────────────────────│                    │                     │                   │
```

#### 2.1.3 授权架构（RBAC + 数据权限）

```java
// 权限校验链路
@RequiresPermissions(PermissionConstants.CLUE_MANAGEMENT_READ)
    → PermissionUtils.hasPermission(permission)
        → PermissionCache.getPermissionIds(userId, orgId)    // Redis 缓存
            → RoleService.getRoleOptions(userId, orgId)      // 用户角色
                → RolePermission (角色-权限映射)

// 数据权限校验
DataScopeService.getDeptDataPermission(userId, orgId, viewId, permission)
    → RoleDataScope 枚举:
        ALL            → 查看全部数据
        DEPT_AND_CHILD → 本部门及下级部门数据
        DEPT_CUSTOM    → 指定部门数据
        SELF           → 仅本人数据
    → DeptDataPermissionDTO { all: boolean, deptIds: List<String> }
```

**核心类关系**：

| 类名 | 职责 |
|-----|------|
| `ShiroConfig` | Shiro 过滤器链配置：AuthFilter → CsrfFilter → ApiKeyFilter |
| `LocalRealm` | 多源认证 Realm，根据 Session 中 `authenticate` 字段路由 LOCAL/OAUTH2/CAS 等 |
| `AuthFilter` | 未认证请求拦截，设置 `AUTHENTICATION_STATUS` 响应头 |
| `CsrfFilter` | CSRF Token 双重校验（csrfToken + xAuthToken），Referer 域名校验 |
| `ApiKeyFilter` | API Key 认证，`Authorization: accessKey:AES(signature, secretKey)` |
| `PermissionCache` | 权限 Redis 缓存，`@Cacheable("permission_cache")`，角色变更时清除 |
| `DataScopeService` | 数据权限解析，部门树裁剪，支持 ALL/DEPT_AND_CHILD/DEPT_CUSTOM/SELF |
| `PermissionUtils` | 运行时权限判断工具，admin 用户默认拥有所有权限 |

### 2.2 Lead-to-Cash 核心流转

#### 2.2.1 L2C 数据流转时序

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                        Lead-to-Cash 核心流转                                  │
│                                                                              │
│  Clue ──转客户──▶ Customer ──创建商机──▶ Opportunity ──生成报价──▶ Quotation  │
│    │                │                      │                                │
│    │                │                      │──签订合同──▶ Contract           │
│    │                │                      │                │                │
│    │                │                      │            回款计划│回款记录       │
│    │                │                      │                │                │
│    │                │                      │            发票 Invoice         │
│    │                │                      │                │                │
│    │                │                      │            订单 Order            │
│    │                │                      │                                  │
│  线索池          公海池                  商机看板                             │
│  CluePool       CustomerPool          看板拖拽                             │
└──────────────────────────────────────────────────────────────────────────────┘
```

#### 2.2.2 线索转客户核心逻辑

```java
// ClueService.transformToCustomer() 伪代码
public Customer transformToCustomer(ClueTransformRequest request, String userId, String orgId) {
    // 1. 校验线索存在且未转化
    Clue clue = clueMapper.selectByPrimaryKey(request.getClueId());
    if (clue.getTransitionType() != null) {
        throw new GenericException("该线索已转化");  // 乐观锁防重复
    }
    
    // 2. 客户容量校验
    customerCapacityService.checkCapacity(userId, orgId);
    
    // 3. 创建/关联客户
    Customer customer;
    if (request.isCreateNew()) {
        customer = new Customer();
        BeanUtils.copyBean(customer, request);  // 预填线索信息
        customer.setOwner(userId);              // 原线索负责人 → 新客户负责人
        customer.setOrganizationId(orgId);
        customerMapper.insert(customer);
    } else {
        customer = customerMapper.selectByPrimaryKey(request.getCustomerId());
        // 校验关联客户权限
    }
    
    // 4. 更新线索状态
    clue.setTransitionType("CUSTOMER");
    clue.setTransitionId(customer.getId());
    clueMapper.update(clue);
    
    // 5. 记录操作日志
    logService.log(LogModule.CLUE_INDEX, LogType.TRANSFORM, clue.getId());
    
    // 6. 记录负责人变更历史
    clueOwnerService.record(clue.getId(), userId);
    
    return customer;
}
```

### 2.3 审批流模块

#### 2.3.1 审批流数据模型

```
┌──────────────┐     1:N     ┌───────────────────┐
│ ApprovalFlow │────────────▶│ ApprovalFlowVersion│
│ (审批流定义)  │             │ (版本快照)          │
└──────────────┘             └─────────┬─────────┘
                                       │ 1:N
                              ┌────────▼────────┐
                              │  ApprovalNode    │
                              │  (节点定义)       │
                              │  START/APPROVER  │
                              │  CONDITION/END   │
                              └────────┬────────┘
                                       │
                    ┌──────────────────┼──────────────────┐
                    │                  │                   │
           ┌────────▼──────┐  ┌───────▼──────┐  ┌───────▼──────────┐
           │ApprovalNode   │  │ApprovalNode  │  │ApprovalNode      │
           │Approver       │  │Condition     │  │Link              │
           │(审批人配置)    │  │(条件分支)     │  │(节点连接关系)     │
           └───────────────┘  └──────────────┘  └──────────────────┘
```

#### 2.3.2 审批执行时序

```
提交人                  ApprovalFlowService                  审批人
  │                            │                                │
  │ 1. 提交审批                │                                │
  │───────────────────────────▶│                                │
  │                            │ 2. 查找生效审批流版本            │
  │                            │───────────(DB)                 │
  │                            │ 3. 解析 START → APPROVER 节点   │
  │                            │ 4. resolveApprovers()           │
  │                            │    (MEMBER/SUPERIOR/DEPT_HEAD/ROLE)│
  │                            │ 5. 创建 ApprovalInstance        │
  │                            │ 6. 创建 ApprovalTask(s)         │
  │                            │    multiApproverMode:           │
  │                            │    ALL/ANY/SEQUENTIAL           │
  │                            │                                │
  │                            │ 7. 发送待办通知                  │
  │                            │───────────────────────────────▶│
  │                            │                                │
  │                            │ 8. 审批人操作 (通过/驳回/转审)  │
  │                            │◀───────────────────────────────│
  │                            │ 9. 更新 ApprovalTask.status     │
  │                            │ 10. 创建 ApprovalRecord         │
  │                            │ 11. 判断当前节点是否全部通过      │
  │                            │     YES → 流转至下一节点         │
  │                            │     NO  → 等待其他人审批         │
  │                            │ 12. 最后节点 → Instance 完成     │
  │ 13. 通知审批结果            │                                │
  │◀───────────────────────────│                                │
```

#### 2.3.3 审批与业务模块的集成

通过 AOP 注解 `@HitApproval` 实现审批流与业务逻辑的解耦：

```java
@HitApproval(formKey = FormKey.CONTRACT, executeType = ExecuteTimingEnum.CREATE)
public Contract add(ContractAddRequest request, String operatorId, String orgId) {
    // 业务逻辑...
}
```

AOP 切面在方法执行后自动触发审批流，根据 `formKey` 和 `executeType` 查找对应审批流配置。

### 2.4 动态表单模块

#### 2.4.1 表单引擎架构

```
┌──────────────────────────────────────────────────────┐
│                  动态表单引擎                           │
│                                                        │
│  ModuleForm ──▶ ModuleField ──▶ ModuleFieldBlob       │
│  (表单定义)     (字段定义)      (字段属性 JSON)         │
│                                                        │
│  FormKey 枚举:                                         │
│  CLUE / CUSTOMER / CONTACT / OPPORTUNITY / QUOTATION  │
│  PRODUCT / PRICE / CONTRACT / PAYMENT_PLAN / INVOICE  │
│  ORDER / FOLLOW_RECORD / FOLLOW_PLAN                  │
│                                                        │
│  字段类型 (FieldType):                                 │
│  TEXT / NUMBER / DATE_TIME / SELECT / MULTI_SELECT    │
│  DATASOURCE / DATASOURCE_MULTIPLE / SUB_FORM / ...   │
│                                                        │
│  数据存储:                                             │
│  业务表 (clue/customer/...) → 固定字段                  │
│  xxx_field        → 自定义字段值 (短文本)               │
│  xxx_field_blob   → 自定义字段值 (长文本/JSON)          │
└──────────────────────────────────────────────────────┘
```

**设计决策**：采用 EAV（Entity-Attribute-Value）混合模式 —— 固定业务字段直接存在主表，自定义字段存在独立的 field/field_blob 表。原因：纯 JSON 列查询性能差，纯 EAV 关联查询复杂，混合模式兼顾查询效率与扩展性。

---

## 3. 数据模型设计

### 3.1 核心 ER 图描述

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                                                                                   │
│  ┌─────────┐     ┌──────────┐     ┌────────────┐     ┌──────────────┐           │
│  │  Clue   │────▶│ Customer │────▶│ Opportunity│────▶│  Quotation   │           │
│  │  线索   │1:1  │  客户    │1:N  │   商机     │1:N  │   报价单     │           │
│  └────┬────┘     └────┬─────┘     └─────┬──────┘     └──────────────┘           │
│       │               │                  │                                       │
│  ┌────▼────┐     ┌────▼─────┐     ┌──────▼──────┐                               │
│  │CluePool │     │Customer  │     │  Contract   │◀──────────────────┐           │
│  │ 线索池  │     │Contact   │     │   合同      │                   │           │
│  │ClueOwner│     │CustomerPool│   │ContractStage│                   │           │
│  └─────────┘     │CustomerColl│   └──────┬──────┘                   │           │
│                  │CustomerOwner│          │                          │           │
│                  │CustomerCap │   ┌───────┼────────┐                │           │
│                  └───────────┘   │       │        │                │           │
│                          ▲       │       │        │                │           │
│                          │  ┌────▼───┐ ┌─▼────┐ ┌─▼──────┐  ┌─────▼────┐     │
│                          │  │Payment │ │Payment│ │Invoice │  │  Order   │     │
│                          │  │Plan    │ │Record │ │ 发票   │  │  订单    │     │
│                          │  │回款计划 │ │回款记录│ └────────┘  │OrderStage│     │
│                          │  └────────┘ └───────┘             └──────────┘     │
│                          │                                                      │
│                  ┌───────┴───────┐                                              │
│                  │   Product     │                                              │
│                  │   产品主数据   │                                              │
│                  │   ProductPrice│                                              │
│                  └──────────────┘                                              │
│                                                                                   │
│  ┌──────────────────────────────────────────────────────────────────┐           │
│  │                    系统管理域                                     │           │
│  │  sys_user ──▶ sys_organization_user ──▶ sys_role ──▶ sys_permission│          │
│  │                        │                         │               │           │
│  │                  sys_department              role_scope_dept     │           │
│  └──────────────────────────────────────────────────────────────────┘           │
│                                                                                   │
│  ┌──────────────────────────────────────────────────────────────────┐           │
│  │                    审批流域                                       │           │
│  │  approval_flow ──▶ approval_flow_version ──▶ approval_node      │           │
│  │       │                                      │                  │           │
│  │  approval_instance ──▶ approval_task     approval_node_approver │           │
│  │       │              approval_record     approval_node_condition │           │
│  │  approval_instance_attachment            approval_node_link      │           │
│  └──────────────────────────────────────────────────────────────────┘           │
└─────────────────────────────────────────────────────────────────────────────────┘
```

### 3.2 核心数据表定义

#### 3.2.1 基础模型 (BaseModel)

所有业务实体继承 `BaseModel`，统一审计字段：

| 字段 | 类型 | 说明 |
|-----|------|------|
| `id` | VARCHAR(32) | 主键，IDGenerator 生成 |
| `create_user` | VARCHAR(32) | 创建人 |
| `update_user` | VARCHAR(32) | 修改人 |
| `create_time` | BIGINT | 创建时间（毫秒时间戳） |
| `update_time` | BIGINT | 更新时间（毫秒时间戳） |

#### 3.2.2 线索表 (clue)

| 字段 | 类型 | 说明 |
|-----|------|------|
| `id` | VARCHAR(32) PK | 主键 |
| `name` | VARCHAR(255) | 客户名称 |
| `owner` | VARCHAR(32) | 负责人 ID |
| `stage` | VARCHAR(32) | 阶段 ID |
| `last_stage` | VARCHAR(32) | 上次阶段 |
| `contact` | VARCHAR(255) | 联系人名称 |
| `phone` | VARCHAR(30) | 联系人电话 |
| `products` | JSON | 意向产品 ID 列表 |
| `organization_id` | VARCHAR(32) | 组织 ID（多租户隔离） |
| `collection_time` | BIGINT | 领取时间 |
| `in_shared_pool` | TINYINT(1) | 是否在线索池 |
| `transition_type` | VARCHAR(20) | 转化类型 (CUSTOMER) |
| `transition_id` | VARCHAR(32) | 转化目标 ID |
| `follower` | VARCHAR(32) | 最新跟进人 |
| `follow_time` | BIGINT | 最新跟进时间 |
| `pool_id` | VARCHAR(32) | 线索池 ID |
| `reason_id` | VARCHAR(32) | 移入线索池原因 ID |
| + BaseModel 字段 | | |

#### 3.2.3 客户表 (customer)

| 字段 | 类型 | 说明 |
|-----|------|------|
| `id` | VARCHAR(32) PK | 主键 |
| `name` | VARCHAR(255) | 客户名称 |
| `owner` | VARCHAR(32) | 负责人 ID |
| `collection_time` | BIGINT | 领取时间 |
| `pool_id` | VARCHAR(32) | 公海池 ID |
| `in_shared_pool` | TINYINT(1) | 是否在公海池 |
| `organization_id` | VARCHAR(32) | 组织 ID |
| `follower` | VARCHAR(32) | 最新跟进人 |
| `follow_time` | BIGINT | 最新跟进时间 |
| `reason_id` | VARCHAR(32) | 移入公海原因 ID |
| + BaseModel 字段 | | |

#### 3.2.4 合同表 (contract)

| 字段 | 类型 | 说明 |
|-----|------|------|
| `id` | VARCHAR(32) PK | 主键 |
| `name` | VARCHAR(255) | 合同名称 |
| `customer_id` | VARCHAR(32) | 客户 ID |
| `owner` | VARCHAR(32) | 合同负责人 |
| `amount` | DECIMAL(18,2) | 合同金额 |
| `number` | VARCHAR(64) | 合同编号 |
| `approval_status` | VARCHAR(20) | 审核状态 (NONE/PENDING/APPROVED/REJECTED) |
| `stage` | VARCHAR(32) | 合同阶段 |
| `start_time` | BIGINT | 合同开始时间 |
| `end_time` | BIGINT | 合同结束时间 |
| `void_reason` | TEXT | 作废原因 |
| `organization_id` | VARCHAR(32) | 组织 ID |
| `pos` | BIGINT | 自定义排序 |
| + BaseModel 字段 | | |

#### 3.2.5 审批流相关表

| 表名 | 核心字段 | 说明 |
|-----|---------|------|
| `approval_flow` | current_version_id, number, name, form_type, enable, status_permissions | 审批流定义，form_type 支持 quotation/contract/invoice/order |
| `approval_flow_version` | flow_id, organization_id | 版本快照，每次编辑生成新版本 |
| `approval_node` | flow_version_id, name, node_type(START/APPROVER/CONDITION/DEFAULT/END), sort | 流程节点 |
| `approval_node_approver` | flow_version_id, approval_type(MANUAL/AUTO_PASS), multi_approver_mode(ALL/ANY/SEQUENTIAL), approver_type(MEMBER/SUPERIOR/DEPT_HEAD/ROLE) | 审批人配置 |
| `approval_node_condition` | flow_version_id, condition_config(JSON) | 条件分支配置 |
| `approval_node_link` | flow_version_id, from_node_id, to_node_id, sort | 节点连接关系 |
| `approval_instance` | flow_version_id, type, resource_id, submitter_id, current_node_id, approval_status | 审批实例 |
| `approval_task` | node_id, instance_id, approver_id, status, type, action | 审批任务 |
| `approval_record` | instance_id, task_id, node_round, node_id, result, comment | 审批记录 |
| `approval_add_sign_task` | task_id, sign_task_id, type, root_task_id | 加签任务 |
| `approval_return_back_record` | task_id, return_to_node_id, return_reason | 退回记录 |

#### 3.2.6 自定义字段存储表（以线索为例）

| 表名 | 核心字段 | 说明 |
|-----|---------|------|
| `clue_field` | 继承 BaseResourceField: resource_id, field_id, type, name, internal_key | 线索自定义字段值（短文本） |
| `clue_field_blob` | 继承 BaseResourceField: resource_id, field_id, type, name, internal_key | 线索自定义字段值（长文本/JSON） |

---

## 4. 接口设计（API）

### 4.1 接口规范

- 基础路径：`/api/crm/v1`
- 认证方式：Shiro Session（Web 端） / API Key 签名（Open API）
- 响应格式：统一 JSON 包装

```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

### 4.2 认证接口

#### 4.2.1 获取 RSA 公钥

```
GET /rsa/key
```

**响应示例**：

```json
{
  "code": 200,
  "data": {
    "publicKey": "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA...",
    "nonce": "a1b2c3d4e5f6"
  }
}
```

#### 4.2.2 登录

```
POST /login
```

**请求体**：

```json
{
  "username": "admin",
  "password": "RSA_ENCRYPTED_PASSWORD"
}
```

**响应示例**：

```json
{
  "code": 200,
  "data": {
    "userId": "u001",
    "name": "管理员",
    "sessionId": "abc123",
    "organizations": [
      { "id": "org001", "name": "默认组织" }
    ]
  }
}
```

#### 4.2.3 登录状态检测

```
GET /is-login
```

**响应示例**：

```json
{
  "code": 200,
  "data": true
}
```

### 4.3 线索管理接口

#### 4.3.1 线索列表（分页/筛选/视图）

```
POST /lead/page
```

**请求体**：

```json
{
  "current": 1,
  "pageSize": 20,
  "viewId": "view001",
  "conditions": [
    {
      "fieldId": "field001",
      "operator": "CONTAINS",
      "value": "科技"
    }
  ]
}
```

**响应示例**：

```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": "clue001",
        "name": "深圳科技有限公司",
        "owner": "u002",
        "ownerName": "张三",
        "stage": "stage001",
        "contact": "李四",
        "phone": "138****1234",
        "products": ["prod001", "prod002"],
        "inSharedPool": false,
        "createTime": 1716100000000,
        "departmentId": "dept001",
        "departmentName": "销售一部",
        "follower": "u003",
        "followerName": "王五",
        "followTime": 1716110000000,
        "moduleFields": [
          { "fieldId": "cf001", "name": "来源渠道", "value": "官网" }
        ]
      }
    ],
    "total": 156,
    "current": 1,
    "pageSize": 20
  }
}
```

#### 4.3.2 新增线索

```
POST /lead/add
```

**请求体**：

```json
{
  "name": "深圳科技有限公司",
  "owner": "u002",
  "contact": "李四",
  "phone": "13800001234",
  "products": ["prod001"],
  "moduleFields": [
    { "fieldId": "cf001", "name": "来源渠道", "value": "官网" },
    { "fieldId": "cf002", "name": "备注", "value": "意向较强" }
  ]
}
```

**响应示例**：

```json
{
  "code": 200,
  "data": {
    "id": "clue001",
    "name": "深圳科技有限公司",
    "owner": "u002"
  }
}
```

#### 4.3.3 线索转客户

```
POST /lead/transform
```

**请求体**：

```json
{
  "clueId": "clue001",
  "mode": "NEW",
  "customerData": {
    "name": "深圳科技有限公司",
    "owner": "u002"
  }
}
```

或关联已有客户：

```json
{
  "clueId": "clue001",
  "mode": "LINK",
  "customerId": "cust001"
}
```

**响应示例**：

```json
{
  "code": 200,
  "data": {
    "customerId": "cust002",
    "clueStatus": "TRANSFORMED"
  }
}
```

### 4.4 客户管理接口

#### 4.4.1 客户列表

```
POST /account/page
```

**请求体**：

```json
{
  "current": 1,
  "pageSize": 20,
  "viewId": "view002",
  "conditions": []
}
```

**响应示例**：

```json
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": "cust001",
        "name": "深圳科技有限公司",
        "owner": "u002",
        "ownerName": "张三",
        "inSharedPool": false,
        "collectionTime": 1716100000000,
        "follower": "u003",
        "followerName": "王五",
        "followTime": 1716110000000,
        "moduleFields": []
      }
    ],
    "total": 89,
    "current": 1,
    "pageSize": 20
  }
}
```

#### 4.4.2 客户移入公海

```
POST /account/batch/move-pool
```

**请求体**：

```json
{
  "ids": ["cust001", "cust002"],
  "reasonId": "reason001",
  "reason": "30天未跟进"
}
```

#### 4.4.3 客户合同统计

```
GET /account/{id}/contract-statistic
```

**响应示例**：

```json
{
  "code": 200,
  "data": {
    "totalAmount": 500000.00,
    "paidAmount": 300000.00,
    "unpaidAmount": 200000.00,
    "invoicedAmount": 250000.00,
    "uninvoicedAmount": 250000.00
  }
}
```

### 4.5 合同管理接口

#### 4.5.1 创建合同

```
POST /contract/add
```

**请求体**：

```json
{
  "name": "2026年度采购合同",
  "customerId": "cust001",
  "opportunityId": "opp001",
  "owner": "u002",
  "amount": 500000.00,
  "startTime": 1716100000000,
  "endTime": 1747636000000,
  "moduleFields": [
    { "fieldId": "cf010", "name": "合同类型", "value": "年度框架" }
  ]
}
```

### 4.6 审批流接口

#### 4.6.1 审批待办列表

```
GET /approval/todo
```

**响应示例**：

```json
{
  "code": 200,
  "data": [
    {
      "resourceId": "contract001",
      "resourceName": "2026年度采购合同",
      "resourceType": "contract",
      "applicant": "张三",
      "submitTime": 1716100000000,
      "approvalTaskId": "task001",
      "approvalNodeId": "node003",
      "approvalInstanceId": "inst001"
    }
  ]
}
```

#### 4.6.2 审批操作

```
POST /approval/action
```

**请求体**：

```json
{
  "taskId": "task001",
  "instanceId": "inst001",
  "action": "APPROVE",
  "comment": "同意，金额合理"
}
```

### 4.7 全局搜索接口

```
GET /search?keyword=深圳
```

**响应示例**：

```json
{
  "code": 200,
  "data": {
    "counts": {
      "customer": 5,
      "clue": 3,
      "opportunity": 2,
      "contact": 8,
      "pool": 1,
      "cluePool": 0
    }
  }
}
```

### 4.8 系统管理接口

#### 4.8.1 角色创建

```
POST /role/add
```

**请求体**：

```json
{
  "name": "销售经理",
  "dataScope": "DEPT_AND_CHILD",
  "deptIds": [],
  "permissions": [
    { "id": "CLUE_MANAGEMENT_READ", "enable": true },
    { "id": "CLUE_MANAGEMENT_ADD", "enable": true },
    { "id": "CONTRACT_READ", "enable": true },
    { "id": "SYSTEM_ROLE_READ", "enable": false }
  ]
}
```

---

## 5. 风险评估与应对

### 风险 1：列表查询性能瓶颈

**风险描述**：数据量增长后，带数据权限的列表查询（线索/客户/商机/合同）响应时间可能超过 1.5s 阈值。数据权限需要在 SQL 层面 JOIN 部门树，复杂条件筛选 + 自定义字段 EAV 查询叠加后性能急剧下降。

**影响级别**：高

**应对方案**：

| 策略 | 实施方式 | 预期效果 |
|-----|---------|---------|
| **索引优化** | `organization_id + owner` 联合索引；`organization_id + in_shared_pool` 联合索引；自定义字段表 `resource_id + field_id` 联合索引 | 消除全表扫描 |
| **数据权限预计算** | `DataScopeService.getDeptDataPermission()` 结果缓存在 Redis（TTL 5min），避免每次请求递归遍历部门树 | 减少 DB 查询 |
| **分页优化** | PageHelper 深分页优化：`WHERE id > lastId LIMIT N` 替代 `OFFSET M LIMIT N` | 深分页 P99 从 3s → 200ms |
| **读写分离准备** | 架构预留主从复制能力，报表/搜索类查询走从库 | 列表查询不受统计查询影响 |
| **降级策略** | 超过 10 万条数据时，导出改用异步任务；列表强制分页不提供全量查询 | 保障核心链路可用 |

### 风险 2：审批流并发导致的数据一致性

**风险描述**：多人同时审批同一节点时，可能出现：
- 重复审批（同一任务被两人同时通过）
- 审批状态不一致（部分通过 + 部分驳回）
- 加签与审批并发导致流程状态混乱

**影响级别**：中

**应对方案**：

| 策略 | 实施方式 | 预期效果 |
|-----|---------|---------|
| **乐观锁** | `ApprovalTask` 更新时 `WHERE id = ? AND status = 'PENDING'`，影响行数为 0 则拒绝操作 | 防重复审批 |
| **Redisson 分布式锁** | 审批实例级别加锁 `approval_instance:{instanceId}`，同一实例的审批操作串行化 | 防并发状态混乱 |
| **幂等设计** | 审批操作前校验 `task.status == PENDING`，已处理的任务直接返回成功 | 防重复提交 |
| **加签隔离** | 加签操作创建新任务，不影响原审批链路；加签完成后再回归主流程 | 加签与审批互不干扰 |
| **降级策略** | 极端情况下，管理员可手动终止审批实例，将资源回退到「起草」状态 | 保障业务连续性 |

### 风险 3：第三方集成依赖故障

**风险描述**：系统深度集成 DataEase BI、OpenClaw AI、企微/钉钉/飞书等第三方服务。任一服务不可用时可能导致：
- BI 报表无法加载（DataEase Token 失效或服务宕机）
- AI 助手无响应（OpenClaw 超时）
- SSO 登录失败（企微/钉钉 OAuth2 接口不可达）
- 组织同步中断（三方 API 限流）

**影响级别**：中

**应对方案**：

| 策略 | 实施方式 | 预期效果 |
|-----|---------|---------|
| **熔断降级** | DataEase/Agent 调用通过 Resilience4j 熔断器包装，超时 5s 触发降级，显示「报表加载失败，请稍后重试」 | 不阻塞主流程 |
| **本地认证兜底** | SSO 不可用时，前端自动切换到本地账号密码登录入口 | 用户始终可登录 |
| **Token 缓存** | DataEase Token 提前 5min 刷新并缓存到 Redis，避免每次请求实时获取 | 减少 Token 接口依赖 |
| **异步同步** | 企微/钉钉/飞书组织同步改为消息队列异步处理，失败自动重试 3 次 | 同步不阻塞用户操作 |
| **健康检查** | 定时 Ping 第三方服务端点，不可用时在管理后台告警，管理员可手动暂停集成 | 快速感知故障 |

---

## 6. 部署架构

### 6.1 Docker 容器编排

```
┌────────────────────────────────────────────────────────────┐
│                    Docker Compose                           │
│                                                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌───────────┐ │
│  │  Nginx   │  │ VincentCRM│  │  MySQL   │  │   Redis   │ │
│  │  :80/443 │─▶│  :8082   │  │  :3306   │  │   :6379   │ │
│  │ 静态资源  │  │ Spring   │  │ 数据持久化 │  │ 缓存/会话  │ │
│  │ SSL终止   │  │  Boot    │  │          │  │  Redisson  │ │
│  └──────────┘  └──────────┘  └──────────┘  └───────────┘ │
│                                                             │
│  数据卷: ~/cordys:/opt/cordys                               │
│  端口映射: 8081(Web UI) / 8082(API)                         │
└────────────────────────────────────────────────────────────┘
```

### 6.2 备份策略

| 策略 | 实施方式 | RPO |
|-----|---------|-----|
| MySQL 全量备份 | 每日 crontab `mysqldump` | ≤ 24h |
| MySQL 增量备份 | binlog 实时归档 | ≈ 0 |
| Redis RDB | 默认配置 `save 900 1` | ≤ 15min |
| 文件存储 | Local/S3 自动同步 | ≈ 0 (S3) |

---

## 7. 技术决策记录

| 决策 | 选择 | 备选 | 理由 |
|-----|------|------|------|
| 单体 vs 微服务 | 单体模块化 | Spring Cloud 微服务 | 当前 200 并发量级无需微服务复杂度，Maven 多模块已实现代码隔离 |
| Shiro vs Spring Security | Shiro | Spring Security | Shiro 更轻量，Session 模型简单，多 Realm 定制容易；Security 过重 |
| MyBatis vs JPA | MyBatis | Spring Data JPA | CRM 列表查询条件动态组合（条件筛选+数据权限+自定义字段），MyBatis SQL 更可控 |
| EAV vs JSON 列 vs 独立列 | EAV 混合模式 | 纯 JSON / 纯列 | 固定字段用列（查询性能好），自定义字段用 EAV（灵活扩展），折中方案 |
| Session vs JWT | Shiro Session | JWT 无状态 | CRM 系统用户量可控，Session 更易实现强制下线、权限实时生效 |
| pnpm vs npm/yarn | pnpm | npm / yarn | monorepo workspace 原生支持，磁盘占用少，安装速度快 |
| 雪花 ID vs UUID | IDGenerator (自定义) | 雪花算法 / UUID | 短 ID (32字符) 适合 URL/前端使用，兼顾唯一性与可读性 |

---

## 8. 术语表

| 术语 | 定义 |
|-----|------|
| L2C | Lead-to-Cash，线索到回款的完整销售流程 |
| RBAC | Role-Based Access Control，基于角色的访问控制 |
| EAV | Entity-Attribute-Value，实体-属性-值模式，用于自定义字段存储 |
| 公海 | 无负责人的客户池，可供领取或分配 |
| 线索池 | 无负责人的线索池，可供领取或分配 |
| 数据权限 | 基于部门层级的数据可见范围控制（ALL/DEPT_CUSTOM/DEPT_AND_CHILD/SELF） |
| 动态表单 | 基于 ModuleForm + ModuleField 的可配置表单引擎 |
| 审批流 | 可配置的多级审批规则引擎，支持条件分支/会签/或签/加签 |
| 快照 | 合同/订单在某一时刻的数据快照，用于历史版本对比 |
| Agent | OpenClaw AI 智能销售助手 |
| DataEase | 开源 BI 工具，嵌入系统提供报表能力 |
| SQLBot | 基于 RAG 的自然语言数据查询工具 |
