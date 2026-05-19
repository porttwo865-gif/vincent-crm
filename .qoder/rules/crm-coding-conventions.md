---
trigger: always_on
---

# VincentCRM AI 编码规约

本文档定义了 AI 助手在为 VincentCRM 项目开发代码时必须遵循的编码标准和约定。所有规则均源自现有代码库模式和通用最佳实践。

---

## 1. 项目架构概览

### 1.1 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 3.5.14 |
| 编程语言 | Java | 17+ |
| ORM | MyBatis（自定义 BaseMapper） | 3.0.5 |
| 数据库 | MySQL | - |
| 缓存/会话 | Redis + Redisson | 3.52.0 |
| 安全框架 | Apache Shiro (Jakarta) | 2.1.0 |
| 数据库迁移 | Flyway | - |
| 前端框架 | Vue 3 + Composition API | - |
| UI 组件库(Web) | Naive UI | - |
| UI 组件库(Mobile) | Vant | - |
| 前端构建 | Vite + pnpm (monorepo) | Node 22.16.0 / pnpm 10.4.1 |
| API 文档 | SpringDoc OpenAPI | 2.8.16 |
| Excel 处理 | FastExcel | 1.3.0 |

### 1.2 模块结构

```
VincentCRM/
├── backend/
│   ├── framework/       # 核心框架（BaseMapper、通用工具、安全、切面）
│   ├── crm/             # 业务模块（线索、客户、合同、商机等）
│   └── app/             # 应用入口、配置、Flyway 迁移
├── frontend/
│   └── packages/
│       ├── web/         # 桌面端 Web 应用（Naive UI）
│       ├── mobile/      # 移动端 Web 应用（Vant）
│       └── lib-shared/  # 共享库（API、枚举、模型、Hooks、国际化）
└── installer/           # Docker 部署脚本
```

---

## 2. 后端编码规约（Java / Spring Boot）

### 2.1 包结构

每个业务模块在 `cn.vincent.crm.<module>` 下遵循以下包布局：

```
cn.vincent.crm.<module>/
├── constants/      # 模块常量（PermissionConstants 等）
├── controller/     # REST 控制器
├── domain/         # 实体类（数据库表映射）
├── dto/
│   ├── request/    # 输入 DTO（AddRequest、UpdateRequest、PageRequest）
│   └── response/   # 输出 DTO（ListResponse、GetResponse）
├── mapper/         # MyBatis Mapper 接口（Ext*Mapper 用于自定义 SQL）
├── service/        # 业务逻辑服务
└── utils/          # 模块工具类
```

**规则**：创建新业务模块时，必须严格遵循此目录结构，不得创建其他包布局。

### 2.2 命名规范

| 元素 | 规范 | 示例 |
|------|------|------|
| 类名 | 大驼峰（UpperCamelCase） | `CustomerService`、`ContractController` |
| 方法名 | 小驼峰（lowerCamelCase） | `getWithDataPermissionCheck()` |
| 常量 | 全大写下划线（UPPER_SNAKE_CASE） | `CUSTOMER_MANAGEMENT_READ` |
| 变量/字段 | 小驼峰（lowerCamelCase） | `organizationId`、`customerListResponse` |
| 包名 | 全小写，无下划线 | `cn.vincent.crm.customer` |
| 实体类 | 与领域概念同名 | `Customer`、`Contract` |
| 请求 DTO | `<实体><动作>Request` | `CustomerAddRequest`、`ContractUpdateRequest` |
| 响应 DTO | `<实体><动作>Response` | `CustomerListResponse`、`ContractGetResponse` |
| 分页请求 | `<实体>PageRequest` | `CustomerPageRequest` |
| Mapper 接口 | `Ext<实体>Mapper`（自定义）、`BaseMapper<实体>`（通用） | `ExtCustomerMapper` |
| Service 类 | `<实体>Service` | `CustomerService`、`ContractExportService` |
| Controller 类 | `<实体>Controller` | `CustomerController` |
| 导出服务 | `<实体>ExportService` | `CustomerExportService` |
| 权限常量 | `<模块>_<动作>` | `CUSTOMER_MANAGEMENT_READ` |
| 表名 | snake_case，实体驼峰转下划线 | `Customer` → `customer` |

