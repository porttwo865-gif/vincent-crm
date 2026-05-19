import CDR from './http';
import type { BaseEntity, PagerResult } from '@vincent-crm/shared';

/** 合同 */
export interface Contract extends BaseEntity {
  name: string;
  customerId: string;
  customerName?: string;
  opportunityId?: string;
  amount: number;
  signDate?: number;
  startDate?: number;
  endDate?: number;
  status: string;
  ownerId: string;
  ownerName?: string;
  remark?: string;
}

/** 回款计划 */
export interface PaymentPlan {
  id: string;
  contractId: string;
  amount: number;
  plannedDate: number;
  actualDate?: number;
  status: string;
  remark?: string;
}

/** 回款记录 */
export interface PaymentRecord {
  id: string;
  contractId: string;
  amount: number;
  paymentDate: number;
  paymentMethod: string;
  status: string;
  remark?: string;
}

/** 发票 */
export interface Invoice {
  id: string;
  contractId: string;
  invoiceNo: string;
  amount: number;
  invoiceDate: number;
  type: string;
  status: string;
  remark?: string;
}

/** 合同分页请求 */
export interface ContractPageRequest {
  pageNum: number;
  pageSize: number;
  keyword?: string;
  status?: string;
  ownerId?: string;
}

/** 合同保存请求 */
export interface ContractSaveRequest {
  id?: string;
  name: string;
  customerId: string;
  opportunityId?: string;
  amount: number;
  signDate?: number;
  startDate?: number;
  endDate?: number;
  ownerId: string;
  remark?: string;
}

/** 合同分页列表 */
export function getContractPage(data: ContractPageRequest) {
  return CDR.post<PagerResult<Contract>>('/contract/page', data);
}

/** 合同详情 */
export function getContract(id: string) {
  return CDR.get<Contract>(`/contract/get/${id}`);
}

/** 新增合同 */
export function addContract(data: ContractSaveRequest) {
  return CDR.post<Contract>('/contract/add', data);
}

/** 更新合同 */
export function updateContract(data: ContractSaveRequest) {
  return CDR.post<Contract>('/contract/update', data);
}

/** 删除合同 */
export function deleteContract(id: string) {
  return CDR.get<void>(`/contract/delete/${id}`);
}

/** 变更合同状态 */
export function changeContractStatus(id: string, status: string) {
  return CDR.post<void>('/contract/status', { id, status });
}

/** 获取回款计划 */
export function getPaymentPlans(contractId: string) {
  return CDR.get<PaymentPlan[]>(`/contract/plan/list/${contractId}`);
}

/** 获取回款记录 */
export function getPaymentRecords(contractId: string) {
  return CDR.get<PaymentRecord[]>(`/contract/payment/list/${contractId}`);
}

/** 获取发票列表 */
export function getContractInvoices(contractId: string) {
  return CDR.get<Invoice[]>(`/contract/invoice/list/${contractId}`);
}
