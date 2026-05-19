# VincentCRM Backend P0 Development Plan

## Overview

Build the VincentCRM backend as a Maven multi-module Spring Boot 3.5.14 project on JDK 21. The architecture follows the technical design: `app` (startup) + `framework` (shared infra) + `crm` (business modules). We will implement all P0 features in phases, with each phase building on the previous.

---

## Task 1: Project Scaffolding & Infrastructure

Set up the Maven multi-module project structure and core dependencies.

**Deliverables:**
- Root `pom.xml` with dependency management (Spring Boot 3.5.14, MyBatis 3.0.5, Shiro 2.1.0, Redisson 3.52.0, etc.)
- `backend/app` module: Spring Boot application entry, configuration files (application.yml for MySQL, Redis, Shiro)
- `backend/framework` module: BaseModel, IDGenerator, unified response wrapper (`ResultHolder`), global exception handler, MyBatis configuration, PageHelper integration
- `backend/crm` module: empty package structure for all 13 business sub-packages
- Docker Compose for local dev: MySQL 8.0 + Redis
- Flyway/SQL init scripts for database schema creation

**Key files:**
```
backend/
  pom.xml (parent)
  app/pom.xml, src/main/java/.../Application.java, application.yml
  framework/pom.xml, src/main/java/.../base/BaseModel.java, IDGenerator, ResultHolder, GlobalExceptionHandler
  crm/pom.xml, src/main/java/cn/cordys/crm/{clue,customer,opportunity,...}
docker-compose.yml
sql/init.sql
```

---

## Task 2: Authentication & Authorization Module

Implement Shiro-based auth with RSA encryption, RBAC, and data permissions.

**Deliverables:**
- RSA key pair generation and `/rsa/key` endpoint
- `/login` endpoint with RSA password decryption + bcrypt verification
- `/is-login` status check and `/logout` endpoint
- `ShiroConfig` with filter chain (AuthFilter, CsrfFilter)
- `LocalRealm` for username/password authentication
- Redis-backed Shiro Session management (Redisson)
- `PermissionCache` with Redis caching
- `DataScopeService` for department-based data permissions (ALL/DEPT_AND_CHILD/DEPT_CUSTOM/SELF)
- `PermissionUtils` and `@RequiresPermissions` integration

**Database tables:** `sys_user`, `sys_role`, `sys_permission`, `sys_organization_user`, `sys_department`, `role_scope_dept`

---

## Task 3: System Management Module (Organization, Users, Roles)

Core system administration: departments, users, and roles with permission assignment.

**Deliverables:**
- Department CRUD with tree structure (parent-child)
- User CRUD (add, edit, enable/disable, reset password)
- Role CRUD with permission assignment and data scope configuration
- Organization management (multi-tenant via `organization_id`)
- `SessionUser` context holder for current user info

**API endpoints:**
- `POST /api/crm/v1/department/{add|update|delete|tree}`
- `POST /api/crm/v1/user/{add|update|delete|page|enable|disable|reset-password}`
- `POST /api/crm/v1/role/{add|update|delete|list|permissions}`

---

## Task 4: Dynamic Form Engine (ModuleForm)

Build the configurable form system that all business modules depend on.

**Deliverables:**
- `ModuleForm` and `ModuleField` entities and mappers
- `FormKey` enum for all module types
- `FieldType` enum (TEXT, NUMBER, DATE_TIME, SELECT, MULTI_SELECT, DATASOURCE, SUB_FORM, etc.)
- Field CRUD API for configuring module forms
- EAV storage: `xxx_field` and `xxx_field_blob` tables pattern
- Field value read/write service (used by all business modules)

**Database tables:** `module_form`, `module_field`, `module_field_blob`

---

## Task 5: Lead Management Module

Full lead lifecycle management including pool operations.

**Deliverables:**
- Lead CRUD: `POST /lead/{add|update|delete|page|detail}`
- Lead list with pagination, filtering, view-based queries, data permission
- Lead-to-Customer transformation: `POST /lead/transform`
- Move to Lead Pool: `POST /lead/move-pool`
- Lead Pool: list, claim (`POST /lead-pool/claim`), assign (`POST /lead-pool/assign`)
- Lead owner change history tracking
- Custom field support via dynamic form engine

**Database tables:** `clue`, `clue_field`, `clue_field_blob`, `clue_pool`, `clue_owner`

---

## Task 6: Customer Management Module

Customer lifecycle including contact management and pool operations.

