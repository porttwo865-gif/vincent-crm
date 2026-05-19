SET SESSION innodb_lock_wait_timeout = 7200;

-- 模块表单管理权限
INSERT INTO sys_permission (id, name, module, description) VALUES
('MODULE_FORM_READ', '查看表单配置', '模块表单', '查看模块表单和字段配置'),
('MODULE_FORM_UPDATE', '编辑表单配置', '模块表单', '添加、编辑、删除、排序模块字段')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 为管理员角色分配模块表单权限
INSERT INTO sys_role_permission (id, role_id, permission_id)
SELECT REPLACE(UUID(), '-', ''), 'role_admin', p.id
FROM sys_permission p
WHERE p.id LIKE 'MODULE_FORM%'
ON DUPLICATE KEY UPDATE permission_id = VALUES(permission_id);

SET SESSION innodb_lock_wait_timeout = DEFAULT;
