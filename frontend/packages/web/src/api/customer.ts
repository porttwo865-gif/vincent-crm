import CDR from './http';
import type { BaseEntity, PagerResult } from '@vincent-crm/shared';

/** 客户 */
export interface Customer extends BaseEntity {
  name: string;
  phone?: string;
  email?: string;
  company?: string;
  industry?: string;
  source: string;
  ownerId: string;
  ownerName?: string;
  level: string;
  address?: string;
  remark?: string;
}

/** 客户联系人 */
export interface CustomerContact extends BaseEntity {
  customerId: string;
  name: string;
  phone?: string;
  email?: string;
  position?: string;
  isPrimary: boolean;
}

/** 客户分页请求 */
export interface CustomerPageRequest {
  pageNum: number;
  pageSize: number;
  keyword?: string;
  industry?: string;
  ownerId?: string;
  level?: string;
}

/** 客户保存请求 */
export interface CustomerSaveRequest {
  id?: string;
  name: string;
  phone?: string;
  email?: string;
  company?: string;
  industry?: string;
  source: string;
  ownerId: string;
  level: string;
  address?: string;
  remark?: string;
}

/** 客户联系人保存 */
export interface ContactSaveRequest {
  id?: string;
  customerId: string;
  name: string;
  phone?: string;
  email?: string;
  position?: string;
  isPrimary: boolean;
}

/** 客户分页列表 */
export function getCustomerPage(data: CustomerPageRequest) {
  return CDR.post<PagerResult<Customer>>('/customer/page', data);
}

/** 客户详情 */
export function getCustomer(id: string) {
  return CDR.get<Customer>(`/customer/get/${id}`);
}

/** 新增客户 */
export function addCustomer(data: CustomerSaveRequest) {
  return CDR.post<Customer>('/customer/add', data);
}

/** 更新客户 */
export function updateCustomer(data: CustomerSaveRequest) {
  return CDR.post<Customer>('/customer/update', data);
}

/** 删除客户 */
export function deleteCustomer(id: string) {
  return CDR.get<void>(`/customer/delete/${id}`);
}

/** 客户公海池列表 */
export function getCustomerSeaPage(data: CustomerPageRequest) {
  return CDR.post<PagerResult<Customer>>('/customer/sea/page', data);
}

/** 领取公海客户 */
export function claimCustomer(id: string) {
  return CDR.post<void>(`/customer/claim/${id}`);
}

/** 获取客户联系人 */
export function getCustomerContacts(customerId: string) {
  return CDR.get<CustomerContact[]>(`/customer/contact/list/${customerId}`);
}

/** 保存联系人 */
export function saveCustomerContact(data: ContactSaveRequest) {
  return CDR.post<CustomerContact>('/customer/contact/save', data);
}

/** 删除联系人 */
export function deleteCustomerContact(id: string) {
  return CDR.get<void>(`/customer/contact/delete/${id}`);
}
