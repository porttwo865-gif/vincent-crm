import CDR from './http';
import type { BaseEntity, PagerResult } from '@vincent-crm/shared';

/** 审批节点 */
export interface ApprovalNode {
  id: string;
  name: string;
  type: 'serial' | 'parallel';
  approvers: string[];
  approverNames?: string[];
  sort: number;
}

/** 审批模板 */
export interface ApprovalTemplate extends BaseEntity {
  name: string;
  module: string;
  description?: string;
  nodes: ApprovalNode[];
  isDefault: boolean;
}

/** 审批实例 */
export interface ApprovalInstance extends BaseEntity {
  templateId: string;
  templateName?: string;
  businessId: string;
  businessType: string;
  title: string;
  status: string;
  currentNodeIndex: number;
  currentNodeName?: string;
  applicantId: string;
  applicantName?: string;
  approverId?: string;
  approverName?: string;
  comment?: string;
}

/** 审批历史记录 */
export interface ApprovalHistory {
  id: string;
  instanceId: string;
  nodeName: string;
  action: string;
  operatorId: string;
  operatorName?: string;
  comment?: string;
  operateTime: number;
}

/** 审批分页请求 */
export interface ApprovalPageRequest {
  pageNum: number;
  pageSize: number;
  keyword?: string;
  status?: string;
  type?: string;
}

/** 审批模板保存 */
export interface ApprovalTemplateSaveRequest {
  id?: string;
  name: string;
  module: string;
  description?: string;
  nodes: ApprovalNode[];
  isDefault: boolean;
}

/** 审批操作请求 */
export interface ApprovalActionRequest {
  instanceId: string;
  action: 'approve' | 'reject';
  comment?: string;
}

/** 获取审批模板列表 */
export function getApprovalTemplateList() {
  return CDR.get<ApprovalTemplate[]>('/approval/template/list');
}

/** 审批模板分页 */
export function getApprovalTemplatePage(data: ApprovalPageRequest) {
  return CDR.post<PagerResult<ApprovalTemplate>>('/approval/template/page', data);
}

/** 审批模板详情 */
export function getApprovalTemplate(id: string) {
  return CDR.get<ApprovalTemplate>(`/approval/template/get/${id}`);
}

/** 保存审批模板 */
export function saveApprovalTemplate(data: ApprovalTemplateSaveRequest) {
  return CDR.post<ApprovalTemplate>('/approval/template/save', data);
}

/** 删除审批模板 */
export function deleteApprovalTemplate(id: string) {
  return CDR.get<void>(`/approval/template/delete/${id}`);
}

/** 我发起的审批 */
export function getMyApprovalPage(data: ApprovalPageRequest) {
  return CDR.post<PagerResult<ApprovalInstance>>('/approval/instance/my', data);
}

/** 待我审批 */
export function getPendingApprovalPage(data: ApprovalPageRequest) {
  return CDR.post<PagerResult<ApprovalInstance>>('/approval/instance/pending', data);
}

/** 全部审批实例 */
export function getApprovalInstancePage(data: ApprovalPageRequest) {
  return CDR.post<PagerResult<ApprovalInstance>>('/approval/instance/page', data);
}

/** 审批实例详情 */
export function getApprovalInstance(id: string) {
  return CDR.get<ApprovalInstance>(`/approval/instance/get/${id}`);
}

/** 审批历史 */
export function getApprovalHistory(instanceId: string) {
  return CDR.get<ApprovalHistory[]>(`/approval/history/${instanceId}`);
}

/** 审批操作 */
export function doApprovalAction(data: ApprovalActionRequest) {
  return CDR.post<void>('/approval/action', data);
}
