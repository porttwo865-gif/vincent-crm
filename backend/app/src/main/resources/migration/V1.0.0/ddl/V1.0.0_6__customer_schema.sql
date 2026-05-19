SET SESSION innodb_lock_wait_timeout = 7200;

-- 客户表
CREATE TABLE IF NOT EXISTS customer (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    name VARCHAR(255) COMMENT '客户名称',
    owner VARCHAR(32) COMMENT '负责人ID',
    collection_time BIGINT COMMENT '领取时间',
    pool_id VARCHAR(32) COMMENT '公海池ID',
    in_shared_pool TINYINT(1) DEFAULT 0 COMMENT '是否在公海池',
    organization_id VARCHAR(32) NOT NULL COMMENT '组织ID',
    follower VARCHAR(32) COMMENT '最新跟进人',
    follow_time BIGINT COMMENT '最新跟进时间',
    reason_id VARCHAR(32) COMMENT '移入公海原因ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_org_owner (organization_id, owner),
    KEY idx_org_pool (organization_id, in_shared_pool)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

-- 客户联系人表
CREATE TABLE IF NOT EXISTS customer_contact (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    customer_id VARCHAR(32) NOT NULL COMMENT '客户ID',
    name VARCHAR(128) NOT NULL COMMENT '联系人姓名',
    phone VARCHAR(30) COMMENT '手机号',
    email VARCHAR(128) COMMENT '邮箱',
    position VARCHAR(64) COMMENT '职位',
    department VARCHAR(64) COMMENT '部门',
    is_primary TINYINT(1) DEFAULT 0 COMMENT '是否主要联系人',
    remark VARCHAR(512) COMMENT '备注',
    organization_id VARCHAR(32) COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_customer_id (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户联系人表';

-- 客户负责人变更历史表
CREATE TABLE IF NOT EXISTS customer_owner (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    customer_id VARCHAR(32) NOT NULL COMMENT '客户ID',
    from_owner VARCHAR(32) COMMENT '原负责人',
    to_owner VARCHAR(32) COMMENT '新负责人',
    operation_type VARCHAR(32) COMMENT '操作类型',
    operator_id VARCHAR(32) COMMENT '操作人',
    operate_time BIGINT COMMENT '操作时间',
    organization_id VARCHAR(32) COMMENT '组织ID',
    KEY idx_customer_id (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户负责人变更历史表';

-- 客户自定义字段值表
CREATE TABLE IF NOT EXISTS customer_field (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '业务实体ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    value TEXT COMMENT '字段值',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户自定义字段值表';

-- 客户自定义字段值 blob 表
CREATE TABLE IF NOT EXISTS customer_field_blob (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '业务实体ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    value LONGTEXT COMMENT '字段值',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户自定义字段值blob表';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
