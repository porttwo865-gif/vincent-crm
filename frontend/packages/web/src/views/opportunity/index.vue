<script setup lang="ts">
import { useMessage, useDialog } from 'naive-ui';
import { useRouter } from 'vue-router';
import { formatDate } from '@/utils';
import type { Opportunity, OpportunitySaveRequest, OpportunityStage } from '@/api/opportunity';
import { getOpportunityPage, deleteOpportunity, changeOpportunityStage } from '@/api/opportunity';
import OpportunityForm from './components/OpportunityForm.vue';

const router = useRouter();
const message = useMessage();
const dialog = useDialog();

const loading = ref(false);
const showForm = ref(false);
const currentOpportunity = ref<OpportunitySaveRequest | null>(null);
const viewMode = ref<'list' | 'board'>('list');

const searchForm = reactive({ keyword: '', stage: undefined as string | undefined });
const pagination = reactive({ page: 1, pageSize: 20, itemCount: 0, showSizePicker: true, pageSizes: [10, 20, 50], prefix: () => `共 ${pagination.itemCount} 条` });
const dataList = ref<Opportunity[]>([]);

const stages = ref<OpportunityStage[]>([
  { id: 's1', name: '初步接触', sort: 1, winProbability: 10 },
  { id: 's2', name: '需求确认', sort: 2, winProbability: 30 },
  { id: 's3', name: '方案报价', sort: 3, winProbability: 50 },
  { id: 's4', name: '商务谈判', sort: 4, winProbability: 70 },
  { id: 's5', name: '赢单', sort: 5, winProbability: 100 },
  { id: 's6', name: '输单', sort: 6, winProbability: 0 },
]);

const stageOptions = computed(() => stages.value.map(s => ({ label: s.name, value: s.id })));
const stageMap = computed(() => Object.fromEntries(stages.value.map(s => [s.id, s])));

const columns = [
  { title: '商机名称', key: 'name', ellipsis: { tooltip: true } },
  { title: '客户', key: 'customerName', ellipsis: { tooltip: true } },
  { title: '金额', key: 'amount', width: 120, render: (row: Opportunity) => `¥${row.amount.toLocaleString()}` },
  { title: '阶段', key: 'stage', width: 120, render: (row: Opportunity) => h(NTag, { size: 'small', type: row.stage === 's5' ? 'success' : row.stage === 's6' ? 'error' : 'info' }, { default: () => stageMap.value[row.stage]?.name || row.stage }) },
  { title: '负责人', key: 'ownerName', width: 100 },
  { title: '预计成交', key: 'expectedCloseDate', width: 120, render: (row: Opportunity) => row.expectedCloseDate ? formatDate(row.expectedCloseDate, 'YYYY-MM-DD') : '-' },
  { title: '操作', key: 'actions', width: 180, fixed: 'right', render: (row: Opportunity) => h(NSpace, { size: 4 }, { default: () => [
    h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => handleEdit(row) }, { default: () => '编辑' }),
    h(NButton, { text: true, type: 'info', size: 'small', onClick: () => handleChangeStage(row) }, { default: () => '变更阶段' }),
    h(NButton, { text: true, type: 'error', size: 'small', onClick: () => handleDelete(row) }, { default: () => '删除' }),
  ]}) },
];

