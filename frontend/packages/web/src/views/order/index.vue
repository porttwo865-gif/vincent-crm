<script setup lang="ts">
import { useMessage, useDialog } from 'naive-ui';
import { formatDate } from '@/utils';
import type { Order, OrderSaveRequest } from '@/api/order';
import { getOrderPage, deleteOrder, changeOrderStatus } from '@/api/order';

const message = useMessage();
const dialog = useDialog();

const loading = ref(false);
const showForm = ref(false);
const currentOrder = ref<OrderSaveRequest | null>(null);

const searchForm = reactive({ keyword: '', status: undefined as string | undefined });
const pagination = reactive({ page: 1, pageSize: 20, itemCount: 0, showSizePicker: true, pageSizes: [10, 20, 50], prefix: () => `共 ${pagination.itemCount} 条` });
const dataList = ref<Order[]>([]);

const statusOptions = [{ label: '待确认', value: 'pending' }, { label: '已确认', value: 'confirmed' }, { label: '已发货', value: 'shipped' }, { label: '已完成', value: 'completed' }, { label: '已取消', value: 'cancelled' }];
const statusMap: Record<string, { label: string; type: string }> = {
  pending: { label: '待确认', type: 'warning' },
  confirmed: { label: '已确认', type: 'info' },
  shipped: { label: '已发货', type: 'info' },
  completed: { label: '已完成', type: 'success' },
  cancelled: { label: '已取消', type: 'error' },
};

const columns = [
  { title: '订单号', key: 'orderNo', width: 140 },
  { title: '客户', key: 'customerName', ellipsis: { tooltip: true } },
  { title: '金额', key: 'totalAmount', width: 120, render: (row: Order) => `¥${row.totalAmount.toLocaleString()}` },
  { title: '状态', key: 'status', width: 100, render: (row: Order) => { const s = statusMap[row.status]; return h(NTag, { size: 'small', type: s?.type as any }, { default: () => s?.label || row.status }); } },
  { title: '订单日期', key: 'orderDate', width: 120, render: (row: Order) => formatDate(row.orderDate, 'YYYY-MM-DD') },
  { title: '负责人', key: 'ownerName', width: 100 },
  { title: '操作', key: 'actions', width: 180, fixed: 'right', render: (row: Order) => h(NSpace, { size: 4 }, { default: () => [
    h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => handleEdit(row) }, { default: () => '编辑' }),
    h(NButton, { text: true, type: 'warning', size: 'small', onClick: () => handleChangeStatus(row) }, { default: () => '变更状态' }),
    h(NButton, { text: true, type: 'error', size: 'small', onClick: () => handleDelete(row) }, { default: () => '删除' }),
  ]}) },
];

const mockData: Order[] = [
  { id: '1', orderNo: 'SO20240001', customerId: 'c1', customerName: '华为技术', contractId: 'ct1', totalAmount: 150000, status: 'confirmed', orderDate: Date.now() - 86400000 * 5, deliveryDate: Date.now() + 86400000 * 10, ownerId: 'u1', ownerName: '张三', remark: '加急', items: [], createTime: Date.now() - 86400000 * 5, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '2', orderNo: 'SO20240002', customerId: 'c2', customerName: '阿里巴巴', contractId: 'ct2', totalAmount: 80000, status: 'pending', orderDate: Date.now() - 86400000 * 2, deliveryDate: Date.now() + 86400000 * 15, ownerId: 'u2', ownerName: '李四', remark: '', items: [], createTime: Date.now() - 86400000 * 2, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '3', orderNo: 'SO20240003', customerId: 'c3', customerName: '腾讯科技', totalAmount: 320000, status: 'completed', orderDate: Date.now() - 86400000 * 30, deliveryDate: Date.now() - 86400000 * 10, ownerId: 'u1', ownerName: '张三', remark: '', items: [], createTime: Date.now() - 86400000 * 30, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
];

const loadList = async () => {
  loading.value = true;
  try { const res = await getOrderPage({ pageNum: pagination.page, pageSize: pagination.pageSize, ...searchForm }); dataList.value = res.list; pagination.itemCount = res.total; }
  catch { dataList.value = mockData; pagination.itemCount = mockData.length; }
  finally { loading.value = false; }
};
const handleSearch = () => { pagination.page = 1; loadList(); };
const handleReset = () => { searchForm.keyword = ''; searchForm.status = undefined; handleSearch(); };
const handleAdd = () => { currentOrder.value = null; showForm.value = true; };
const handleEdit = (row: Order) => { currentOrder.value = { id: row.id, customerId: row.customerId, contractId: row.contractId, orderDate: row.orderDate, deliveryDate: row.deliveryDate, ownerId: row.ownerId, remark: row.remark, items: row.items?.map(i => ({ productId: i.productId, quantity: i.quantity, unitPrice: i.unitPrice, remark: i.remark })) || [] }; showForm.value = true; };
const handleDelete = (row: Order) => { dialog.warning({ title: '确认删除', content: `确定删除订单 "${row.orderNo}" 吗？`, positiveText: '确认', negativeText: '取消', onPositiveClick: async () => { await deleteOrder(row.id); message.success('删除成功'); loadList(); } }); };
const handleChangeStatus = (row: Order) => { dialog.info({ title: '变更状态', content: () => h(NSelect, { value: row.status, options: statusOptions, onUpdateValue: (v: string) => { row.status = v; } }), positiveText: '确认', onPositiveClick: async () => { await changeOrderStatus(row.id, row.status); message.success('状态变更成功'); loadList(); } }); };
onMounted(loadList);
</script>

<template>
  <div class="page-container">
    <n-card class="search-card" :bordered="false">
      <n-space align="center">
        <n-input v-model:value="searchForm.keyword" placeholder="搜索订单号/客户" clearable style="width: 240px" />
        <n-select v-model:value="searchForm.status" :options="statusOptions" placeholder="状态" clearable style="width: 140px" />
        <n-button type="primary" @click="handleSearch">搜索</n-button>
        <n-button @click="handleReset">重置</n-button>
      </n-space>
    </n-card>
    <n-card class="table-card" :bordered="false">
      <template #header><n-space><n-button type="primary" @click="handleAdd">新增订单</n-button></n-space></template>
      <n-data-table :columns="columns" :data="dataList" :loading="loading" :pagination="pagination" :scroll-x="800" size="medium" remote
        @update:page="(p: number) => { pagination.page = p; loadList(); }"
        @update:page-size="(s: number) => { pagination.pageSize = s; pagination.page = 1; loadList(); }" />
    </n-card>
  </div>
</template>

<style lang="less" scoped>
.page-container { display: flex; flex-direction: column; gap: 16px; }
.search-card, .table-card { border-radius: 8px; }
</style>