### 2.3 Controller 层规则

1. **注解顺序**：`@RestController` → `@RequestMapping` → `@Tag`（可选，用于 API 分组）
2. **每个接口必须包含**：
   - `@Operation(summary = "...")` 用于 OpenAPI 文档（中文描述）
   - `@RequiresPermissions(...)` 用于权限控制
   - `@Validated` 用于请求体参数校验
3. **HTTP 方法约定**：
   - `@PostMapping` 用于新增、更新、批量操作和分页列表查询
   - `@GetMapping` 用于单条查询、删除、启用/禁用
4. **URL 路径约定**：
   - 基础路径：`/<实体名>`（如 `/account` 对应客户，`/contract` 对应合同）
   - 新增：`POST /add`
   - 更新：`POST /update`
   - 删除：`GET /delete/{id}` 或 `POST /batch/delete`
   - 详情：`GET /get/{id}`
   - 分页列表：`POST /page`
   - 全量列表：`GET /list`
   - 表单配置：`GET /module/form`
   - 启用/禁用：`GET /enable/{id}`、`GET /disable/{id}`
   - 拖拽排序：`POST /edit/pos`
5. **Controller 方法中的通用模式**：
   - 获取当前用户：`SessionUtils.getUserId()`
   - 获取当前组织：`OrganizationContext.getOrganizationId()`
   - 数据权限校验：`dataScopeService.getDeptDataPermission(...)` 或 `dataScopeService.checkDataPermission(...)`
   - 条件过滤：`ConditionFilterUtils.parseCondition(request, FormKey.XXX.getKey())`

**示例**：
```java
@RestController
@RequestMapping("/account")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.CUSTOMER_MANAGEMENT_ADD)
    @Operation(summary = "添加客户")
    public Customer add(@Validated @RequestBody CustomerAddRequest request) {
        return customerService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    @GetMapping("/get/{id}")
    @RequiresPermissions(PermissionConstants.CUSTOMER_MANAGEMENT_READ)
    @Operation(summary = "客户详情")
    public CustomerGetResponse get(@PathVariable String id) {
        return customerService.getWithDataPermissionCheck(id, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }
}
```

### 2.4 Service 层规则

1. **注解**：Service 类必须标注 `@Transactional(rollbackFor = Exception.class)` + `@Slf4j`
2. **依赖注入**：使用 `@Resource`（不是 `@Autowired`）—— 这是项目标准
3. **方法命名**：
   - `add(...)` — 新增
   - `update(...)` — 更新
   - `delete(...)` / `batchDelete(...)` — 删除
   - `get(...)` / `getWithDataPermissionCheck(...)` — 按 ID 查询
   - `list(...)` — 分页列表
   - `checkResourceRef(...)` — 删除前检查引用完整性
4. **实体获取模式**：
   - 单条查询：`xxxMapper.selectByPrimaryKey(id)`
   - 批量查询：`xxxMapper.selectByIds(ids)`
   - 空值校验：`if (entity == null) { return null; }` 或抛出 `GenericException`
5. **对象拷贝模式**：`BeanUtils.copyBean(new TargetDTO(), sourceEntity)`
6. **用户名解析**：使用 `baseService.setCreateAndUpdateUserName(list)` 或 `baseService.setCreateUpdateOwnerUserName(object)`
7. **日志记录**：异常日志使用 `log.error("描述信息: {}", e.getMessage(), e)`
8. **操作日志**：使用 `@OperationLog` 注解或 `OperationLogContext.setContext(...)` 实现审计追踪
9. **消息通知**：使用 `commonNoticeSendService.sendNotice(...)` 发送业务通知