const mockData: Opportunity[] = [
  { id: '1', name: '服务器采购项目', customerId: 'c1', customerName: '华为技术', amount: 500000, stage: 's3', expectedCloseDate: Date.now() + 86400000 * 30, ownerId: 'u1', ownerName: '张三', source: '官网', createTime: Date.now() - 86400000 * 10, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '2', name: '云服务订阅', customerId: 'c2', customerName: '阿里巴巴', amount: 1200000, stage: 's4', expectedCloseDate: Date.now() + 86400000 * 15, ownerId: 'u2', ownerName: '李四', source: '展会', createTime: Date.now() - 86400000 * 20, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '3', name: '数据中心建设', customerId: 'c3', customerName: '腾讯科技', amount: 3000000, stage: 's2', expectedCloseDate: Date.now() + 86400000 * 60, ownerId: 'u1', ownerName: '张三', source: '转介绍', createTime: Date.now() - 86400000 * 5, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '4', name: '软件定制开发', customerId: 'c1', customerName: '华为技术', amount: 800000, stage: 's5', expectedCloseDate: Date.now() - 86400000 * 5, ownerId: 'u2', ownerName: '李四', source: '官网', createTime: Date.now() - 86400000 * 45, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
];

const boardData = computed(() => stages.value.map(stage => ({
  stage,
  items: mockData.filter(o => o.stage === stage.id),
})));

const loadList = async () => {
  loading.value = true;
  try {
    const res = await getOpportunityPage({ pageNum: pagination.page, pageSize: pagination.pageSize, ...searchForm });
    dataList.value = res.list; pagination.itemCount = res.total;
  } catch { dataList.value = mockData; pagination.itemCount = mockData.length; }
  finally { loading.value = false; }
};

const handleSearch = () => { pagination.page = 1; loadList(); };
const handleReset = () => { searchForm.keyword = ''; searchForm.stage = undefined; handleSearch(); };
const handleAdd = () => { currentOpportunity.value = null; showForm.value = true; };
const handleEdit = (row: Opportunity) => {
  currentOpportunity.value = { id: row.id, name: row.name, customerId: row.customerId, amount: row.amount, stage: row.stage, expectedCloseDate: row.expectedCloseDate, ownerId: row.ownerId, source: row.source, remark: row.remark };
  showForm.value = true;
};
const handleDelete = (row: Opportunity) => {
  dialog.warning({ title: '确认删除', content: `确定删除商机 "${row.name}" 吗？`, positiveText: '确认', negativeText: '取消',
    onPositiveClick: async () => { await deleteOpportunity(row.id); message.success('删除成功'); loadList(); } });
};
const handleChangeStage = (row: Opportunity) => {
  dialog.info({ title: '变更阶段', content: () => h(NSelect, { value: row.stage, options: stageOptions.value, onUpdateValue: (v: string) => { row.stage = v; } }), positiveText: '确认',
    onPositiveClick: async () => { await changeOpportunityStage(row.id, row.stage); message.success('阶段变更成功'); loadList(); } });
};

onMounted(loadList);
</script>

<template>
  <div class="page-container">
    <n-card class="search-card" :bordered="false">
      <n-space align="center" justify="space-between">
        <n-space>
          <n-input v-model:value="searchForm.keyword" placeholder="搜索商机/客户" clearable style="width: 240px" />
          <n-select v-model:value="searchForm.stage" :options="stageOptions" placeholder="阶段" clearable style="width: 140px" />
          <n-button type="primary" @click="handleSearch">搜索</n-button>
          <n-button @click="handleReset">重置</n-button>
        </n-space>
        <n-space>
          <n-button-group>
            <n-button :type="viewMode === 'list' ? 'primary' : 'default'" @click="viewMode = 'list'">列表</n-button>
            <n-button :type="viewMode === 'board' ? 'primary' : 'default'" @click="viewMode = 'board'">看板</n-button>
          </n-button-group>
          <n-button type="primary" @click="handleAdd">新增商机</n-button>
        </n-space>
      </n-space>
    </n-card>

    <!-- 列表视图 -->
    <n-card v-if="viewMode === 'list'" class="table-card" :bordered="false">
      <n-data-table :columns="columns" :data="dataList" :loading="loading" :pagination="pagination" :scroll-x="900" size="medium" remote
        @update:page="(p: number) => { pagination.page = p; loadList(); }"
        @update:page-size="(s: number) => { pagination.pageSize = s; pagination.page = 1; loadList(); }" />
    </n-card>

    <!-- 看板视图 -->
    <n-card v-else class="board-card" :bordered="false">
      <div class="board-container">
        <div v-for="col in boardData" :key="col.stage.id" class="board-column">
          <div class="board-header" :style="{ borderLeft: `4px solid ${col.stage.id === 's5' ? 'var(--success)' : col.stage.id === 's6' ? 'var(--error)' : 'var(--primary-8)'}` }">
            <span class="board-title">{{ col.stage.name }}</span>
            <n-tag size="small" round>{{ col.items.length }}</n-tag>
          </div>
          <div class="board-items">
            <n-card v-for="item in col.items" :key="item.id" size="small" class="board-item" hoverable @click="handleEdit(item)">
              <div class="item-name">{{ item.name }}</div>
              <div class="item-customer">{{ item.customerName }}</div>
              <div class="item-amount">¥{{ item.amount.toLocaleString() }}</div>
              <div class="item-owner">{{ item.ownerName }}</div>
            </n-card>
          </div>
        </div>
      </div>
    </n-card>

    <OpportunityForm v-model:show="showForm" :data="currentOpportunity" @saved="loadList" />
  </div>
</template>

<style lang="less" scoped>
.page-container { display: flex; flex-direction: column; gap: 16px; }
.search-card, .table-card, .board-card { border-radius: 8px; }
.board-container { display: flex; gap: 16px; overflow-x: auto; padding-bottom: 8px; }
.board-column { min-width: 280px; flex: 1; background: var(--text-n9); border-radius: 8px; padding: 12px; }
.board-header { display: flex; align-items: center; justify-content: space-between; padding: 8px 12px; margin-bottom: 12px; background: var(--text-n10); border-radius: 4px; }
.board-title { font-weight: 600; }
.board-items { display: flex; flex-direction: column; gap: 8px; }
.board-item { cursor: pointer; }
.item-name { font-weight: 600; margin-bottom: 4px; }
.item-customer { color: var(--text-n4); font-size: 12px; margin-bottom: 4px; }
.item-amount { color: var(--primary-8); font-weight: 600; margin-bottom: 4px; }
.item-owner { color: var(--text-n4); font-size: 12px; }
</style>
