import CDR from './http';
import type { PagerResult } from '@vincent-crm/shared';

/** 工作台统计卡片 */
export interface WorkbenchStats {
  clueCount: number;
  customerCount: number;
  opportunityCount: number;
  opportunityAmount: number;
  monthContractCount: number;
  monthContractAmount: number;
}

/** 待办事项 */
export interface TodoItem {
  id: string;
  type: 'follow' | 'approval' | 'contract' | 'payment';
  title: string;
  count: number;
}

/** 最近动态 */
export interface ActivityItem {
  id: string;
  content: string;
  time: number;
  userName: string;
  type: string;
}

/** 获取工作台统计 */
export function getWorkbenchStats() {
  return CDR.get<WorkbenchStats>('/workbench/stats');
}

/** 获取待办事项 */
export function getTodoList() {
  return CDR.get<TodoItem[]>('/workbench/todo');
}

/** 获取最近动态 */
export function getActivityList(pageNum = 1, pageSize = 10) {
  return CDR.post<PagerResult<ActivityItem>>('/workbench/activity', { pageNum, pageSize });
}