**关键规则**：通过反射调用的方法（AOP）不得修改方法签名，须在 Javadoc 中标注 `⚠️反射调用; 勿修改入参, 返回, 方法名!`

### 2.5 Mapper / DAO 层规则

1. **通用 CRUD**：使用框架提供的 `BaseMapper<Entity>` 进行标准操作（`selectByPrimaryKey`、`insert`、`update`、`deleteByIds`、`batchInsert` 等）
2. **自定义 SQL**：创建 `Ext<实体>Mapper` 接口，不继承任何类，标注 `@Mapper`
3. **XML 映射**：Mapper XML 文件放在与 Java Mapper 同包下
4. **表名规范**：CamelCase 实体名 → snake_case 表名（如 `CustomerContact` → `customer_contact`）
5. **列名规范**：camelCase 字段 → snake_case 列名，通过 `map-underscore-to-camel-case=true` 配置

### 2.6 DTO 规则

1. **请求 DTO**（位于 `dto/request/`）：
   - 每个字段使用 `@Schema(description = "...")`（中文描述）
   - 使用 JSR 303 校验注解：`@NotBlank`、`@NotNull`、`@Size`、`@Pattern` 等
   - 命名：`<实体>AddRequest`、`<实体>UpdateRequest`、`<实体>PageRequest`
2. **响应 DTO**（位于 `dto/response/`）：
   - 每个字段使用 `@Schema(description = "...")`
   - 命名：`<实体>ListResponse`、`<实体>GetResponse`
   - 包含 `moduleFields` 和 `optionMap` 以支持自定义字段
3. **通用 DTO**：`OptionDTO`、`PosRequest`、`LogDTO` 等共享 DTO 位于 framework 模块
4. **分页**：继承基础请求或使用 `PagerWithOption<List<T>>` 作为返回类型

### 2.7 异常处理

1. **业务异常**：统一抛出 `GenericException` —— 禁止抛出原始 `RuntimeException`
2. **带国际化消息**：`throw new GenericException(Translator.get("message_key"))`
3. **带错误码**：`throw new GenericException(CrmHttpResultCode.FORBIDDEN)`
4. **同时带错误码和消息**：`throw new GenericException(CrmHttpResultCode.VALIDATE_FAILED, "详情")`
5. **异常包装**：`throw new GenericException(e)` 或 `throw new GenericException(e.getMessage())`
6. **全局处理器**：`RestControllerExceptionHandler` 统一处理所有异常
7. **错误响应格式**：`ResultHolder.error(code, message)` 或 `ResultHolder.error(code, message, data)`

### 2.8 权限控制

1. **Controller 层**：`@RequiresPermissions(PermissionConstants.XXX_READ)`
2. **多权限**：`@RequiresPermissions(value = {PERM_A, PERM_B}, logical = Logical.OR)`
3. **数据范围**：使用 `DataScopeService` 实现行级数据权限：
   - `getDeptDataPermission(userId, orgId, viewId, permission)` → 返回 `DeptDataPermissionDTO`
   - `checkDataPermission(userId, orgId, ownerIds, permission)` → 无权限时抛出异常
   - `hasDataPermission(userId, orgId, owner, permission)` → 返回布尔值
4. **权限命名**：`<模块>_<动作>`（如 `CUSTOMER_MANAGEMENT_READ`、`CONTRACT_ADD`）

### 2.9 数据库迁移（Flyway）

1. **目录结构**：`resources/migration/<版本>/ddl/` 和 `resources/migration/<版本>/dml/`
2. **文件命名**：`V<版本>_<序号>__<描述>.sql`
   - 示例：`V1.4.0_2_1__data.sql`、`V1.4.0_2__ga_ddl.sql`
