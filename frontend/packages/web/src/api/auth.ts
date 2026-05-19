import CDR from './http';

/** RSA 公钥响应 */
export interface RsaKeyResponse {
  publicKey: string;
  rsaKey: string;
}

/** 登录请求 */
export interface LoginRequest {
  username: string;
  password: string;
  rsaKey: string;
}

/** 登录响应（SessionUser） */
export interface LoginResponse {
  userId: string;
  username: string;
  name: string;
  organizationId: string;
  roleIds: string[];
  permissions: string[];
}

/** 获取 RSA 公钥 */
export function getRsaKey() {
  return CDR.get<RsaKeyResponse>('/rsa/key');
}

/** 用户登录 */
export function login(data: LoginRequest) {
  return CDR.post<LoginResponse>('/login', data);
}

/** 登录状态检测 */
export function isLogin() {
  return CDR.get<boolean>('/is-login');
}

/** 退出登录 */
export function logout() {
  return CDR.get<void>('/logout');
}