**Deliverables:**
- Customer CRUD: `POST /account/{add|update|delete|page|detail}`
- Customer list with pagination, filtering, data permissions
- Move to Customer Pool: `POST /account/batch/move-pool`
- Customer Pool: list, claim, assign
- Contact management (nested under customer): CRUD
- Customer owner change history
- Customer contract statistics: `GET /account/{id}/contract-statistic`

**Database tables:** `customer`, `customer_field`, `customer_field_blob`, `customer_pool`, `customer_owner`, `customer_contact`

---

## Task 7: Opportunity Management Module

Opportunity tracking with stage management.

**Deliverables:**
- Opportunity CRUD: `POST /opportunity/{add|update|delete|page|detail}`
- Stage update API (supports kanban drag-and-drop): `POST /opportunity/stage`
- Association with customer and contacts
- Stage-based filtering and kanban data API
- Custom field support

**Database tables:** `opportunity`, `opportunity_field`, `opportunity_field_blob`

---

## Task 8: Contract, Payment, Invoice & Order Modules

The financial modules completing the L2C flow.

**Deliverables:**
- Contract CRUD with stage management, customer/opportunity association
- Payment Plan CRUD (linked to contract, amount validation against contract total)
- Payment Record CRUD (linked to plan, amount validation)
- Invoice CRUD (linked to contract)
- Order CRUD with stage management
- Contract statistics (total/paid/unpaid/invoiced/uninvoiced)

**Database tables:** `contract`, `contract_stage`, `payment_plan`, `payment_record`, `invoice`, `order`, `order_stage` + respective field tables

---

## Task 9: Product Management Module

Product master data for quotation and contract line items.

**Deliverables:**
- Product CRUD: `POST /product/{add|update|delete|page|detail}`
- Product list with pagination

**Database tables:** `product`, `product_field`, `product_field_blob`

---

## Task 10: Follow-up Plan & Record Module

Cross-module follow-up tracking for leads, customers, and opportunities.

**Deliverables:**
- Follow-up Plan CRUD with status management (NOT_STARTED/IN_PROGRESS/COMPLETED)
- Follow-up Record creation (linked to any resource type)
- Unified list API across all resource types

**Database tables:** `follow_plan`, `follow_record`

---

## Task 11: Approval Flow Module

Configurable multi-level approval engine.

**Deliverables:**
- Approval Flow definition CRUD (flow, version, nodes, links, conditions)
- `@HitApproval` AOP annotation for business module integration
- Approval instance creation on form submission
- Approver resolution (MEMBER/SUPERIOR/DEPT_HEAD/ROLE)
- Approval task management (approve/reject/transfer)
- Multi-approver modes (ALL/ANY/SEQUENTIAL)
- Todo list API: `GET /approval/todo`
- Approval action: `POST /approval/action`

**Database tables:** `approval_flow`, `approval_flow_version`, `approval_node`, `approval_node_approver`, `approval_node_condition`, `approval_node_link`, `approval_instance`, `approval_task`, `approval_record`

---

## Task 12: Global Search & Workbench

Cross-module search and home page data aggregation.

**Deliverables:**
- Global search: `GET /search?keyword=xxx` — searches across customer, lead, opportunity, contact, pools
- Module-level result count aggregation
- Workbench home: today's todos, recent follow-ups, key metrics summary

---

## Task 13: Personal Center & API Documentation

User profile and OpenAPI docs.

**Deliverables:**
- Personal info update (password change, avatar, name)
- Springdoc OpenAPI integration for auto-generated Swagger docs at `/swagger-ui.html`

---

## Execution Order & Dependencies

```
Task 1 (Scaffolding)
  └── Task 2 (Auth) 
       └── Task 3 (System Mgmt)
            └── Task 4 (Dynamic Form)
                 ├── Task 5 (Lead) ──┐
                 ├── Task 6 (Customer)│── Task 8 (Contract/Payment/Invoice/Order)
                 └── Task 7 (Opportunity)┘
                 └── Task 9 (Product)
                 └── Task 10 (Follow-up)
            └── Task 11 (Approval) — can start after Task 4, integrates with Task 8
  Task 12 (Search/Workbench) — after Tasks 5-9
  Task 13 (Personal/Docs) — after Task 2
```

Tasks 5, 6, 7, 9, 10 can be developed in parallel once Task 4 is complete. Task 8 depends on Tasks 6 and 7. Task 11 can proceed independently after Task 4. Task 12 depends on all business modules being in place.
