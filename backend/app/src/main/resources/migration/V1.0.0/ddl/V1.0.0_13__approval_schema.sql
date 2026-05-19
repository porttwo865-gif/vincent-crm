SET SESSION innodb_lock_wait_timeout = 7200;

-- 审批模板表
CREATE TABLE IF NOT EXISTS crm_approval_template (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    name VARCHAR(128) NOT NULL COMMENT '模板名称',
    biz_type VARCHAR(32) NOT NULL COMMENT '业务类型：CONTRACT/INVOICE/OTHER',
    description VARCHAR(512) COMMENT '描述',
    enabled TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    nodes TEXT COMMENT '审批节点配置（JSON）',
    organization_id VARCHAR(32) NOT NULL COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_org_biz_type (organization_id, biz_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批模板表';

-- 审批实例表
CREATE TABLE IF NOT EXISTS crm_approval_instance (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    template_id VARCHAR(32) NOT NULL COMMENT '模板ID',
    template_name VARCHAR(128) COMMENT '模板名称',
    biz_type VARCHAR(32) NOT NULL COMMENT '业务类型',
    biz_id VARCHAR(32) NOT NULL COMMENT '业务ID',
    biz_name VARCHAR(256) COMMENT '业务名称',
    applicant VARCHAR(32) NOT NULL COMMENT '申请人ID',
    status VARCHAR(32) NOT NULL DEFAULT 'pending' COMMENT '状态：pending/approved/rejected/cancelled',
    current_node_seq INT NOT NULL DEFAULT 1 COMMENT '当前节点序号',
    remark TEXT COMMENT '备注',
    organization_id VARCHAR(32) NOT NULL COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_org_biz (organization_id, biz_type, biz_id),
    KEY idx_applicant (applicant),
    KEY idx_template_id (template_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批实例表';

-- 审批节点记录表
CREATE TABLE IF NOT EXISTS crm_approval_node_record (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    instance_id VARCHAR(32) NOT NULL COMMENT '审批实例ID',
    node_seq INT NOT NULL COMMENT '节点序号',
    approver_id VARCHAR(32) NOT NULL COMMENT '审批人ID',
    status VARCHAR(32) NOT NULL DEFAULT 'pending' COMMENT '状态：pending/approved/rejected',
    comment TEXT COMMENT '审批意见',
    operate_time BIGINT COMMENT '操作时间',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_instance_id (instance_id),
    KEY idx_approver (approver_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批节点记录表';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
