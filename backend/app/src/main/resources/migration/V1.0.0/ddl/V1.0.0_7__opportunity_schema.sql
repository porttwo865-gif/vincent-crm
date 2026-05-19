SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS opportunity (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL COMMENT '商机名称',
    customer_id VARCHAR(32) COMMENT '关联客户ID',
    contact_id VARCHAR(32) COMMENT '关联联系人ID',
    owner VARCHAR(32) COMMENT '负责人ID',
    stage VARCHAR(32) COMMENT '阶段ID',
    last_stage VARCHAR(32) COMMENT '上次阶段',
    amount DECIMAL(18,2) COMMENT '预计金额',
    expected_close_time BIGINT COMMENT '预计成交时间',
    remark TEXT COMMENT '备注',
    pos BIGINT COMMENT '看板排序',
    organization_id VARCHAR(32) NOT NULL COMMENT '组织ID',
    follower VARCHAR(32) COMMENT '最新跟进人',
    follow_time BIGINT COMMENT '最新跟进时间',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_org_owner (organization_id, owner),
    KEY idx_customer_id (customer_id),
    KEY idx_stage (organization_id, stage)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商机表';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
