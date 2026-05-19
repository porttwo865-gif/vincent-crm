import CDR from './http';
import type { BaseEntity, PagerResult } from '@vincent-crm/shared';

/** 用户 */
export interface SystemUser extends BaseEntity {
  account: string;
  name: string;
  email?: string;
  phone?: string;
  deptId?: string;
  deptName?: string;
  roleIds?: string[];
  roleNames?: string[];
  gender?: string;
  lastLoginTime?: number;
}

/** 角色 */
export interface SystemRole extends BaseEntity {
  name: string;
  code: string;
  description?: string;
  permissionIds?: string[];
}

/** 部门 */
export interface SystemDepartment extends BaseEntity {
  name: string;
  parentId?: string;
  sort: number;
  leaderId?: string;
  leaderName?: string;
  children?: SystemDepartment[];
}

/** 权限 */
export interface SystemPermission {
  id: string;
  name: string;
  code: string;
  parentId?: string;
  type: string;
  sort: number;
  children?: SystemPermission[];
}

/** 用户分页请求 */
export interface UserPageRequest {
  pageNum: number;
  pageSize: number;
  keyword?: string;
  deptId?: string;
  roleId?: string;
}

/** 用户保存 */
export interface UserSaveRequest {
  id?: string;
  account: string;
  name: string;
  email?: string;
  phone?: string;
  deptId?: string;
  roleIds?: string[];
  gender?: string;
  password?: string;
}

/** 角色保存 */
export interface RoleSaveRequest {
  id?: string;
  name: string;
  code: string;
  description?: string;
  permissionIds?: string[];
}

/** 部门保存 */
export interface DepartmentSaveRequest {
  id?: string;
  name: string;
  parentId?: string;
  sort: number;
  leaderId?: string;
}

/** 用户分页 */
export function getUserPage(data: UserPageRequest) {
  return CDR.post<PagerResult<SystemUser>>('/system/user/page', data);
}

/** 用户详情 */
export function getUser(id: string) {
  return CDR.get<SystemUser>(`/system/user/get/${id}`);
}

/** 保存用户 */
export function saveUser(data: UserSaveRequest) {
  return CDR.post<SystemUser>('/system/user/save', data);
}

/** 删除用户 */
export function deleteUser(id: string) {
  return CDR.get<void>(`/system/user/delete/${id}`);
}

/** 重置密码 */
export function resetUserPassword(id: string) {
  return CDR.post<string>(`/system/user/reset/password/${id}`);
}

/** 角色列表 */
export function getRoleList() {
  return CDR.get<SystemRole[]>('/system/role/list');
}

/** 角色分页 */
export function getRolePage(data: { pageNum: number; pageSize: number; keyword?: string }) {
  return CDR.post<PagerResult<SystemRole>>('/system/role/page', data);
}

/** 角色详情 */
export function getRole(id: string) {
  return CDR.get<SystemRole>(`/system/role/get/${id}`);
}

/** 保存角色 */
export function saveRole(data: RoleSaveRequest) {
  return CDR.post<SystemRole>('/system/role/save', data);
}

/** 删除角色 */
export function deleteRole(id: string) {
  return CDR.get<void>(`/system/role/delete/${id}`);
}

/** 部门树 */
export function getDepartmentTree() {
  return CDR.get<SystemDepartment[]>('/system/department/tree');
}

/** 保存部门 */
export function saveDepartment(data: DepartmentSaveRequest) {
  return CDR.post<SystemDepartment>('/system/department/save', data);
}

/** 删除部门 */
export function deleteDepartment(id: string) {
  return CDR.get<void>(`/system/department/delete/${id}`);
}

/** 权限树 */
export function getPermissionTree() {
  return CDR.get<SystemPermission[]>('/system/permission/tree');
}
