<script setup lang="ts">
import { useMessage, useDialog } from 'naive-ui';
import { formatDate } from '@/utils';
import type { Invoice, InvoiceSaveRequest } from '@/api/invoice';
import { getInvoicePage, deleteInvoice, issueInvoice, voidInvoice } from '@/api/invoice';

const message = useMessage();
const dialog = useDialog();

const loading = ref(false);
const showForm = ref(false);
const currentInvoice = ref<InvoiceSaveRequest | null>(null);

const searchForm = reactive({ keyword: '', status: undefined as string | undefined, type: undefined as string | undefined });
const pagination = reactive({ page: 1, pageSize: 20, itemCount: 0, showSizePicker: true, pageSizes: [10, 20, 50], prefix: () => `共 ${pagination.itemCount} 条` });
const dataList = ref<Invoice[]>([]);

const statusOptions = [{ label: '待开票', value: 'pending' }, { label: '已开票', value: 'issued' }, { label: '已作废', value: 'voided' }];
const typeOptions = [{ label: '专票', value: 'special' }, { label: '普票', value: 'normal' }];
const statusMap: Record<string, { label: string; type: string }> = {
  pending: { label: '待开票', type: 'warning' },
  issued: { label: '已开票', type: 'success' },
  voided: { label: '已作废', type: 'error' },
};

const columns = [
  { title: '发票号', key: 'invoiceNo', width: 140 },
  { title: '客户', key: 'customerName', ellipsis: { tooltip: true } },
  { title: '金额', key: 'amount', width: 120, render: (row: Invoice) => `¥${row.amount.toLocaleString()}` },
  { title: '类型', key: 'type', width: 80 },
  { title: '状态', key: 'status', width: 100, render: (row: Invoice) => { const s = statusMap[row.status]; return h(NTag, { size: 'small', type: s?.type as any }, { default: () => s?.label || row.status }); } },
  { title: '开票日期', key: 'invoiceDate', width: 120, render: (row: Invoice) => formatDate(row.invoiceDate, 'YYYY-MM-DD') },
  { title: '操作', key: 'actions', width: 200, fixed: 'right', render: (row: Invoice) => h(NSpace, { size: 4 }, { default: () => [
    h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => handleEdit(row) }, { default: () => '编辑' }),
    ...(row.status === 'pending' ? [h(NButton, { text: true, type: 'success', size: 'small', onClick: () => handleIssue(row) }, { default: () => '开票' })] : []),
    ...(row.status === 'issued' ? [h(NButton, { text: true, type: 'warning', size: 'small', onClick: () => handleVoid(row) }, { default: () => '作废' })] : []),
    h(NButton, { text: true, type: 'error', size: 'small', onClick: () => handleDelete(row) }, { default: () => '删除' }),
  ]}) },
];

const mockData: Invoice[] = [
  { id: '1', invoiceNo: '20240001', customerId: 'c1', customerName: '华为技术', contractId: 'ct1', amount: 150000, invoiceDate: Date.now() - 86400000 * 19, type: '专票', status: 'issued', taxNo: '9144030019237', remark: '首付款发票', createTime: Date.now() - 86400000 * 19, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '2', invoiceNo: '20240002', customerId: 'c2', customerName: '阿里巴巴', contractId: 'ct2', amount: 80000, invoiceDate: Date.now() - 86400000 * 5, type: '普票', status: 'pending', taxNo: '9133010079', remark: '', createTime: Date.now() - 86400000 * 5, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
];

const loadList = async () => {
  loading.value = true;
  try { const res = await getInvoicePage({ pageNum: pagination.page, pageSize: pagination.pageSize, ...searchForm }); dataList.value = res.list; pagination.itemCount = res.total; }
  catch { dataList.value = mockData; pagination.itemCount = mockData.length; }
  finally { loading.value = false; }
};
const handleSearch = () => { pagination.page = 1; loadList(); };
const handleReset = () => { searchForm.keyword = ''; searchForm.status = undefined; searchForm.type = undefined; handleSearch(); };
const handleAdd = () => { currentInvoice.value = null; showForm.value = true; };
const handleEdit = (row: Invoice) => { currentInvoice.value = { id: row.id, customerId: row.customerId, contractId: row.contractId, orderId: row.orderId, amount: row.amount, invoiceDate: row.invoiceDate, type: row.type, taxNo: row.taxNo, remark: row.remark }; showForm.value = true; };
const handleDelete = (row: Invoice) => { dialog.warning({ title: '确认删除', content: `确定删除发票 "${row.invoiceNo}" 吗？`, positiveText: '确认', negativeText: '取消', onPositiveClick: async () => { await deleteInvoice(row.id); message.success('删除成功'); loadList(); } }); };
const handleIssue = (row: Invoice) => { dialog.success({ title: '确认开票', content: `确定开具发票 "${row.invoiceNo}" 吗？`, positiveText: '确认', negativeText: '取消', onPositiveClick: async () => { await issueInvoice(row.id); message.success('开票成功'); loadList(); } }); };
const handleVoid = (row: Invoice) => { dialog.warning({ title: '确认作废', content: `确定作废发票 "${row.invoiceNo}" 吗？`, positiveText: '确认', negativeText: '取消', onPositiveClick: async () => { await voidInvoice(row.id); message.success('作废成功'); loadList(); } }); };
onMounted(loadList);
</script>

<template>
  <div class="page-container">
    <n-card class="search-card" :bordered="false">
      <n-space align="center">
        <n-input v-model:value="searchForm.keyword" placeholder="搜索发票号/客户" clearable style="width: 240px" />
        <n-select v-model:value="searchForm.status" :options="statusOptions" placeholder="状态" clearable style="width: 140px" />
        <n-select v-model:value="searchForm.type" :options="typeOptions" placeholder="类型" clearable style="width: 120px" />
        <n-button type="primary" @click="handleSearch">搜索</n-button>
        <n-button @click="handleReset">重置</n-button>
      </n-space>
    </n-card>
    <n-card class="table-card" :bordered="false">
      <template #header><n-space><n-button type="primary" @click="handleAdd">新增发票</n-button></n-space></template>
      <n-data-table :columns="columns" :data="dataList" :loading="loading" :pagination="pagination" :scroll-x="850" size="medium" remote
        @update:page="(p: number) => { pagination.page = p; loadList(); }"
        @update:page-size="(s: number) => { pagination.pageSize = s; pagination.page = 1; loadList(); }" />
    </n-card>
  </div>
</template>

<style lang="less" scoped>
.page-container { display: flex; flex-direction: column; gap: 16px; }
.search-card, .table-card { border-radius: 8px; }
</style>