3. **DDL 脚本**：开头必须设置锁等待超时，结尾恢复默认值：
   ```sql
   SET SESSION innodb_lock_wait_timeout = 7200;
   -- DDL 语句...
   SET SESSION innodb_lock_wait_timeout = DEFAULT;
   ```
4. **版本表**：`vincent_crm_version`（在 commons.properties 中配置）
5. **规则**：严禁修改已有迁移脚本，必须创建新脚本

### 2.10 配置

1. **属性文件**：`commons.properties` 位于 `app/src/main/resources/`
2. **MyBatis 配置**：`map-underscore-to-camel-case=true`、`auto-mapping-behavior=full`
3. **虚拟线程**：`spring.threads.virtual.enabled=true`
4. **Lombok**：`lombok.addLombokGeneratedAnnotation=true`、`lombok.equalsAndHashCode.callSuper=call`

---

## 3. 前端编码规约（Vue.js / TypeScript）

### 3.1 Monorepo 结构

```
frontend/packages/
├── lib-shared/           # Web 与 Mobile 共享
│   ├── api/              # HTTP 客户端（Axios 封装）
│   ├── enums/            # 共享枚举（customerEnum、contractEnum 等）
│   ├── hooks/            # 共享组合式 Hooks
│   ├── locale/           # 国际化语言包（zh-CN、en-US）
│   ├── method/           # 共享工具方法
│   ├── models/           # 共享 TypeScript 接口/类型
│   └── types/            # 共享类型定义
├── web/                  # 桌面端应用
│   └── src/
│       ├── api/          # API 模块 & HTTP 客户端
│       ├── assets/       # 静态资源（样式、图标）
│       ├── components/   # 可复用组件
│       │   ├── business/ # 业务组件（crm-form-create 等）
│       │   └── pure/     # 纯展示组件
│       ├── config/       # 页面级配置
│       ├── directive/    # 自定义 Vue 指令
│       ├── enums/        # Web 端特有枚举
│       ├── hooks/        # Web 端特有组合式 Hooks
│       ├── layout/       # 布局组件
│       ├── locale/       # Web 端特有国际化覆盖
│       ├── router/       # Vue Router 配置
│       ├── store/        # Pinia Store 模块
│       ├── types/        # Web 端特有 TypeScript 类型
│       ├── utils/        # 工具函数
│       └── views/        # 页面视图（每个模块一个目录）
└── mobile/               # 移动端应用（结构与 web 相同）
```

### 3.2 组件规范

1. **使用 Composition API**，即 `<script setup lang="ts">` —— 禁止使用 Options API
2. **自动导入**：Vue API（`ref`、`computed`、`onMounted` 等）已配置自动导入 —— 无需手动 import
3. **组件自动导入**：`unplugin-vue-components` 自动注册组件 —— 无需手动 import UI 库组件
4. **组件命名**：使用 PascalCase 命名组件文件（`CustomerList.vue`，而非 `customer-list.vue`）
5. **组件分类**：
   - `pure/` — 展示型、无状态组件（图标、颜色选择器）
   - `business/` — 领域型、有状态组件（CRM 表单构建器）
6. **导出模式**：使用桶式 `index.ts` 文件导出组件目录：
   ```typescript
   import checkbox from './checkbox.vue';
   import radio from './radio.vue';
   export default { checkbox, radio };
   ```

### 3.3 API 层

1. **HTTP 客户端**：使用 `@/api/http` 中的 `CDR`（自定义 Axios 封装）
2. **共享客户端**：基础 Axios 实例来自 `@lib/shared/api/http`
3. **API 模块模式**：
   ```typescript
   import CDR from '@/api/http';
   // 分页列表
   export function getCustomerPage(data: CustomerPageRequest) {
     return CDR.post<PagerResult<CustomerListResponse>>('/account/page', data);
   }
   // 获取详情
   export function getCustomer(id: string) {
     return CDR.get<CustomerGetResponse>(`/account/get/${id}`);
   }
   // 新增
   export function addCustomer(data: CustomerAddRequest) {
     return CDR.post<Customer>('/account/add', data);
   }
   ```
