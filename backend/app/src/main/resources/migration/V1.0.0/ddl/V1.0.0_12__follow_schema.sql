SET SESSION innodb_lock_wait_timeout = 7200;

-- 跟进记录表
CREATE TABLE IF NOT EXISTS crm_follow_record (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    biz_type VARCHAR(32) NOT NULL COMMENT '业务类型：CLUE/CUSTOMER/OPPORTUNITY/CONTRACT',
    biz_id VARCHAR(32) NOT NULL COMMENT '业务对象ID',
    content TEXT COMMENT '跟进内容',
    follow_type VARCHAR(32) COMMENT '跟进方式：phone/visit/email/wechat/other',
    next_follow_time BIGINT COMMENT '下次跟进时间',
    attachments TEXT COMMENT '附件（JSON字符串）',
    owner VARCHAR(32) COMMENT '负责人ID',
    organization_id VARCHAR(32) NOT NULL COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_biz (biz_type, biz_id),
    KEY idx_org_owner (organization_id, owner)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='跟进记录表';

-- 跟进计划表
CREATE TABLE IF NOT EXISTS crm_follow_plan (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    biz_type VARCHAR(32) NOT NULL COMMENT '业务类型：CLUE/CUSTOMER/OPPORTUNITY/CONTRACT',
    biz_id VARCHAR(32) NOT NULL COMMENT '业务对象ID',
    plan_time BIGINT COMMENT '计划跟进时间',
    content TEXT COMMENT '计划内容',
    remind_before INT COMMENT '提前提醒分钟数',
    status VARCHAR(32) COMMENT '状态：pending/done/expired',
    owner VARCHAR(32) COMMENT '负责人ID',
    organization_id VARCHAR(32) NOT NULL COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_biz (biz_type, biz_id),
    KEY idx_org_owner (organization_id, owner),
    KEY idx_status (status, plan_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='跟进计划表';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
