<script setup lang="ts">
import { useMessage, useDialog } from 'naive-ui';
import { useRouter } from 'vue-router';
import { formatDate } from '@/utils';
import type { Contract, ContractSaveRequest } from '@/api/contract';
import { getContractPage, deleteContract, changeContractStatus } from '@/api/contract';

const router = useRouter();
const message = useMessage();
const dialog = useDialog();

const loading = ref(false);
const showForm = ref(false);
const currentContract = ref<ContractSaveRequest | null>(null);

const searchForm = reactive({ keyword: '', status: undefined as string | undefined });
const pagination = reactive({ page: 1, pageSize: 20, itemCount: 0, showSizePicker: true, pageSizes: [10, 20, 50], prefix: () => `共 ${pagination.itemCount} 条` });
const dataList = ref<Contract[]>([]);

const statusOptions = [{ label: '草稿', value: 'draft' }, { label: '执行中', value: 'executing' }, { label: '已完成', value: 'completed' }, { label: '终止', value: 'terminated' }];
const statusMap: Record<string, { label: string; type: string }> = {
  draft: { label: '草稿', type: 'default' },
  executing: { label: '执行中', type: 'info' },
  completed: { label: '已完成', type: 'success' },
  terminated: { label: '终止', type: 'error' },
};

const columns = [
  { title: '合同名称', key: 'name', ellipsis: { tooltip: true } },
  { title: '客户', key: 'customerName', ellipsis: { tooltip: true } },
  { title: '金额', key: 'amount', width: 120, render: (row: Contract) => `¥${row.amount.toLocaleString()}` },
  { title: '状态', key: 'status', width: 100, render: (row: Contract) => { const s = statusMap[row.status]; return h(NTag, { size: 'small', type: s?.type as any }, { default: () => s?.label || row.status }); } },
  { title: '签订日期', key: 'signDate', width: 120, render: (row: Contract) => row.signDate ? formatDate(row.signDate, 'YYYY-MM-DD') : '-' },
  { title: '负责人', key: 'ownerName', width: 100 },
  { title: '操作', key: 'actions', width: 220, fixed: 'right', render: (row: Contract) => h(NSpace, { size: 4 }, { default: () => [
    h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => handleEdit(row) }, { default: () => '编辑' }),
    h(NButton, { text: true, type: 'info', size: 'small', onClick: () => router.push(`/contract/detail/${row.id}`) }, { default: () => '详情' }),
    h(NButton, { text: true, type: 'warning', size: 'small', onClick: () => handleChangeStatus(row) }, { default: () => '变更状态' }),
    h(NButton, { text: true, type: 'error', size: 'small', onClick: () => handleDelete(row) }, { default: () => '删除' }),
  ]}) },
];

const mockData: Contract[] = [
  { id: '1', name: '软件服务合同2024', customerId: 'c1', customerName: '华为技术', amount: 500000, signDate: Date.now() - 86400000 * 30, startDate: Date.now() - 86400000 * 30, endDate: Date.now() + 86400000 * 335, status: 'executing', ownerId: 'u1', ownerName: '张三', createTime: Date.now() - 86400000 * 30, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '2', name: '云服务年度合同', customerId: 'c2', customerName: '阿里巴巴', amount: 1200000, signDate: Date.now() - 86400000 * 60, startDate: Date.now() - 86400000 * 60, endDate: Date.now() + 86400000 * 305, status: 'executing', ownerId: 'u2', ownerName: '李四', createTime: Date.now() - 86400000 * 60, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '3', name: '硬件采购合同', customerId: 'c3', customerName: '腾讯科技', amount: 300000, signDate: Date.now() - 86400000 * 90, startDate: Date.now() - 86400000 * 90, endDate: Date.now() - 86400000 * 10, status: 'completed', ownerId: 'u1', ownerName: '张三', createTime: Date.now() - 86400000 * 90, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
];

const loadList = async () => {
  loading.value = true;
  try { const res = await getContractPage({ pageNum: pagination.page, pageSize: pagination.pageSize, ...searchForm }); dataList.value = res.list; pagination.itemCount = res.total; }
  catch { dataList.value = mockData; pagination.itemCount = mockData.length; }
  finally { loading.value = false; }
};
const handleSearch = () => { pagination.page = 1; loadList(); };
const handleReset = () => { searchForm.keyword = ''; searchForm.status = undefined; handleSearch(); };
const handleAdd = () => { currentContract.value = null; showForm.value = true; };
const handleEdit = (row: Contract) => {
  currentContract.value = { id: row.id, name: row.name, customerId: row.customerId, opportunityId: row.opportunityId, amount: row.amount, signDate: row.signDate, startDate: row.startDate, endDate: row.endDate, ownerId: row.ownerId, remark: row.remark };
  showForm.value = true;
};
const handleDelete = (row: Contract) => {
  dialog.warning({ title: '确认删除', content: `确定删除合同 "${row.name}" 吗？`, positiveText: '确认', negativeText: '取消',
    onPositiveClick: async () => { await deleteContract(row.id); message.success('删除成功'); loadList(); } });
};
const handleChangeStatus = (row: Contract) => {
  dialog.info({ title: '变更状态', content: () => h(NSelect, { value: row.status, options: statusOptions, onUpdateValue: (v: string) => { row.status = v; } }), positiveText: '确认',
    onPositiveClick: async () => { await changeContractStatus(row.id, row.status); message.success('状态变更成功'); loadList(); } });
};
onMounted(loadList);
</script>

<template>
  <div class="page-container">
    <n-card class="search-card" :bordered="false">
      <n-space align="center">
        <n-input v-model:value="searchForm.keyword" placeholder="搜索合同/客户" clearable style="width: 240px" />
        <n-select v-model:value="searchForm.status" :options="statusOptions" placeholder="状态" clearable style="width: 140px" />
        <n-button type="primary" @click="handleSearch">搜索</n-button>
        <n-button @click="handleReset">重置</n-button>
      </n-space>
    </n-card>
    <n-card class="table-card" :bordered="false">
      <template #header><n-space><n-button type="primary" @click="handleAdd">新增合同</n-button></n-space></template>
      <n-data-table :columns="columns" :data="dataList" :loading="loading" :pagination="pagination" :scroll-x="900" size="medium" remote
        @update:page="(p: number) => { pagination.page = p; loadList(); }"
        @update:page-size="(s: number) => { pagination.pageSize = s; pagination.page = 1; loadList(); }" />
    </n-card>
  </div>
</template>

<style lang="less" scoped>
.page-container { display: flex; flex-direction: column; gap: 16px; }
.search-card, .table-card { border-radius: 8px; }
</style>
