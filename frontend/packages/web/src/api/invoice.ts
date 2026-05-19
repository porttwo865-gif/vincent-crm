import CDR from './http';
import type { BaseEntity, PagerResult } from '@vincent-crm/shared';

/** 发票 */
export interface Invoice extends BaseEntity {
  invoiceNo: string;
  customerId: string;
  customerName?: string;
  contractId?: string;
  orderId?: string;
  amount: number;
  invoiceDate: number;
  type: string;
  status: string;
  taxNo?: string;
  remark?: string;
}

/** 发票分页请求 */
export interface InvoicePageRequest {
  pageNum: number;
  pageSize: number;
  keyword?: string;
  status?: string;
  type?: string;
  customerId?: string;
}

/** 发票保存请求 */
export interface InvoiceSaveRequest {
  id?: string;
  customerId: string;
  contractId?: string;
  orderId?: string;
  amount: number;
  invoiceDate: number;
  type: string;
  taxNo?: string;
  remark?: string;
}

/** 发票分页列表 */
export function getInvoicePage(data: InvoicePageRequest) {
  return CDR.post<PagerResult<Invoice>>('/invoice/page', data);
}

/** 发票详情 */
export function getInvoice(id: string) {
  return CDR.get<Invoice>(`/invoice/get/${id}`);
}

/** 新增发票 */
export function addInvoice(data: InvoiceSaveRequest) {
  return CDR.post<Invoice>('/invoice/add', data);
}

/** 更新发票 */
export function updateInvoice(data: InvoiceSaveRequest) {
  return CDR.post<Invoice>('/invoice/update', data);
}

/** 删除发票 */
export function deleteInvoice(id: string) {
  return CDR.get<void>(`/invoice/delete/${id}`);
}

/** 开票 */
export function issueInvoice(id: string) {
  return CDR.post<void>(`/invoice/issue/${id}`);
}

/** 作废发票 */
export function voidInvoice(id: string) {
  return CDR.post<void>(`/invoice/void/${id}`);
}
