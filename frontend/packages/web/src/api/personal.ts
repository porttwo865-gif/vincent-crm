import CDR from './http';
import type { BaseEntity } from '@vincent-crm/shared';

/** 登录日志 */
export interface LoginLog {
  id: string;
  ip: string;
  location?: string;
  browser?: string;
  os?: string;
  loginTime: number;
  status: string;
}

/** 消息通知 */
export interface Notification extends BaseEntity {
  title: string;
  content: string;
  type: string;
  isRead: boolean;
  senderName?: string;
}

/** 个人信息更新 */
export interface ProfileUpdateRequest {
  name?: string;
  email?: string;
  phone?: string;
  avatar?: string;
}

/** 修改密码 */
export interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
}

/** 获取个人信息 */
export function getProfile() {
  return CDR.get('/personal/profile');
}

/** 更新个人信息 */
export function updateProfile(data: ProfileUpdateRequest) {
  return CDR.post('/personal/profile/update', data);
}

/** 修改密码 */
export function changePassword(data: ChangePasswordRequest) {
  return CDR.post('/personal/password/change', data);
}

/** 登录日志 */
export function getLoginLogs(pageNum = 1, pageSize = 20) {
  return CDR.post('/personal/login/log', { pageNum, pageSize });
}

/** 消息列表 */
export function getNotifications(pageNum = 1, pageSize = 20) {
  return CDR.post('/personal/notification', { pageNum, pageSize });
}

/** 标记已读 */
export function markNotificationRead(id: string) {
  return CDR.post(`/personal/notification/read/${id}`);
}

/** 全部已读 */
export function markAllNotificationsRead() {
  return CDR.post('/personal/notification/read/all');
}
