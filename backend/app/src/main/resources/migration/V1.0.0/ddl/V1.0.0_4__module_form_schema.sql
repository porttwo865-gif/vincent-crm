SET SESSION innodb_lock_wait_timeout = 7200;

-- 模块表单定义表
CREATE TABLE IF NOT EXISTS module_form (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    form_key VARCHAR(64) NOT NULL COMMENT '表单标识',
    name VARCHAR(128) NOT NULL COMMENT '表单名称',
    organization_id VARCHAR(32) NOT NULL COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    UNIQUE KEY uk_form_key_org (form_key, organization_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模块表单定义表';

-- 模块字段定义表
CREATE TABLE IF NOT EXISTS module_field (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    form_id VARCHAR(32) NOT NULL COMMENT '所属表单ID',
    form_key VARCHAR(64) NOT NULL COMMENT '所属表单key',
    name VARCHAR(128) NOT NULL COMMENT '字段显示名称',
    field_key VARCHAR(64) NOT NULL COMMENT '字段标识',
    field_type VARCHAR(32) NOT NULL COMMENT '字段类型',
    internal_key VARCHAR(64) COMMENT '内部关联键',
    is_system TINYINT(1) DEFAULT 0 COMMENT '是否系统内置字段',
    required TINYINT(1) DEFAULT 0 COMMENT '是否必填',
    default_value VARCHAR(512) COMMENT '默认值',
    options TEXT COMMENT '选项配置JSON',
    sort INT DEFAULT 0 COMMENT '排序',
    visible TINYINT(1) DEFAULT 1 COMMENT '是否可见',
    editable TINYINT(1) DEFAULT 1 COMMENT '是否可编辑',
    section_name VARCHAR(64) COMMENT '分组名称',
    section_sort INT DEFAULT 0 COMMENT '分组排序',
    organization_id VARCHAR(32) NOT NULL COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_form_id (form_id),
    KEY idx_form_key_org (form_key, organization_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模块字段定义表';

-- 线索自定义字段值表
CREATE TABLE IF NOT EXISTS clue_field (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '线索ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    internal_key VARCHAR(64) COMMENT '内部键',
    value VARCHAR(1024) COMMENT '字段值',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id),
    KEY idx_resource_field (resource_id, field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线索自定义字段值表';

-- 线索自定义字段值表（大文本/JSON）
CREATE TABLE IF NOT EXISTS clue_field_blob (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '线索ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    internal_key VARCHAR(64) COMMENT '内部键',
    value TEXT COMMENT '字段值（长文本/JSON）',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id),
    KEY idx_resource_field (resource_id, field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线索自定义字段值（长文本）表';

-- 客户自定义字段值表
CREATE TABLE IF NOT EXISTS customer_field (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '客户ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    internal_key VARCHAR(64) COMMENT '内部键',
    value VARCHAR(1024) COMMENT '字段值',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id),
    KEY idx_resource_field (resource_id, field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户自定义字段值表';

CREATE TABLE IF NOT EXISTS customer_field_blob (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '客户ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    internal_key VARCHAR(64) COMMENT '内部键',
    value TEXT COMMENT '字段值（长文本/JSON）',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id),
    KEY idx_resource_field (resource_id, field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户自定义字段值（长文本）表';

-- 商机自定义字段值表
CREATE TABLE IF NOT EXISTS opportunity_field (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '商机ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    internal_key VARCHAR(64) COMMENT '内部键',
    value VARCHAR(1024) COMMENT '字段值',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id),
    KEY idx_resource_field (resource_id, field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商机自定义字段值表';

CREATE TABLE IF NOT EXISTS opportunity_field_blob (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '商机ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    internal_key VARCHAR(64) COMMENT '内部键',
    value TEXT COMMENT '字段值（长文本/JSON）',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id),
    KEY idx_resource_field (resource_id, field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商机自定义字段值（长文本）表';

-- 合同自定义字段值表
CREATE TABLE IF NOT EXISTS contract_field (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '合同ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    internal_key VARCHAR(64) COMMENT '内部键',
    value VARCHAR(1024) COMMENT '字段值',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id),
    KEY idx_resource_field (resource_id, field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同自定义字段值表';

CREATE TABLE IF NOT EXISTS contract_field_blob (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '合同ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    internal_key VARCHAR(64) COMMENT '内部键',
    value TEXT COMMENT '字段值（长文本/JSON）',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id),
    KEY idx_resource_field (resource_id, field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同自定义字段值（长文本）表';

-- 产品自定义字段值表
CREATE TABLE IF NOT EXISTS product_field (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '产品ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    internal_key VARCHAR(64) COMMENT '内部键',
    value VARCHAR(1024) COMMENT '字段值',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id),
    KEY idx_resource_field (resource_id, field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品自定义字段值表';

CREATE TABLE IF NOT EXISTS product_field_blob (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '产品ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    internal_key VARCHAR(64) COMMENT '内部键',
    value TEXT COMMENT '字段值（长文本/JSON）',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id),
    KEY idx_resource_field (resource_id, field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品自定义字段值（长文本）表';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
