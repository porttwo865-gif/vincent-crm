<script setup lang="ts">
import { useMessage, useDialog } from 'naive-ui';
import type { Product, ProductSaveRequest } from '@/api/product';
import { getProductPage, deleteProduct, toggleProductEnable } from '@/api/product';
import ProductForm from './components/ProductForm.vue';

const message = useMessage();
const dialog = useDialog();

const loading = ref(false);
const showForm = ref(false);
const currentProduct = ref<ProductSaveRequest | null>(null);

const searchForm = reactive({ keyword: '', category: undefined as string | undefined });
const pagination = reactive({ page: 1, pageSize: 20, itemCount: 0, showSizePicker: true, pageSizes: [10, 20, 50], prefix: () => `共 ${pagination.itemCount} 条` });
const dataList = ref<Product[]>([]);

const categoryOptions = [{ label: '软件', value: 'software' }, { label: '硬件', value: 'hardware' }, { label: '服务', value: 'service' }];

const columns = [
  { title: '产品名称', key: 'name', ellipsis: { tooltip: true } },
  { title: '编码', key: 'code', width: 120 },
  { title: '分类', key: 'category', width: 100 },
  { title: '单价', key: 'price', width: 120, render: (row: Product) => `¥${row.price.toLocaleString()}` },
  { title: '单位', key: 'unit', width: 80 },
  { title: '状态', key: 'enable', width: 80, render: (row: Product) => h(NSwitch, { value: row.enable, onUpdateValue: (v: boolean) => handleToggle(row, v) }) },
  { title: '操作', key: 'actions', width: 140, fixed: 'right', render: (row: Product) => h(NSpace, { size: 4 }, { default: () => [
    h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => handleEdit(row) }, { default: () => '编辑' }),
    h(NButton, { text: true, type: 'error', size: 'small', onClick: () => handleDelete(row) }, { default: () => '删除' }),
  ]}) },
];

const mockData: Product[] = [
  { id: '1', name: 'CRM 标准版', code: 'P001', category: '软件', price: 50000, unit: '套', description: '标准 CRM 软件', createTime: Date.now() - 86400000 * 30, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '2', name: '服务器 ECS-01', code: 'P002', category: '硬件', price: 12000, unit: '台', description: '云服务器', createTime: Date.now() - 86400000 * 60, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '3', name: '技术支持服务', code: 'P003', category: '服务', price: 20000, unit: '年', description: '年度技术支持', createTime: Date.now() - 86400000 * 10, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
];

const loadList = async () => {
  loading.value = true;
  try { const res = await getProductPage({ pageNum: pagination.page, pageSize: pagination.pageSize, ...searchForm }); dataList.value = res.list; pagination.itemCount = res.total; }
  catch { dataList.value = mockData; pagination.itemCount = mockData.length; }
  finally { loading.value = false; }
};
const handleSearch = () => { pagination.page = 1; loadList(); };
const handleReset = () => { searchForm.keyword = ''; searchForm.category = undefined; handleSearch(); };
const handleAdd = () => { currentProduct.value = null; showForm.value = true; };
const handleEdit = (row: Product) => { currentProduct.value = { id: row.id, name: row.name, code: row.code, category: row.category, price: row.price, unit: row.unit, description: row.description, spec: row.spec }; showForm.value = true; };
const handleDelete = (row: Product) => {
  dialog.warning({ title: '确认删除', content: `确定删除产品 "${row.name}" 吗？`, positiveText: '确认', negativeText: '取消',
    onPositiveClick: async () => { await deleteProduct(row.id); message.success('删除成功'); loadList(); } });
};
const handleToggle = async (row: Product, val: boolean) => {
  await toggleProductEnable(row.id, val); row.enable = val; message.success(val ? '已启用' : '已禁用');
};
onMounted(loadList);
</script>

<template>
  <div class="page-container">
    <n-card class="search-card" :bordered="false">
      <n-space align="center">
        <n-input v-model:value="searchForm.keyword" placeholder="搜索产品名称/编码" clearable style="width: 240px" />
        <n-select v-model:value="searchForm.category" :options="categoryOptions" placeholder="分类" clearable style="width: 140px" />
        <n-button type="primary" @click="handleSearch">搜索</n-button>
        <n-button @click="handleReset">重置</n-button>
      </n-space>
    </n-card>
    <n-card class="table-card" :bordered="false">
      <template #header><n-space><n-button type="primary" @click="handleAdd">新增产品</n-button></n-space></template>
      <n-data-table :columns="columns" :data="dataList" :loading="loading" :pagination="pagination" :scroll-x="700" size="medium" remote
        @update:page="(p: number) => { pagination.page = p; loadList(); }"
        @update:page-size="(s: number) => { pagination.pageSize = s; pagination.page = 1; loadList(); }" />
    </n-card>
    <ProductForm v-model:show="showForm" :data="currentProduct" @saved="loadList" />
  </div>
</template>

<style lang="less" scoped>
.page-container { display: flex; flex-direction: column; gap: 16px; }
.search-card, .table-card { border-radius: 8px; }
</style>
