SET SESSION innodb_lock_wait_timeout = 7200;

-- 发票表
CREATE TABLE IF NOT EXISTS crm_invoice (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    contract_id VARCHAR(32) COMMENT '关联合同ID',
    customer_id VARCHAR(32) COMMENT '关联客户ID',
    invoice_no VARCHAR(128) COMMENT '发票编号',
    amount DECIMAL(18,2) COMMENT '发票金额',
    invoice_date BIGINT COMMENT '开票日期',
    invoice_type VARCHAR(32) COMMENT '发票类型：normal/special',
    status VARCHAR(32) COMMENT '状态：pending/issued/cancelled',
    remark TEXT COMMENT '备注',
    organization_id VARCHAR(32) NOT NULL COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_org_customer (organization_id, customer_id),
    KEY idx_contract_id (contract_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发票表';

-- 发票自定义字段值表
CREATE TABLE IF NOT EXISTS invoice_field (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '发票ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    internal_key VARCHAR(64) COMMENT '内部键',
    value VARCHAR(1024) COMMENT '字段值',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id),
    KEY idx_resource_field (resource_id, field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发票自定义字段值表';

-- 发票自定义字段值表（大文本/JSON）
CREATE TABLE IF NOT EXISTS invoice_field_blob (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '发票ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    internal_key VARCHAR(64) COMMENT '内部键',
    value TEXT COMMENT '字段值（长文本/JSON）',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id),
    KEY idx_resource_field (resource_id, field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发票自定义字段值（长文本）表';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
