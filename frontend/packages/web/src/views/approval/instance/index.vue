<script setup lang="ts">
import { useMessage } from 'naive-ui';
import { useRouter } from 'vue-router';
import { formatDate } from '@/utils';
import type { ApprovalInstance } from '@/api/approval';
import { getMyApprovalPage, getPendingApprovalPage, getApprovalInstancePage } from '@/api/approval';

const router = useRouter();
const message = useMessage();

const loading = ref(false);
const activeTab = ref('my');

const searchForm = reactive({ keyword: '', status: undefined as string | undefined });
const pagination = reactive({ page: 1, pageSize: 20, itemCount: 0, showSizePicker: true, pageSizes: [10, 20, 50], prefix: () => `共 ${pagination.itemCount} 条` });
const dataList = ref<ApprovalInstance[]>([]);

const statusOptions = [{ label: '审批中', value: 'pending' }, { label: '已通过', value: 'approved' }, { label: '已驳回', value: 'rejected' }];
const statusMap: Record<string, { label: string; type: string }> = {
  pending: { label: '审批中', type: 'info' },
  approved: { label: '已通过', type: 'success' },
  rejected: { label: '已驳回', type: 'error' },
};

const columns = [
  { title: '标题', key: 'title', ellipsis: { tooltip: true } },
  { title: '业务类型', key: 'businessType', width: 120 },
  { title: '申请人', key: 'applicantName', width: 100 },
  { title: '当前节点', key: 'currentNodeName', width: 120 },
  { title: '状态', key: 'status', width: 100, render: (row: ApprovalInstance) => { const s = statusMap[row.status]; return h(NTag, { size: 'small', type: s?.type as any }, { default: () => s?.label || row.status }); } },
  { title: '申请时间', key: 'createTime', width: 160, render: (row: ApprovalInstance) => formatDate(row.createTime, 'YYYY-MM-DD HH:mm') },
  { title: '操作', key: 'actions', width: 120, fixed: 'right', render: (row: ApprovalInstance) => h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => router.push(`/approval/instance/detail/${row.id}`) }, { default: () => '查看' }) },
];

const mockData: ApprovalInstance[] = [
  { id: '1', templateId: 't1', templateName: '合同审批流程', businessId: 'c1', businessType: 'contract', title: '合同：软件服务合同2024', status: 'pending', currentNodeIndex: 0, currentNodeName: '部门经理审批', applicantId: 'u1', applicantName: '张三', approverId: 'u2', approverName: '李四', createTime: Date.now() - 86400000 * 2, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '2', templateId: 't2', templateName: '订单审批流程', businessId: 'o1', businessType: 'order', title: '订单：SO20240002', status: 'approved', currentNodeIndex: 1, currentNodeName: '-', applicantId: 'u2', applicantName: '李四', createTime: Date.now() - 86400000 * 5, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '3', templateId: 't1', templateName: '合同审批流程', businessId: 'c2', businessType: 'contract', title: '合同：云服务年度合同', status: 'rejected', currentNodeIndex: 0, currentNodeName: '-', applicantId: 'u1', applicantName: '张三', createTime: Date.now() - 86400000 * 10, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
];

const loadList = async () => {
  loading.value = true;
  try {
    let res;
    if (activeTab.value === 'my') res = await getMyApprovalPage({ pageNum: pagination.page, pageSize: pagination.pageSize, ...searchForm });
    else if (activeTab.value === 'pending') res = await getPendingApprovalPage({ pageNum: pagination.page, pageSize: pagination.pageSize, ...searchForm });
    else res = await getApprovalInstancePage({ pageNum: pagination.page, pageSize: pagination.pageSize, ...searchForm });
    dataList.value = res.list; pagination.itemCount = res.total;
  } catch { dataList.value = mockData; pagination.itemCount = mockData.length; }
  finally { loading.value = false; }
};
const handleSearch = () => { pagination.page = 1; loadList(); };
const handleReset = () => { searchForm.keyword = ''; searchForm.status = undefined; handleSearch(); };
watch(() => activeTab.value, () => { pagination.page = 1; loadList(); });
onMounted(loadList);
</script>

<template>
  <div class="page-container">
    <n-card class="search-card" :bordered="false">
      <n-space align="center">
        <n-input v-model:value="searchForm.keyword" placeholder="搜索标题" clearable style="width: 240px" />
        <n-select v-model:value="searchForm.status" :options="statusOptions" placeholder="状态" clearable style="width: 140px" />
        <n-button type="primary" @click="handleSearch">搜索</n-button>
        <n-button @click="handleReset">重置</n-button>
      </n-space>
    </n-card>
    <n-card class="table-card" :bordered="false">
      <n-tabs v-model:value="activeTab" type="line" @update:value="loadList">
        <n-tab-pane name="my" tab="我发起的">
          <n-data-table :columns="columns" :data="dataList" :loading="loading" :pagination="pagination" :scroll-x="800" size="medium" remote
            @update:page="(p: number) => { pagination.page = p; loadList(); }"
            @update:page-size="(s: number) => { pagination.pageSize = s; pagination.page = 1; loadList(); }" />
        </n-tab-pane>
        <n-tab-pane name="pending" tab="待我审批">
          <n-data-table :columns="columns" :data="dataList" :loading="loading" :pagination="pagination" :scroll-x="800" size="medium" remote
            @update:page="(p: number) => { pagination.page = p; loadList(); }"
            @update:page-size="(s: number) => { pagination.pageSize = s; pagination.page = 1; loadList(); }" />
        </n-tab-pane>
        <n-tab-pane name="all" tab="全部">
          <n-data-table :columns="columns" :data="dataList" :loading="loading" :pagination="pagination" :scroll-x="800" size="medium" remote
            @update:page="(p: number) => { pagination.page = p; loadList(); }"
            @update:page-size="(s: number) => { pagination.pageSize = s; pagination.page = 1; loadList(); }" />
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </div>
</template>

<style lang="less" scoped>
.page-container { display: flex; flex-direction: column; gap: 16px; }
.search-card, .table-card { border-radius: 8px; }
</style>
