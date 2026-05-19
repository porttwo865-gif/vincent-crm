SET SESSION innodb_lock_wait_timeout = 7200;

-- 权限数据初始化（系统管理模块）
INSERT INTO sys_permission (id, name, module, description) VALUES
('SYSTEM_USER_READ', '查看用户', '系统管理', '查看用户列表和详情'),
('SYSTEM_USER_ADD', '新增用户', '系统管理', '新增用户'),
('SYSTEM_USER_UPDATE', '编辑用户', '系统管理', '编辑用户、启用/禁用、重置密码'),
('SYSTEM_USER_DELETE', '删除用户', '系统管理', '删除用户'),
('SYSTEM_ROLE_READ', '查看角色', '系统管理', '查看角色列表和权限'),
('SYSTEM_ROLE_ADD', '新增角色', '系统管理', '新增角色'),
('SYSTEM_ROLE_UPDATE', '编辑角色', '系统管理', '编辑角色'),
('SYSTEM_ROLE_DELETE', '删除角色', '系统管理', '删除角色'),
('SYSTEM_DEPARTMENT_READ', '查看部门', '系统管理', '查看部门树'),
('SYSTEM_DEPARTMENT_ADD', '新增部门', '系统管理', '新增部门'),
('SYSTEM_DEPARTMENT_UPDATE', '编辑部门', '系统管理', '编辑部门'),
('SYSTEM_DEPARTMENT_DELETE', '删除部门', '系统管理', '删除部门')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 为管理员角色分配所有系统管理权限
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT REPLACE(UUID(), '-', ''), 'role_admin', p.id
FROM sys_permission p
WHERE p.id LIKE 'SYSTEM_%'
ON DUPLICATE KEY UPDATE permission_id = VALUES(permission_id);

-- 更新管理员用户的组织用户关联（补充默认部门）
UPDATE sys_organization_user SET department_id = '0' WHERE user_id = 'user_admin' AND department_id IS NULL;

SET SESSION innodb_lock_wait_timeout = DEFAULT;
