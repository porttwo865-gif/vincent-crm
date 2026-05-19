SET SESSION innodb_lock_wait_timeout = 7200;

-- 订单表
CREATE TABLE IF NOT EXISTS crm_order (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    order_no VARCHAR(64) NOT NULL COMMENT '订单编号',
    customer_id VARCHAR(32) COMMENT '关联客户ID',
    contact_id VARCHAR(32) COMMENT '关联联系人ID',
    contract_id VARCHAR(32) COMMENT '关联合同ID',
    total_amount DECIMAL(18,2) COMMENT '订单总金额',
    status VARCHAR(32) COMMENT '状态：draft/confirmed/shipped/completed/cancelled',
    remark TEXT COMMENT '备注',
    organization_id VARCHAR(32) NOT NULL COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_org_customer (organization_id, customer_id),
    KEY idx_contract_id (contract_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单明细表
CREATE TABLE IF NOT EXISTS crm_order_item (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    order_id VARCHAR(32) NOT NULL COMMENT '关联订单ID',
    product_id VARCHAR(32) COMMENT '产品ID',
    product_name VARCHAR(255) COMMENT '产品名称',
    quantity INT COMMENT '数量',
    unit_price DECIMAL(18,2) COMMENT '单价',
    amount DECIMAL(18,2) COMMENT '金额',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 订单自定义字段值表
CREATE TABLE IF NOT EXISTS order_form_field (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '订单ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    internal_key VARCHAR(64) COMMENT '内部键',
    value VARCHAR(1024) COMMENT '字段值',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id),
    KEY idx_resource_field (resource_id, field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单自定义字段值表';

-- 订单自定义字段值表（大文本/JSON）
CREATE TABLE IF NOT EXISTS order_form_field_blob (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    resource_id VARCHAR(32) NOT NULL COMMENT '订单ID',
    field_id VARCHAR(32) NOT NULL COMMENT '字段ID',
    field_type VARCHAR(32) COMMENT '字段类型',
    name VARCHAR(128) COMMENT '字段名称',
    internal_key VARCHAR(64) COMMENT '内部键',
    value TEXT COMMENT '字段值（长文本/JSON）',
    create_user VARCHAR(32),
    create_time BIGINT,
    KEY idx_resource_id (resource_id),
    KEY idx_resource_field (resource_id, field_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单自定义字段值（长文本）表';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