4. **类型安全**：始终为请求/响应对象定义 TypeScript 接口

### 3.4 状态管理

1. **使用 Pinia** —— 禁止使用 Vuex
2. **Store 模块**：每个领域一个 Store
3. **Store 位置**：`src/store/modules/`

### 3.5 路由

1. **路由元信息**：在 `RouteMeta` 中定义权限、国际化键、图标
2. **懒加载**：使用动态 import 加载路由组件
3. **路由类型**：使用 `AppRouteRecordRaw`（带 meta 支持的自定义类型）

### 3.6 国际化（i18n）

1. **共享国际化**：`@lib/shared/locale` —— 通过 `setupI18n(app)` 初始化
2. **语言文件**：JSON 结构，使用命名空间键（如 `common.notStarted`）
3. **使用方式**：模板中使用 `$t('common.notStarted')`，后端使用 `Translator.get("key")`
4. **国际化键**：使用点号命名空间：`模块.动作.字段`

### 3.7 样式

1. **CSS 预处理器**：Less（`.less` 文件）
2. **CSS 变量**：使用 CSS 自定义属性实现主题化（`--text-n4`、`--info-blue`、`--success-green`）
3. **图标系统**：SVG 雪碧图通过 `virtual:svg-icons-register` + 自定义 iconfont

---

## 4. 横切关注点规约

### 4.1 ID 生成

- 使用 `IDGenerator.nextStr()` 生成唯一 ID —— 禁止直接使用 UUID

### 4.2 时间戳处理

- 使用 `System.currentTimeMillis()` 获取时间戳 —— 禁止使用 `LocalDateTime` 或 `Instant`
- 字段类型：`createTime`、`updateTime` 为 `Long` 类型

### 4.3 软删除与启用/禁用

- 启用/禁用：实体使用 Boolean 类型字段 `enable`
- 接口：`disable/{id}` 和 `enable/{id}`

### 4.4 对象拷贝

- 始终使用 `BeanUtils.copyBean(new TargetType(), source)` —— 禁止使用 `BeanUtils.copyProperties()`

### 4.5 集合处理

- 空值判断：`CollectionUtils.isEmpty(list)` / `CollectionUtils.isNotEmpty(list)`（Apache Commons）
- 空集合返回：`Collections.emptyList()` 或 `List.of()`
- Stream 收集：优先使用 `.toList()` 而非 `.collect(Collectors.toList())`（Java 16+）

### 4.6 字符串处理

- 空白判断：`StringUtils.isBlank(str)` / `StringUtils.isNotBlank(str)`（Apache Commons）
- 忽略大小写比较：`Strings.CI.equals(a, b)`（自定义工具类）
- 区分大小写比较：`Strings.CS.equals(a, b)`（自定义工具类）

### 4.7 国际化（后端）

- 使用 `Translator.get("message.key")` 实现国际化
- 语言键格式：snake_case（如 `customer.not.exist`、`file_cannot_be_null`）
- 语言文件：`resources/i18n/messages_zh_CN.properties`、`resources/i18n/messages_en_US.properties`

### 4.8 缓存

- 使用 `@CacheEvict` / `@Cacheable` 配合 Redis 缓存
- 自定义缓存：`ModuleFormCacheService` 缓存表单配置
- 权限缓存：`PermissionCache` 缓存用户权限

---

## 5. Git 与提交规约

### 5.1 提交信息格式

