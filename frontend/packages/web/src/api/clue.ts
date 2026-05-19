import CDR from './http';
import type { BaseEntity, PagerResult } from '@vincent-crm/shared';

/** 线索 */
export interface Clue extends BaseEntity {
  name: string;
  phone: string;
  email?: string;
  company?: string;
  source: string;
  ownerId: string;
  ownerName?: string;
  status: string;
  remark?: string;
}

/** 线索分页请求 */
export interface CluePageRequest {
  pageNum: number;
  pageSize: number;
  keyword?: string;
  source?: string;
  ownerId?: string;
  status?: string;
}

/** 线索新增/编辑请求 */
export interface ClueSaveRequest {
  id?: string;
  name: string;
  phone: string;
  email?: string;
  company?: string;
  source: string;
  ownerId: string;
  remark?: string;
}

/** 跟进记录 */
export interface FollowRecord {
  id: string;
  clueId?: string;
  customerId?: string;
  content: string;
  followType: string;
  followTime: number;
  followUserName: string;
  nextFollowTime?: number;
}

/** 线索分页列表 */
export function getCluePage(data: CluePageRequest) {
  return CDR.post<PagerResult<Clue>>('/clue/page', data);
}

/** 线索详情 */
export function getClue(id: string) {
  return CDR.get<Clue>(`/clue/get/${id}`);
}

/** 新增线索 */
export function addClue(data: ClueSaveRequest) {
  return CDR.post<Clue>('/clue/add', data);
}

/** 更新线索 */
export function updateClue(data: ClueSaveRequest) {
  return CDR.post<Clue>('/clue/update', data);
}

/** 删除线索 */
export function deleteClue(id: string) {
  return CDR.get<void>(`/clue/delete/${id}`);
}

/** 批量删除线索 */
export function batchDeleteClue(ids: string[]) {
  return CDR.post<void>('/clue/batch/delete', { ids });
}

/** 线索转客户 */
export function convertClueToCustomer(id: string) {
  return CDR.post<Clue>(`/clue/convert/${id}`);
}

/** 获取跟进记录 */
export function getClueFollowRecords(clueId: string) {
  return CDR.get<FollowRecord[]>(`/follow/clue/${clueId}`);
}

/** 添加跟进记录 */
export function addFollowRecord(data: { clueId?: string; customerId?: string; content: string; followType: string; nextFollowTime?: number }) {
  return CDR.post<FollowRecord>('/follow/add', data);
}
