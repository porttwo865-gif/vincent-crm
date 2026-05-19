/**
 * 共享 TypeScript 接口/模型定义
 * Web 端与 Mobile 端通用业务模型
 */

/** 统一响应结构 */
export interface ApiResponse<T = unknown> {
  code: number;
  message: string;
  data: T;
}

/** 分页请求基础参数 */
export interface BasePageRequest {
  /** 当前页码 */
  pageNum: number;
  /** 每页条数 */
  pageSize: number;
  /** 视图 ID */
  viewId?: string;
}

/** 分页响应结构 */
export interface PagerResult<T> {
  /** 数据列表 */
  list: T[];
  /** 总条数 */
  total: number;
  /** 当前页码 */
  pageNum: number;
  /** 每页条数 */
  pageSize: number;
}

/** 带选项的分页响应 */
export interface PagerWithOption<T> {
  /** 分页数据 */
  pager: PagerResult<T>;
  /** 选项映射（自定义字段选项） */
  optionMap: Record<string, OptionDTO[]>;
}

/** 选项 DTO */
export interface OptionDTO {
  /** 选项值 */
  value: string;
  /** 选项标签 */
  label: string;
}

/** 基础实体字段 */
export interface BaseEntity {
  /** 主键 ID */
  id: string;
  /** 组织 ID */
  organizationId: string;
  /** 创建人 */
  createUser: string;
  /** 更新人 */
  updateUser: string;
  /** 创建时间（时间戳） */
  createTime: number;
  /** 更新时间（时间戳） */
  updateTime: number;
  /** 是否启用 */
  enable: boolean;
}

/** 用户信息 */
export interface UserInfo {
  /** 用户 ID */
  id: string;
  /** 用户名 */
  name: string;
  /** 账号 */
  account: string;
  /** 邮箱 */
  email?: string;
  /** 手机号 */
  phone?: string;
  /** 组织 ID */
  organizationId: string;
  /** 组织名称 */
  organizationName?: string;
  /** 部门 ID */
  deptId?: string;
  /** 部门名称 */
  deptName?: string;
  /** 权限列表 */
  permissions: string[];
}

/** 登录请求 */
export interface LoginRequest {
  /** 账号 */
  account: string;
  /** 密码 */
  password: string;
}

/** 登录响应 */
export interface LoginResponse {
  /** 用户信息 */
  userInfo: UserInfo;
  /** 会话 Token */
  token: string;
}
