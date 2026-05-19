SET SESSION innodb_lock_wait_timeout = 7200;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    username VARCHAR(64) NOT NULL COMMENT '用户名',
    password VARCHAR(128) NOT NULL COMMENT '密码（bcrypt）',
    name VARCHAR(64) COMMENT '姓名',
    email VARCHAR(128) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像地址',
    enable TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    organization_id VARCHAR(32) COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    UNIQUE KEY uk_username_org (username, organization_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 部门表
CREATE TABLE IF NOT EXISTS sys_department (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    name VARCHAR(64) NOT NULL COMMENT '部门名称',
    parent_id VARCHAR(32) DEFAULT '0' COMMENT '父部门ID',
    sort INT DEFAULT 0 COMMENT '排序',
    organization_id VARCHAR(32) COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    name VARCHAR(64) NOT NULL COMMENT '角色名称',
    description VARCHAR(255) COMMENT '角色描述',
    data_scope VARCHAR(32) DEFAULT 'SELF' COMMENT '数据范围(ALL/DEPT_AND_CHILD/DEPT_CUSTOM/SELF)',
    enable TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    organization_id VARCHAR(32) COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS sys_permission (
    id VARCHAR(64) NOT NULL PRIMARY KEY COMMENT '权限标识',
    name VARCHAR(128) COMMENT '权限名称',
    module VARCHAR(64) COMMENT '所属模块',
    description VARCHAR(255) COMMENT '描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    role_id VARCHAR(32) NOT NULL COMMENT '角色ID',
    permission_id VARCHAR(64) NOT NULL COMMENT '权限ID',
    KEY idx_role_id (role_id),
    KEY idx_permission_id (permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    user_id VARCHAR(32) NOT NULL COMMENT '用户ID',
    role_id VARCHAR(32) NOT NULL COMMENT '角色ID',
    organization_id VARCHAR(32) COMMENT '组织ID',
    KEY idx_user_id (user_id),
    KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 组织用户关联表
CREATE TABLE IF NOT EXISTS sys_organization_user (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    user_id VARCHAR(32) NOT NULL COMMENT '用户ID',
    organization_id VARCHAR(32) NOT NULL COMMENT '组织ID',
    department_id VARCHAR(32) COMMENT '部门ID',
    KEY idx_user_id (user_id),
    KEY idx_org_id (organization_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织用户关联表';

-- 角色数据范围-部门表
CREATE TABLE IF NOT EXISTS role_scope_dept (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    role_id VARCHAR(32) NOT NULL COMMENT '角色ID',
    dept_id VARCHAR(32) NOT NULL COMMENT '部门ID',
    KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色数据范围-部门关联表';

-- 组织表
CREATE TABLE IF NOT EXISTS sys_organization (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    name VARCHAR(128) NOT NULL COMMENT '组织名称',
    description VARCHAR(255) COMMENT '描述',
    enable TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织表';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
