SET SESSION innodb_lock_wait_timeout = 7200;

-- 登录日志表
CREATE TABLE IF NOT EXISTS crm_login_log (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    user_id VARCHAR(32) NOT NULL COMMENT '用户ID',
    username VARCHAR(64) COMMENT '用户名',
    login_time BIGINT COMMENT '登录时间（时间戳）',
    ip VARCHAR(64) COMMENT '登录IP',
    user_agent VARCHAR(512) COMMENT '浏览器/客户端信息',
    status VARCHAR(16) NOT NULL DEFAULT 'success' COMMENT '状态：success/failed',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_user_id (user_id),
    KEY idx_login_time (login_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- 消息通知表
CREATE TABLE IF NOT EXISTS crm_notification (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    user_id VARCHAR(32) NOT NULL COMMENT '接收用户ID',
    title VARCHAR(256) NOT NULL COMMENT '通知标题',
    content TEXT COMMENT '通知内容',
    type VARCHAR(32) NOT NULL DEFAULT 'system' COMMENT '通知类型：system/approval/follow',
    biz_type VARCHAR(64) COMMENT '业务类型',
    biz_id VARCHAR(32) COMMENT '业务ID',
    read_status VARCHAR(16) NOT NULL DEFAULT 'unread' COMMENT '阅读状态：unread/read',
    create_user VARCHAR(32),
    update_user VARCHAR(32),
    create_time BIGINT,
    update_time BIGINT,
    KEY idx_user_id (user_id),
    KEY idx_user_read_status (user_id, read_status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';

SET SESSION innodb_lock_wait_timeout = DEFAULT;
