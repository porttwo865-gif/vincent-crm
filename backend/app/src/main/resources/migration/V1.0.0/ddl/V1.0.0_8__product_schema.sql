SET SESSION innodb_lock_wait_timeout = 7200;

CREATE TABLE IF NOT EXISTS product (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL COMMENT '产品名称',
    code VARCHAR(64) COMMENT '产品编码',
    category VARCHAR(64) COMMENT '分类',
    price DECIMAL(18,2) COMMENT '标准价格',
    unit VARCHAR(32) COMMENT '单位',
    description TEXT COMMENT '描述',
    enable TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    sort INT DEFAULT 0 COMMENT '排序',
    organization_id VARCHAR(32) NOT NULL COMMENT '组织ID',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_org_id (organization_id),
    KEY idx_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
