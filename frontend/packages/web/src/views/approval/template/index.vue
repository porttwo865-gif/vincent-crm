<script setup lang="ts">
import { useMessage, useDialog } from 'naive-ui';
import type { ApprovalTemplate } from '@/api/approval';
import { getApprovalTemplatePage, deleteApprovalTemplate } from '@/api/approval';

const message = useMessage();
const dialog = useDialog();

const loading = ref(false);
const showForm = ref(false);
const currentTemplate = ref<ApprovalTemplate | null>(null);

const searchForm = reactive({ keyword: '', module: undefined as string | undefined });
const pagination = reactive({ page: 1, pageSize: 20, itemCount: 0, showSizePicker: true, pageSizes: [10, 20, 50], prefix: () => `共 ${pagination.itemCount} 条` });
const dataList = ref<ApprovalTemplate[]>([]);

const moduleOptions = [{ label: '合同', value: 'contract' }, { label: '订单', value: 'order' }, { label: '回款', value: 'payment' }];

const columns = [
  { title: '模板名称', key: 'name', ellipsis: { tooltip: true } },
  { title: '适用模块', key: 'module', width: 120 },
  { title: '节点数', key: 'nodes', width: 80, render: (row: ApprovalTemplate) => row.nodes?.length || 0 },
  { title: '默认', key: 'isDefault', width: 80, render: (row: ApprovalTemplate) => h(NTag, { size: 'small', type: row.isDefault ? 'success' : 'default' }, { default: () => row.isDefault ? '是' : '否' }) },
  { title: '操作', key: 'actions', width: 160, fixed: 'right', render: (row: ApprovalTemplate) => h(NSpace, { size: 4 }, { default: () => [
    h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => handleEdit(row) }, { default: () => '编辑' }),
    h(NButton, { text: true, type: 'error', size: 'small', onClick: () => handleDelete(row) }, { default: () => '删除' }),
  ]}) },
];

const mockData: ApprovalTemplate[] = [
  { id: '1', name: '合同审批流程', module: 'contract', description: '标准合同审批', nodes: [{ id: 'n1', name: '部门经理审批', type: 'serial', approvers: ['u1'], approverNames: ['张三'], sort: 1 }, { id: 'n2', name: '财务审批', type: 'serial', approvers: ['u2'], approverNames: ['李四'], sort: 2 }], isDefault: true, createTime: Date.now() - 86400000 * 30, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '2', name: '订单审批流程', module: 'order', description: '订单快速审批', nodes: [{ id: 'n1', name: '销售总监审批', type: 'serial', approvers: ['u3'], approverNames: ['王五'], sort: 1 }], isDefault: false, createTime: Date.now() - 86400000 * 15, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
];

const loadList = async () => {
  loading.value = true;
  try { const res = await getApprovalTemplatePage({ pageNum: pagination.page, pageSize: pagination.pageSize, ...searchForm }); dataList.value = res.list; pagination.itemCount = res.total; }
  catch { dataList.value = mockData; pagination.itemCount = mockData.length; }
  finally { loading.value = false; }
};
const handleSearch = () => { pagination.page = 1; loadList(); };
const handleReset = () => { searchForm.keyword = ''; searchForm.module = undefined; handleSearch(); };
const handleAdd = () => { currentTemplate.value = null; showForm.value = true; };
const handleEdit = (row: ApprovalTemplate) => { currentTemplate.value = row; showForm.value = true; };
const handleDelete = (row: ApprovalTemplate) => { dialog.warning({ title: '确认删除', content: `确定删除模板 "${row.name}" 吗？`, positiveText: '确认', negativeText: '取消', onPositiveClick: async () => { await deleteApprovalTemplate(row.id); message.success('删除成功'); loadList(); } }); };
onMounted(loadList);
</script>

<template>
  <div class="page-container">
    <n-card class="search-card" :bordered="false">
      <n-space align="center">
        <n-input v-model:value="searchForm.keyword" placeholder="搜索模板名称" clearable style="width: 240px" />
        <n-select v-model:value="searchForm.module" :options="moduleOptions" placeholder="模块" clearable style="width: 140px" />
        <n-button type="primary" @click="handleSearch">搜索</n-button>
        <n-button @click="handleReset">重置</n-button>
      </n-space>
    </n-card>
    <n-card class="table-card" :bordered="false">
      <template #header><n-space><n-button type="primary" @click="handleAdd">新增模板</n-button></n-space></template>
      <n-data-table :columns="columns" :data="dataList" :loading="loading" :pagination="pagination" :scroll-x="600" size="medium" remote
        @update:page="(p: number) => { pagination.page = p; loadList(); }"
        @update:page-size="(s: number) => { pagination.pageSize = s; pagination.page = 1; loadList(); }" />
    </n-card>
  </div>
</template>

<style lang="less" scoped>
.page-container { display: flex; flex-direction: column; gap: 16px; }
.search-card, .table-card { border-radius: 8px; }
</style>
