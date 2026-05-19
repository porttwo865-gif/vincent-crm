SET SESSION innodb_lock_wait_timeout = 7200;

-- 合同表
CREATE TABLE IF NOT EXISTS crm_contract (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL COMMENT '合同名称',
    customer_id VARCHAR(32) COMMENT '关联客户ID',
    opportunity_id VARCHAR(32) COMMENT '关联商机ID',
    owner VARCHAR(32) COMMENT '负责人ID',
    amount DECIMAL(18,2) COMMENT '合同金额',
    start_date BIGINT COMMENT '开始日期',
    end_date BIGINT COMMENT '结束日期',
    signed_date BIGINT COMMENT '签约日期',
    status VARCHAR(32) COMMENT '状态：draft/active/completed/terminated',
    remark TEXT COMMENT '备注',
    pos BIGINT COMMENT '排序位置',
    organization_id VARCHAR(32) NOT NULL COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_org_owner (organization_id, owner),
    KEY idx_customer_id (customer_id),
    KEY idx_opportunity_id (opportunity_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同表';

-- 回款计划表
CREATE TABLE IF NOT EXISTS crm_payment_plan (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    contract_id VARCHAR(32) NOT NULL COMMENT '关联合同ID',
    plan_num INT COMMENT '期数',
    amount DECIMAL(18,2) COMMENT '计划金额',
    expected_date BIGINT COMMENT '预计回款日期',
    actual_date BIGINT COMMENT '实际回款日期',
    status VARCHAR(32) COMMENT '状态：pending/received/overdue',
    remark TEXT COMMENT '备注',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_contract_id (contract_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回款计划表';

-- 回款记录表
CREATE TABLE IF NOT EXISTS crm_payment_record (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    contract_id VARCHAR(32) NOT NULL COMMENT '关联合同ID',
    plan_id VARCHAR(32) COMMENT '关联回款计划ID',
    amount DECIMAL(18,2) COMMENT '回款金额',
    payment_date BIGINT COMMENT '回款日期',
    payment_method VARCHAR(32) COMMENT '回款方式：transfer/cash/check/other',
    remark TEXT COMMENT '备注',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_contract_id (contract_id),
    KEY idx_plan_id (plan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回款记录表';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
