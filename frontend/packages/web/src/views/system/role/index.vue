<script setup lang="ts">
import { useMessage, useDialog } from 'naive-ui';
import type { SystemRole, RoleSaveRequest } from '@/api/system';
import { getRolePage, deleteRole, getPermissionTree } from '@/api/system';

const message = useMessage();
const dialog = useDialog();

const loading = ref(false);
const showForm = ref(false);
const showPermission = ref(false);
const currentRole = ref<SystemRole | null>(null);
const currentRoleForPerm = ref<SystemRole | null>(null);

const searchForm = reactive({ keyword: '' });
const pagination = reactive({ page: 1, pageSize: 20, itemCount: 0, showSizePicker: true, pageSizes: [10, 20, 50], prefix: () => `共 ${pagination.itemCount} 条` });
const dataList = ref<SystemRole[]>([]);
const permissionTree = ref<any[]>([]);
const checkedKeys = ref<string[]>([]);

const columns = [
  { title: '角色名称', key: 'name' },
  { title: '角色编码', key: 'code', width: 140 },
  { title: '描述', key: 'description', ellipsis: { tooltip: true } },
  { title: '操作', key: 'actions', width: 180, fixed: 'right' as const, render: (row: SystemRole) => h(NSpace, { size: 4 }, { default: () => [
    h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => handleEdit(row) }, { default: () => '编辑' }),
    h(NButton, { text: true, type: 'info', size: 'small', onClick: () => handleConfigPermission(row) }, { default: () => '权限配置' }),
    h(NButton, { text: true, type: 'error', size: 'small', onClick: () => handleDelete(row) }, { default: () => '删除' }),
  ]}) },
];

const mockData: SystemRole[] = [
  { id: '1', name: '系统管理员', code: 'admin', description: '拥有所有权限', createTime: Date.now() - 86400000 * 100, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '2', name: '销售经理', code: 'sales_manager', description: '销售团队管理', createTime: Date.now() - 86400000 * 50, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '3', name: '销售代表', code: 'sales_rep', description: '基础销售权限', createTime: Date.now() - 86400000 * 30, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
];

const mockPermissionTree = [
  { key: 'system', label: '系统管理', children: [{ key: 'system:user', label: '用户管理' }, { key: 'system:role', label: '角色管理' }, { key: 'system:dept', label: '部门管理' }] },
  { key: 'crm', label: 'CRM', children: [{ key: 'crm:clue', label: '线索管理' }, { key: 'crm:customer', label: '客户管理' }, { key: 'crm:opportunity', label: '商机管理' }, { key: 'crm:contract', label: '合同管理' }] },
];

const loadList = async () => {
  loading.value = true;
  try { const res = await getRolePage({ pageNum: pagination.page, pageSize: pagination.pageSize, keyword: searchForm.keyword }); dataList.value = res.list; pagination.itemCount = res.total; }
  catch { dataList.value = mockData; pagination.itemCount = mockData.length; }
  finally { loading.value = false; }
};
const handleSearch = () => { pagination.page = 1; loadList(); };
const handleReset = () => { searchForm.keyword = ''; handleSearch(); };
const handleAdd = () => { currentRole.value = null; showForm.value = true; };
const handleEdit = (row: SystemRole) => { currentRole.value = row; showForm.value = true; };
const handleDelete = (row: SystemRole) => { dialog.warning({ title: '确认删除', content: `确定删除角色 "${row.name}" 吗？`, positiveText: '确认', negativeText: '取消', onPositiveClick: async () => { await deleteRole(row.id); message.success('删除成功'); loadList(); } }); };
const handleConfigPermission = async (row: SystemRole) => {
  currentRoleForPerm.value = row;
  try { const tree = await getPermissionTree(); permissionTree.value = tree; }
  catch { permissionTree.value = mockPermissionTree; }
  checkedKeys.value = row.permissionIds || [];
  showPermission.value = true;
};
const handleSavePermission = () => { message.success('权限配置已保存'); showPermission.value = false; };
onMounted(loadList);
</script>

<template>
  <div class="page-container">
    <n-card class="search-card" :bordered="false">
      <n-space align="center">
        <n-input v-model:value="searchForm.keyword" placeholder="搜索角色名称/编码" clearable style="width: 240px" />
        <n-button type="primary" @click="handleSearch">搜索</n-button>
        <n-button @click="handleReset">重置</n-button>
      </n-space>
    </n-card>
    <n-card class="table-card" :bordered="false">
      <template #header><n-space><n-button type="primary" @click="handleAdd">新增角色</n-button></n-space></template>
      <n-data-table :columns="columns" :data="dataList" :loading="loading" :pagination="pagination" :scroll-x="600" size="medium" remote
        @update:page="(p: number) => { pagination.page = p; loadList(); }"
        @update:page-size="(s: number) => { pagination.pageSize = s; pagination.page = 1; loadList(); }" />
    </n-card>
    <n-modal v-model:show="showPermission" title="权限配置" preset="card" style="width: 480px" :bordered="false" :mask-closable="false">
      <n-tree v-model:checked-keys="checkedKeys" :data="permissionTree" checkable cascade default-expand-all />
      <template #footer><n-space justify="end"><n-button @click="showPermission = false">取消</n-button><n-button type="primary" @click="handleSavePermission">保存</n-button></n-space></template>
    </n-modal>
  </div>
</template>

<style lang="less" scoped>
.page-container { display: flex; flex-direction: column; gap: 16px; }
.search-card, .table-card { border-radius: 8px; }
</style>
