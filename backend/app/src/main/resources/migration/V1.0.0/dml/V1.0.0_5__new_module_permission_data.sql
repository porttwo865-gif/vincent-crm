SET SESSION innodb_lock_wait_timeout = 7200;

-- 商机管理权限
INSERT INTO sys_permission (id, name, module, description) VALUES
('OPPORTUNITY_MANAGEMENT_READ', '查看商机', '商机管理', '查看商机列表和详情'),
('OPPORTUNITY_MANAGEMENT_ADD', '新增商机', '商机管理', '新增商机'),
('OPPORTUNITY_MANAGEMENT_UPDATE', '编辑商机', '商机管理', '编辑商机'),
('OPPORTUNITY_MANAGEMENT_DELETE', '删除商机', '商机管理', '删除商机')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 产品管理权限
INSERT INTO sys_permission (id, name, module, description) VALUES
('PRODUCT_MANAGEMENT_READ', '查看产品', '产品管理', '查看产品列表和详情'),
('PRODUCT_MANAGEMENT_ADD', '新增产品', '产品管理', '新增产品'),
('PRODUCT_MANAGEMENT_UPDATE', '编辑产品', '产品管理', '编辑产品'),
('PRODUCT_MANAGEMENT_DELETE', '删除产品', '产品管理', '删除产品')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 合同管理权限
INSERT INTO sys_permission (id, name, module, description) VALUES
('CONTRACT_MANAGEMENT_READ', '查看合同', '合同管理', '查看合同列表和详情'),
('CONTRACT_MANAGEMENT_ADD', '新增合同', '合同管理', '新增合同'),
('CONTRACT_MANAGEMENT_UPDATE', '编辑合同', '合同管理', '编辑合同'),
('CONTRACT_MANAGEMENT_DELETE', '删除合同', '合同管理', '删除合同')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 发票管理权限
INSERT INTO sys_permission (id, name, module, description) VALUES
('INVOICE_MANAGEMENT_READ', '查看发票', '发票管理', '查看发票列表和详情'),
('INVOICE_MANAGEMENT_ADD', '新增发票', '发票管理', '新增发票'),
('INVOICE_MANAGEMENT_UPDATE', '编辑发票', '发票管理', '编辑发票'),
('INVOICE_MANAGEMENT_DELETE', '删除发票', '发票管理', '删除发票')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 订单管理权限
INSERT INTO sys_permission (id, name, module, description) VALUES
('ORDER_MANAGEMENT_READ', '查看订单', '订单管理', '查看订单列表和详情'),
('ORDER_MANAGEMENT_ADD', '新增订单', '订单管理', '新增订单'),
('ORDER_MANAGEMENT_UPDATE', '编辑订单', '订单管理', '编辑订单'),
('ORDER_MANAGEMENT_DELETE', '删除订单', '订单管理', '删除订单')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 跟进记录权限
INSERT INTO sys_permission (id, name, module, description) VALUES
('FOLLOW_RECORD_READ', '查看跟进记录', '跟进记录', '查看跟进记录列表和详情'),
('FOLLOW_RECORD_ADD', '新增跟进记录', '跟进记录', '新增跟进记录'),
('FOLLOW_RECORD_UPDATE', '编辑跟进记录', '跟进记录', '编辑跟进记录'),
('FOLLOW_RECORD_DELETE', '删除跟进记录', '跟进记录', '删除跟进记录')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 跟进计划权限
INSERT INTO sys_permission (id, name, module, description) VALUES
('FOLLOW_PLAN_READ', '查看跟进计划', '跟进计划', '查看跟进计划列表和详情'),
('FOLLOW_PLAN_ADD', '新增跟进计划', '跟进计划', '新增跟进计划'),
('FOLLOW_PLAN_UPDATE', '编辑跟进计划', '跟进计划', '编辑跟进计划'),
('FOLLOW_PLAN_DELETE', '删除跟进计划', '跟进计划', '删除跟进计划')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 审批模板权限
INSERT INTO sys_permission (id, name, module, description) VALUES
('APPROVAL_TEMPLATE_READ', '查看审批模板', '审批模板', '查看审批模板列表和详情'),
('APPROVAL_TEMPLATE_ADD', '新增审批模板', '审批模板', '新增审批模板'),
('APPROVAL_TEMPLATE_UPDATE', '编辑审批模板', '审批模板', '编辑审批模板'),
('APPROVAL_TEMPLATE_DELETE', '删除审批模板', '审批模板', '删除审批模板')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 审批实例权限
INSERT INTO sys_permission (id, name, module, description) VALUES
('APPROVAL_INSTANCE_READ', '查看审批实例', '审批实例', '查看审批实例列表和详情'),
('APPROVAL_INSTANCE_SUBMIT', '提交审批', '审批实例', '提交审批申请')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 为管理员角色分配所有新增权限
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT REPLACE(UUID(), '-', ''), 'role_admin', p.id
FROM sys_permission p
WHERE p.id LIKE 'OPPORTUNITY_MANAGEMENT_%'
   OR p.id LIKE 'PRODUCT_MANAGEMENT_%'
   OR p.id LIKE 'CONTRACT_MANAGEMENT_%'
   OR p.id LIKE 'INVOICE_MANAGEMENT_%'
   OR p.id LIKE 'ORDER_MANAGEMENT_%'
   OR p.id LIKE 'FOLLOW_RECORD_%'
   OR p.id LIKE 'FOLLOW_PLAN_%'
   OR p.id LIKE 'APPROVAL_TEMPLATE_%'
   OR p.id LIKE 'APPROVAL_INSTANCE_%'
ON DUPLICATE KEY UPDATE permission_id = VALUES(permission_id);

SET SESSION innodb_lock_wait_timeout = DEFAULT;