遵循 [Conventional Commits](https://www.conventionalcommits.org/)（通过 commitlint 强制执行）：

```
<type>(<scope>): <subject>

<body>

<footer>
```

**类型**：`feat`、`fix`、`docs`、`style`、`refactor`、`perf`、`test`、`build`、`ci`、`chore`、`revert`

**示例**：
- `feat(customer): 新增客户合并功能`
- `fix(contract): 修复回款计划计算错误`
- `refactor(product): 优化产品列表查询性能`

### 5.2 分支命名

- 功能分支：`feature/<模块>-<描述>`（如 `feature/customer-merge`）
- 修复分支：`fix/<模块>-<描述>`（如 `fix/contract-payment-calc`）

---

## 6. 测试规约

### 6.1 测试结构

1. **测试目录与源码目录对应**：`src/test/java/cn/vincent/crm/<module>/`
2. **测试类命名**：`<类名>Tests`（如 `CustomerContactControllerTests`）
3. **基类**：继承 `BaseTest` 获取通用测试配置
4. **注解**：`@AutoConfigureMockMvc`、`@TestMethodOrder(MethodOrderer.OrderAnnotation.class)`

### 6.2 Testcontainers

- 集成测试使用 Testcontainers（MySQL + Redis 嵌入式）
- 依赖：`testcontainers`、`embedded-mysql`、`embedded-redis`

### 6.3 测试方法命名

- 使用描述性方法名：`testAddCustomer_WhenValidRequest_ShouldReturnCustomer()`
- 或遵循现有简单操作式命名模式

---

## 7. 安全规约

### 7.1 认证

- 基于 Session 的认证，通过 Apache Shiro 实现
- JWT 用于 API Key 认证（`UserKeyService`）
- 会话用户上下文：`SessionUtils.getUserId()`、`SessionUtils.getOrganizationId()`

### 7.2 授权

- 所有接口必须标注 `@RequiresPermissions`
- 使用 `PermissionConstants` 定义权限字符串常量 —— 禁止使用硬编码字符串
- 数据级权限：始终通过 `DataScopeService` 校验

### 7.3 密码处理

- 密码存储使用 `CodingUtils.md5()` 进行 MD5 哈希
- 密码重置或用户禁用后踢出会话：`SessionUtils.kickOutUser()`

### 7.4 输入校验

- 请求体参数必须使用 `@Validated`
- 请求 DTO 中使用 JSR 303 注解
- 请求参数自动去除首尾空格（通过 `RequestParamTrimConfig` 配置）

---

## 8. AI 专属开发规则

### 8.1 代码生成优先级

1. **一致性优先**：生成的代码必须严格遵循代码库中的现有模式
2. **不引入新模式**：未经明确指示，不得引入新的架构模式、库或约定
3. **完整实现**：始终生成完整、可运行的代码 —— 禁止使用 TODO 占位符或桩代码
4. **导入正确性**：确保所有导入正确，遵循现有导入顺序

### 8.2 新增业务模块时

按以下检查清单顺序执行：

1. 创建包结构：`constants/`、`controller/`、`domain/`、`dto/request/`、`dto/response/`、`mapper/`、`service/`
2. 在 `domain/` 中创建实体类，添加 `@Table` 注解和 Lombok
3. 创建 `Ext<实体>Mapper` 接口及对应 XML
4. 创建请求/响应 DTO，添加 `@Schema` 和校验注解
5. 创建 Service 类，添加 `@Transactional(rollbackFor = Exception.class)` 和 `@Slf4j`
6. 创建 Controller 类，添加正确注解和权限校验
7. 在 `PermissionConstants` 中添加权限常量
8. 添加 Flyway 迁移脚本（DDL 和 DML）
9. 在 `messages_zh_CN.properties` 和 `messages_en_US.properties` 中添加国际化键
10. 如需要，向模块表单系统添加表单配置

### 8.3 修改已有代码时

1. **保留方法签名** —— 尤其是通过反射调用的方法（查找 `⚠️反射调用` 标记）
2. **保留注解顺序** —— 遵循文件中已有的注解排列顺序
3. **保留依赖注入方式** —— 使用 `@Resource`，不用 `@Autowired`
4. **不得更改 ID 生成方式** —— 使用 `IDGenerator.nextStr()`
5. **不得更改时间戳处理方式** —— 使用 `System.currentTimeMillis()`
6. **不得更改异常类型** —— 使用 `GenericException` + `Translator.get()`
7. **不得修改 Flyway 迁移** —— 改为创建新的迁移脚本

### 8.4 代码风格规则

1. **缩进**：Java 使用 4 个空格，TypeScript/Vue 使用 2 个空格
2. **行宽**：目标最大 120 个字符
3. **Javadoc**：公共 Service 方法必须编写；使用与现有代码一致的中文描述
4. **空行**：方法声明之间一个空行，逻辑段落之间两个空行
5. **禁止通配符导入**：使用显式导入，不使用 `.*`
6. **Lombok**：DTO 和实体类使用 `@Data`、`@Builder`、`@NoArgsConstructor`、`@AllArgsConstructor`

### 8.5 禁止事项

1. 禁止使用 `@Autowired` —— 使用 `@Resource`
2. 禁止使用 `LocalDateTime` 或 `Instant` —— 使用 `System.currentTimeMillis()` 返回 Long 型时间戳
3. 禁止使用 `UUID.randomUUID()` —— 使用 `IDGenerator.nextStr()`
4. 禁止使用 `BeanUtils.copyProperties()` —— 使用 `BeanUtils.copyBean(new Target(), source)`
5. 禁止抛出 `RuntimeException` —— 使用 `GenericException`
6. 禁止硬编码权限字符串 —— 使用 `PermissionConstants`
7. 禁止硬编码错误消息 —— 使用 `Translator.get("i18n.key")`
8. 禁止新增接口省略 `@RequiresPermissions`
9. 禁止新增接口省略 `@Operation(summary = "...")`
10. 禁止在 Vue 组件中使用 Options API —— 使用 Composition API + `<script setup lang="ts">`
11. 禁止手动导入已自动导入的 Vue Composition API
12. 禁止创建修改已有 Schema 版本的新迁移文件

### 8.6 前端 API 模块规则

创建新 API 模块时：

```typescript
// 1. 在 models/ 或 types/ 中定义类型
export interface EntityPageRequest extends BasePageRequest {
  viewId?: string;
  // ...
}

export interface EntityListResponse {
  id: string;
  name: string;
  // ...
}

// 2. 创建 API 函数
import CDR from '@/api/http';

export function getEntityPage(data: EntityPageRequest) {
  return CDR.post<PagerResult<EntityListResponse>>('/entity/page', data);
}

export function getEntity(id: string) {
  return CDR.get<EntityGetResponse>(`/entity/get/${id}`);
}

export function addEntity(data: EntityAddRequest) {
  return CDR.post<Entity>('/entity/add', data);
}

export function updateEntity(data: EntityUpdateRequest) {
  return CDR.post<Entity>('/entity/update', data);
}

export function deleteEntity(id: string) {
  return CDR.get(`/entity/delete/${id}`);
}
```

---

## 9. 常用代码模板

### 9.1 后端实体模板

```java
package cn.vincent.crm.<module>.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "<table_name>")
public class <Entity> {

    @Id
    private String id;
    private String name;
    private String organizationId;
    private String createUser;
    private String updateUser;
    private Long createTime;
    private Long updateTime;
    private Boolean enable;
}
```

### 9.2 后端 Controller 模板

```java
package cn.vincent.crm.<module>.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.context.OrganizationContext;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/<path>")
@Tag(name = "<模块名称>")
public class <Entity>Controller {

    @Resource
    private <Entity>Service <entity>Service;

    @GetMapping("/module/form")
    @RequiresPermissions(PermissionConstants.<MODULE>_READ)
    @Operation(summary = "获取表单配置")
    public ModuleFormConfigDTO getModuleFormConfig() {
        return moduleFormCacheService.getBusinessFormConfig(FormKey.<ENTITY>.getKey(), OrganizationContext.getOrganizationId());
    }

    @PostMapping("/page")
    @RequiresPermissions(PermissionConstants.<MODULE>_READ)
    @Operation(summary = "<实体>列表")
    public PagerWithOption<List<<Entity>ListResponse>> list(@Validated @RequestBody <Entity>PageRequest request) {
        ConditionFilterUtils.parseCondition(request, FormKey.<ENTITY>.getKey());
        DeptDataPermissionDTO deptDataPermission = dataScopeService.getDeptDataPermission(
                SessionUtils.getUserId(), OrganizationContext.getOrganizationId(),
                request.getViewId(), PermissionConstants.<MODULE>_READ);
        return <entity>Service.list(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId(), deptDataPermission);
    }

    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.<MODULE>_ADD)
    @Operation(summary = "添加<实体>")
    public <Entity> add(@Validated @RequestBody <Entity>AddRequest request) {
        return <entity>Service.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.<MODULE>_UPDATE)
    @Operation(summary = "更新<实体>")
    public <Entity> update(@Validated @RequestBody <Entity>UpdateRequest request) {
        return <entity>Service.update(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.<MODULE>_DELETE)
    @Operation(summary = "删除<实体>")
    public void delete(@PathVariable String id) {
        <entity>Service.delete(id);
    }

    @GetMapping("/get/{id}")
    @RequiresPermissions(PermissionConstants.<MODULE>_READ)
    @Operation(summary = "<实体>详情")
    public <Entity>GetResponse get(@PathVariable String id) {
        return <entity>Service.getWithDataPermissionCheck(id, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }
}
```

### 9.3 后端 Service 模板

```java
package cn.vincent.crm.<module>.service;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.Translator;
import cn.vincent.mybatis.BaseMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class <Entity>Service {

    @Resource
    private BaseMapper<<Entity>> <entity>Mapper;
    @Resource
    private BaseService baseService;
    @Resource
    private DataScopeService dataScopeService;

    public <Entity>GetResponse get(String id) {
        <Entity> entity = <entity>Mapper.selectByPrimaryKey(id);
        if (entity == null) {
            return null;
        }
        <Entity>GetResponse response = BeanUtils.copyBean(new <Entity>GetResponse(), entity);
        response = baseService.setCreateUpdateOwnerUserName(response);
        // 按需添加模块字段处理
        return response;
    }

    public <Entity>GetResponse getWithDataPermissionCheck(String id, String userId, String orgId) {
        <Entity>GetResponse response = get(id);
        if (response == null) {
            throw new GenericException(Translator.get("<entity>.not.exist"));
        }
        dataScopeService.checkDataPermission(userId, orgId, List.of(response.getOwner()), PermissionConstants.<MODULE>_READ);
        return response;
    }
}
```

---

## 10. 快速参考检查清单

提交代码前，请逐项核实：

- [ ] Java 代码使用 `@Resource`（不是 `@Autowired`）
- [ ] 时间戳使用 `System.currentTimeMillis()`（不是 `LocalDateTime`）
- [ ] ID 使用 `IDGenerator.nextStr()`（不是 `UUID`）
- [ ] 对象拷贝使用 `BeanUtils.copyBean(new Target(), source)`
- [ ] 异常使用 `GenericException` + `Translator.get()`
- [ ] 所有接口包含 `@RequiresPermissions` 和 `@Operation(summary = ...)`
- [ ] Service 类包含 `@Transactional(rollbackFor = Exception.class)` 和 `@Slf4j`
- [ ] 请求 DTO 包含 `@Schema` 和 JSR 303 校验注解
- [ ] Vue 组件使用 `<script setup lang="ts">`（Composition API）
- [ ] 不手动导入已自动导入的 Vue API
- [ ] 提交信息遵循 Conventional Commits 格式
- [ ] 数据库变更创建新的 Flyway 迁移脚本（绝不修改已有脚本）
- [ ] 国际化键同时添加到 zh-CN 和 en-US 属性文件
