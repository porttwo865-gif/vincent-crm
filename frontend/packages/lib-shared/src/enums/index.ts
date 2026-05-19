/**
 * 共享枚举定义
 * Web 端与 Mobile 端通用枚举
 */

/** 启用/禁用状态 */
export enum EnableStatus {
  /** 启用 */
  ENABLED = 1,
  /** 禁用 */
  DISABLED = 0,
}

/** 数据权限范围 */
export enum DataScope {
  /** 全部数据 */
  ALL = 'all',
  /** 本部门及子部门 */
  DEPT_AND_CHILD = 'dept_and_child',
  /** 本部门 */
  DEPT_ONLY = 'dept_only',
  /** 仅本人 */
  SELF_ONLY = 'self_only',
}

/** 性别 */
export enum Gender {
  /** 男 */
  MALE = 'male',
  /** 女 */
  FEMALE = 'female',
  /** 未知 */
  UNKNOWN = 'unknown',
}

/** 跟进方式 */
export enum FollowType {
  /** 电话 */
  PHONE = 'phone',
  /** 邮件 */
  EMAIL = 'email',
  /** 拜访 */
  VISIT = 'visit',
  /** 微信 */
  WECHAT = 'wechat',
  /** 其他 */
  OTHER = 'other',
}
