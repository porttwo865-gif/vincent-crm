SET SESSION innodb_lock_wait_timeout = 7200;

-- 线索表
CREATE TABLE IF NOT EXISTS clue (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    name VARCHAR(255) COMMENT '客户名称',
    owner VARCHAR(32) COMMENT '负责人ID',
    stage VARCHAR(32) COMMENT '阶段ID',
    last_stage VARCHAR(32) COMMENT '上次阶段',
    contact VARCHAR(255) COMMENT '联系人名称',
    phone VARCHAR(30) COMMENT '联系人电话',
    products JSON COMMENT '意向产品ID列表',
    organization_id VARCHAR(32) NOT NULL COMMENT '组织ID',
    collection_time BIGINT COMMENT '领取时间',
    in_shared_pool TINYINT(1) DEFAULT 0 COMMENT '是否在线索池',
    transition_type VARCHAR(20) COMMENT '转化类型',
    transition_id VARCHAR(32) COMMENT '转化目标ID',
    follower VARCHAR(32) COMMENT '最新跟进人',
    follow_time BIGINT COMMENT '最新跟进时间',
    pool_id VARCHAR(32) COMMENT '线索池ID',
    reason_id VARCHAR(32) COMMENT '移入原因ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_org_owner (organization_id, owner),
    KEY idx_org_pool (organization_id, in_shared_pool)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线索表';

-- 线索负责人变更历史表
CREATE TABLE IF NOT EXISTS clue_owner (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    clue_id VARCHAR(32) NOT NULL COMMENT '线索ID',
    from_owner VARCHAR(32) COMMENT '原负责人',
    to_owner VARCHAR(32) COMMENT '新负责人',
    operation_type VARCHAR(32) COMMENT '操作类型',
    operator_id VARCHAR(32) COMMENT '操作人',
    operate_time BIGINT COMMENT '操作时间',
    organization_id VARCHAR(32) COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_clue_id (clue_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线索负责人变更历史表';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
