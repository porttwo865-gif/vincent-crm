-- 默认组织
INSERT INTO sys_organization (id, name, description, enable, create_time, update_time)
VALUES ('org_default', '默认组织', 'VincentCRM 默认组织', 1, UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000);

-- 默认管理员（密码: CordysCRM，bcrypt 加密）
INSERT INTO sys_user (id, username, password, name, enable, organization_id, create_time, update_time)
VALUES ('user_admin', 'admin', '$2a$10$VC8zLZvNWsA2FE9C9P7EBeYW7kgEolRdsJa22dnPaEjaU18V.MPY.', '系统管理员', 1, 'org_default', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000);

-- 管理员角色
INSERT INTO sys_role (id, name, description, data_scope, enable, organization_id, create_time, update_time)
VALUES ('role_admin', '系统管理员', '拥有所有权限', 'ALL', 1, 'org_default', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000);

-- 用户角色关联
INSERT INTO sys_user_role (id, user_id, role_id, organization_id)
VALUES ('ur_001', 'user_admin', 'role_admin', 'org_default');

-- 组织用户关联
INSERT INTO sys_organization_user (id, user_id, organization_id)
VALUES ('ou_001', 'user_admin', 'org_default');
