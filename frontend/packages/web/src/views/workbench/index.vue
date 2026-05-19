<script setup lang="ts">
import { useMessage } from 'naive-ui';
import { formatDate } from '@/utils';
import type { WorkbenchStats, TodoItem, ActivityItem } from '@/api/workbench';
import { getWorkbenchStats, getTodoList, getActivityList } from '@/api/workbench';

const message = useMessage();

/** 统计数据 */
const stats = ref<WorkbenchStats>({
  clueCount: 0,
  customerCount: 0,
  opportunityCount: 0,
  opportunityAmount: 0,
  monthContractCount: 0,
  monthContractAmount: 0,
});

/** 待办事项 */
const todoList = ref<TodoItem[]>([]);

/** 最近动态 */
const activityList = ref<ActivityItem[]>([]);
const activityLoading = ref(false);

/** 统计卡片配置 */
const statCards = computed(() => [
  { label: '线索数', value: stats.value.clueCount, color: '#1E88E5', icon: 'M9.5 2c-1.82 0-3.53.5-5 1.35l2.99 2.99A6.47 6.47 0 009.5 6C13.09 6 16 8.91 16 12.5c0 .73-.13 1.43-.35 2.08l2.99 2.99C19.5 16.11 20 14.39 20 12.5 20 6.81 15.19 2 9.5 2z' },
  { label: '客户数', value: stats.value.customerCount, color: '#4CAF50', icon: 'M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z' },
  { label: '商机数 / 金额', value: `${stats.value.opportunityCount} / ¥${(stats.value.opportunityAmount / 10000).toFixed(1)}万`, color: '#FF9800', icon: 'M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z' },
  { label: '本月合同数 / 金额', value: `${stats.value.monthContractCount} / ¥${(stats.value.monthContractAmount / 10000).toFixed(1)}万`, color: '#9C27B0', icon: 'M14 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V8l-6-6zm-1 7V3.5L18.5 9H13z' },
]);

/** 待办类型映射 */
const todoTypeMap: Record<string, { label: string; color: string }> = {
  follow: { label: '待跟进', color: '#1E88E5' },
  approval: { label: '待审批', color: '#FF9800' },
  contract: { label: '即将到期', color: '#F44336' },
  payment: { label: '逾期回款', color: '#F44336' },
};

/** 加载数据 */
const loadData = async () => {
  try {
    stats.value = await getWorkbenchStats();
    todoList.value = await getTodoList();
    activityLoading.value = true;
    const res = await getActivityList(1, 10);
    activityList.value = res.list || [];
  } catch (e: any) {
    message.error(e.message || '加载失败');
    // Mock 数据兜底
    stats.value = {
      clueCount: 128,
      customerCount: 86,
      opportunityCount: 45,
      opportunityAmount: 3600000,
      monthContractCount: 12,
      monthContractAmount: 1800000,
    };
    todoList.value = [
      { id: '1', type: 'follow', title: '跟进张三', count: 5 },
      { id: '2', type: 'approval', title: '合同审批', count: 3 },
      { id: '3', type: 'contract', title: '合同即将到期', count: 2 },
      { id: '4', type: 'payment', title: '逾期回款', count: 1 },
    ];
    activityList.value = [
      { id: '1', content: '创建了客户 华为技术有限公司', time: Date.now() - 3600000, userName: '张三', type: 'customer' },
      { id: '2', content: '更新了商机 服务器采购项目', time: Date.now() - 7200000, userName: '李四', type: 'opportunity' },
      { id: '3', content: '签订了合同 软件服务合同', time: Date.now() - 10800000, userName: '王五', type: 'contract' },
      { id: '4', content: '完成了回款 ¥50,000', time: Date.now() - 14400000, userName: '赵六', type: 'payment' },
      { id: '5', content: '转入了线索 潜在客户A', time: Date.now() - 18000000, userName: '张三', type: 'clue' },
    ];
  } finally {
    activityLoading.value = false;
  }
};

onMounted(loadData);
</script>

<template>
  <div class="workbench-page">
    <!-- 顶部统计卡片 -->
    <n-grid :cols="4" :x-gap="16" :y-gap="16">
      <n-gi v-for="card in statCards" :key="card.label">
        <n-card class="stat-card" :bordered="false">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: card.color + '15', color: card.color }">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="28" height="28">
                <path :d="card.icon" />
              </svg>
            </div>
            <div class="stat-info">
              <div class="stat-label">{{ card.label }}</div>
              <div class="stat-value">{{ card.value }}</div>
            </div>
          </div>
        </n-card>
      </n-gi>
    </n-grid>

    <!-- 中部：待办事项 -->
    <n-card class="todo-card" title="待办事项" :bordered="false">
      <n-grid :cols="4" :x-gap="16">
        <n-gi v-for="item in todoList" :key="item.id">
          <div class="todo-item" :style="{ borderLeft: `4px solid ${todoTypeMap[item.type]?.color || '#1E88E5'}` }">
            <div class="todo-title">{{ item.title }}</div>
            <div class="todo-count">
              <n-tag :type="item.type === 'contract' || item.type === 'payment' ? 'error' : 'info'" size="small">
                {{ item.count }} 条
              </n-tag>
            </div>
          </div>
        </n-gi>
      </n-grid>
    </n-card>

    <!-- 底部：最近动态 -->
    <n-card class="activity-card" title="最近动态" :bordered="false">
      <n-timeline>
        <n-timeline-item v-for="item in activityList" :key="item.id"
          :type="item.type === 'contract' ? 'success' : item.type === 'payment' ? 'warning' : 'info'">
          <div class="activity-item">
            <span class="activity-user">{{ item.userName }}</span>
            <span class="activity-content">{{ item.content }}</span>
            <span class="activity-time">{{ formatDate(item.time, 'MM-DD HH:mm') }}</span>
          </div>
        </n-timeline-item>
      </n-timeline>
    </n-card>
  </div>
</template>

<style lang="less" scoped>
.workbench-page {
  padding: 0;
}

.stat-card {
  border-radius: 8px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-label {
  font-size: 13px;
  color: var(--text-n4);
  margin-bottom: 4px;
}

.stat-value {
  font-size: 22px;
  font-weight: 600;
  color: var(--text-n1);
}

.todo-card {
  margin-top: 16px;
  border-radius: 8px;
}

.todo-item {
  padding: 16px;
  background: var(--text-n9);
  border-radius: 4px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.todo-title {
  font-size: 14px;
  color: var(--text-n1);
}

.activity-card {
  margin-top: 16px;
  border-radius: 8px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.activity-user {
  font-weight: 600;
  color: var(--primary-8);
}

.activity-content {
  color: var(--text-n1);
}

.activity-time {
  color: var(--text-n4);
  font-size: 12px;
  margin-left: auto;
}
</style>
