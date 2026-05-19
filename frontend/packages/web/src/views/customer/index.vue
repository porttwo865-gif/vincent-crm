<script setup lang="ts">
import { useMessage, useDialog } from 'naive-ui';
import { useRouter } from 'vue-router';
import { formatDate } from '@/utils';
import type { Customer, CustomerSaveRequest } from '@/api/customer';
import { getCustomerPage, deleteCustomer } from '@/api/customer';
import CustomerForm from './components/CustomerForm.vue';

const router = useRouter();
const message = useMessage();
const dialog = useDialog();

const loading = ref(false);
const showForm = ref(false);
const currentCustomer = ref<CustomerSaveRequest | null>(null);

const searchForm = reactive({ keyword: '', industry: undefined as string | undefined, level: undefined as string | undefined });
const pagination = reactive({ page: 1, pageSize: 20, itemCount: 0, showSizePicker: true, pageSizes: [10, 20, 50], prefix: () => `共 ${pagination.itemCount} 条` });
const dataList = ref<Customer[]>([]);

const industryOptions = [{ label: '互联网', value: 'internet' }, { label: '金融', value: 'finance' }, { label: '制造', value: 'manufacturing' }, { label: '教育', value: 'education' }];
const levelOptions = [{ label: 'A级', value: 'A' }, { label: 'B级', value: 'B' }, { label: 'C级', value: 'C' }];

const columns = [
  { title: '名称', key: 'name', ellipsis: { tooltip: true } },
  { title: '公司', key: 'company', ellipsis: { tooltip: true } },
  { title: '行业', key: 'industry', width: 100 },
  { title: '等级', key: 'level', width: 80 },
  { title: '负责人', key: 'ownerName', width: 100 },
  { title: '创建时间', key: 'createTime', width: 160, render: (row: Customer) => formatDate(row.createTime, 'YYYY-MM-DD HH:mm') },
  { title: '操作', key: 'actions', width: 180, fixed: 'right', render: (row: Customer) => h(NSpace, { size: 4 }, { default: () => [
    h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => handleEdit(row) }, { default: () => '编辑' }),
    h(NButton, { text: true, type: 'info', size: 'small', onClick: () => router.push(`/customer/detail/${row.id}`) }, { default: () => '详情' }),
    h(NButton, { text: true, type: 'error', size: 'small', onClick: () => handleDelete(row) }, { default: () => '删除' }),
  ]}) },
];

const mockData: Customer[] = [
  { id: '1', name: '华为技术有限公司', phone: '0755-12345678', email: 'contact@huawei.com', company: '华为技术', industry: '通信', source: '官网', ownerId: 'u1', ownerName: '张三', level: 'A', address: '深圳', createTime: Date.now() - 86400000 * 3, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '2', name: '阿里巴巴', phone: '0571-12345678', email: 'contact@alibaba.com', company: '阿里巴巴', industry: '互联网', source: '展会', ownerId: 'u2', ownerName: '李四', level: 'A', address: '杭州', createTime: Date.now() - 86400000 * 5, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '3', name: '腾讯科技', phone: '0755-87654321', email: 'contact@tencent.com', company: '腾讯', industry: '互联网', source: '转介绍', ownerId: 'u1', ownerName: '张三', level: 'B', address: '深圳', createTime: Date.now() - 86400000 * 1, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
];

const loadList = async () => {
  loading.value = true;
  try {
    const res = await getCustomerPage({ pageNum: pagination.page, pageSize: pagination.pageSize, ...searchForm });
    dataList.value = res.list; pagination.itemCount = res.total;
  } catch { dataList.value = mockData; pagination.itemCount = mockData.length; }
  finally { loading.value = false; }
};

const handleSearch = () => { pagination.page = 1; loadList(); };
const handleReset = () => { searchForm.keyword = ''; searchForm.industry = undefined; searchForm.level = undefined; handleSearch(); };
const handleAdd = () => { currentCustomer.value = null; showForm.value = true; };
const handleEdit = (row: Customer) => {
  currentCustomer.value = { id: row.id, name: row.name, phone: row.phone || '', email: row.email, company: row.company, industry: row.industry, source: row.source, ownerId: row.ownerId, level: row.level, address: row.address, remark: row.remark };
  showForm.value = true;
};
const handleDelete = (row: Customer) => {
  dialog.warning({ title: '确认删除', content: `确定删除客户 "${row.name}" 吗？`, positiveText: '确认', negativeText: '取消',
    onPositiveClick: async () => { await deleteCustomer(row.id); message.success('删除成功'); loadList(); } });
};

onMounted(loadList);
</script>

<template>
  <div class="page-container">
    <n-card class="search-card" :bordered="false">
      <n-space align="center">
        <n-input v-model:value="searchForm.keyword" placeholder="搜索名称/公司" clearable style="width: 240px" />
        <n-select v-model:value="searchForm.industry" :options="industryOptions" placeholder="行业" clearable style="width: 140px" />
        <n-select v-model:value="searchForm.level" :options="levelOptions" placeholder="等级" clearable style="width: 120px" />
        <n-button type="primary" @click="handleSearch">搜索</n-button>
        <n-button @click="handleReset">重置</n-button>
      </n-space>
    </n-card>
    <n-card class="table-card" :bordered="false">
      <template #header><n-space><n-button type="primary" @click="handleAdd">新增客户</n-button></n-space></template>
      <n-data-table :columns="columns" :data="dataList" :loading="loading" :pagination="pagination" :scroll-x="800" size="medium" remote
        @update:page="(p: number) => { pagination.page = p; loadList(); }"
        @update:page-size="(s: number) => { pagination.pageSize = s; pagination.page = 1; loadList(); }" />
    </n-card>
    <CustomerForm v-model:show="showForm" :data="currentCustomer" @saved="loadList" />
  </div>
</template>

<style lang="less" scoped>
.page-container { display: flex; flex-direction: column; gap: 16px; }
.search-card, .table-card { border-radius: 8px; }
</style>
