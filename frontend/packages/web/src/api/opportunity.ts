import CDR from './http';
import type { BaseEntity, PagerResult } from '@vincent-crm/shared';

/** 商机 */
export interface Opportunity extends BaseEntity {
  name: string;
  customerId: string;
  customerName?: string;
  amount: number;
  stage: string;
  expectedCloseDate?: number;
  ownerId: string;
  ownerName?: string;
  source?: string;
  remark?: string;
}

/** 商机阶段 */
export interface OpportunityStage {
  id: string;
  name: string;
  sort: number;
  winProbability: number;
}

/** 商机分页请求 */
export interface OpportunityPageRequest {
  pageNum: number;
  pageSize: number;
  keyword?: string;
  stage?: string;
  ownerId?: string;
}

/** 商机保存请求 */
export interface OpportunitySaveRequest {
  id?: string;
  name: string;
  customerId: string;
  amount: number;
  stage: string;
  expectedCloseDate?: number;
  ownerId: string;
  source?: string;
  remark?: string;
}

/** 商机分页列表 */
export function getOpportunityPage(data: OpportunityPageRequest) {
  return CDR.post<PagerResult<Opportunity>>('/opportunity/page', data);
}

/** 商机详情 */
export function getOpportunity(id: string) {
  return CDR.get<Opportunity>(`/opportunity/get/${id}`);
}

/** 新增商机 */
export function addOpportunity(data: OpportunitySaveRequest) {
  return CDR.post<Opportunity>('/opportunity/add', data);
}

/** 更新商机 */
export function updateOpportunity(data: OpportunitySaveRequest) {
  return CDR.post<Opportunity>('/opportunity/update', data);
}

/** 删除商机 */
export function deleteOpportunity(id: string) {
  return CDR.get<void>(`/opportunity/delete/${id}`);
}

/** 变更商机阶段 */
export function changeOpportunityStage(id: string, stage: string) {
  return CDR.post<void>('/opportunity/change/stage', { id, stage });
}

/** 获取商机阶段列表 */
export function getOpportunityStages() {
  return CDR.get<OpportunityStage[]>('/opportunity/stages');
}

/** 看板视图数据 */
export function getOpportunityBoard() {
  return CDR.get<{ stage: OpportunityStage; opportunities: Opportunity[] }[]>('/opportunity/board');
}
