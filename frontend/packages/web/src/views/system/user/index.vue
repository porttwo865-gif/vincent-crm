<script setup lang="ts">
import { useMessage, useDialog } from 'naive-ui';
import { formatDate } from '@/utils';
import type { SystemUser, UserSaveRequest } from '@/api/system';
import { getUserPage, deleteUser, resetUserPassword } from '@/api/system';

const message = useMessage();
const dialog = useDialog();

const loading = ref(false);
const showForm = ref(false);
const currentUser = ref<UserSaveRequest | null>(null);

const searchForm = reactive({ keyword: '', deptId: undefined as string | undefined });
const pagination = reactive({ page: 1, pageSize: 20, itemCount: 0, showSizePicker: true, pageSizes: [10, 20, 50], prefix: () => `共 ${pagination.itemCount} 条` });
const dataList = ref<SystemUser[]>([]);

const columns = [
  { title: '账号', key: 'account', width: 120 },
  { title: '姓名', key: 'name', width: 100 },
  { title: '邮箱', key: 'email', ellipsis: { tooltip: true } },
  { title: '手机', key: 'phone', width: 120 },
  { title: '部门', key: 'deptName', width: 120 },
  { title: '角色', key: 'roleNames', width: 160, render: (row: SystemUser) => row.roleNames?.join(', ') || '-' },
  { title: '状态', key: 'enable', width: 80, render: (row: SystemUser) => h(NTag, { size: 'small', type: row.enable ? 'success' : 'error' }, { default: () => row.enable ? '启用' : '禁用' }) },
  { title: '操作', key: 'actions', width: 220, fixed: 'right', render: (row: SystemUser) => h(NSpace, { size: 4 }, { default: () => [
    h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => handleEdit(row) }, { default: () => '编辑' }),
    h(NButton, { text: true, type: 'warning', size: 'small', onClick: () => handleResetPassword(row) }, { default: () => '重置密码' }),
    h(NButton, { text: true, type: 'error', size: 'small', onClick: () => handleDelete(row) }, { default: () => '删除' }),
  ]}) },
];

const mockData: SystemUser[] = [
  { id: '1', account: 'admin', name: '管理员', email: 'admin@vincent.com', phone: '13800138000', deptId: 'd1', deptName: '技术部', roleIds: ['r1'], roleNames: ['系统管理员'], gender: 'male', createTime: Date.now() - 86400000 * 100, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '2', account: 'zhangsan', name: '张三', email: 'zhangsan@vincent.com', phone: '13800138001', deptId: 'd2', deptName: '销售部', roleIds: ['r2'], roleNames: ['销售经理'], gender: 'male', lastLoginTime: Date.now() - 3600000, createTime: Date.now() - 86400000 * 50, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '3', account: 'lisi', name: '李四', email: 'lisi@vincent.com', phone: '13800138002', deptId: 'd2', deptName: '销售部', roleIds: ['r3'], roleNames: ['销售代表'], gender: 'female', lastLoginTime: Date.now() - 7200000, createTime: Date.now() - 86400000 * 30, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
];

const loadList = async () => {
  loading.value = true;
  try { const res = await getUserPage({ pageNum: pagination.page, pageSize: pagination.pageSize, ...searchForm }); dataList.value = res.list; pagination.itemCount = res.total; }
  catch { dataList.value = mockData; pagination.itemCount = mockData.length; }
  finally { loading.value = false; }
};
const handleSearch = () => { pagination.page = 1; loadList(); };
const handleReset = () => { searchForm.keyword = ''; searchForm.deptId = undefined; handleSearch(); };
const handleAdd = () => { currentUser.value = null; showForm.value = true; };
const handleEdit = (row: SystemUser) => { currentUser.value = { id: row.id, account: row.account, name: row.name, email: row.email, phone: row.phone, deptId: row.deptId, roleIds: row.roleIds, gender: row.gender }; showForm.value = true; };
const handleDelete = (row: SystemUser) => { dialog.warning({ title: '确认删除', content: `确定删除用户 "${row.name}" 吗？`, positiveText: '确认', negativeText: '取消', onPositiveClick: async () => { await deleteUser(row.id); message.success('删除成功'); loadList(); } }); };
const handleResetPassword = (row: SystemUser) => { dialog.warning({ title: '重置密码', content: `确定重置用户 "${row.name}" 的密码吗？`, positiveText: '确认', negativeText: '取消', onPositiveClick: async () => { const pwd = await resetUserPassword(row.id); message.success(`密码已重置为: ${pwd}`); } }); };
onMounted(loadList);
</script>

<template>
  <div class="page-container">
    <n-card class="search-card" :bordered="false">
      <n-space align="center">
        <n-input v-model:value="searchForm.keyword" placeholder="搜索账号/姓名" clearable style="width: 240px" />
        <n-button type="primary" @click="handleSearch">搜索</n-button>
        <n-button @click="handleReset">重置</n-button>
      </n-space>
    </n-card>
    <n-card class="table-card" :bordered="false">
      <template #header><n-space><n-button type="primary" @click="handleAdd">新增用户</n-button></n-space></template>
      <n-data-table :columns="columns" :data="dataList" :loading="loading" :pagination="pagination" :scroll-x="1000" size="medium" remote
        @update:page="(p: number) => { pagination.page = p; loadList(); }"
        @update:page-size="(s: number) => { pagination.pageSize = s; pagination.page = 1; loadList(); }" />
    </n-card>
  </div>
</template>

<style lang="less" scoped>
.page-container { display: flex; flex-direction: column; gap: 16px; }
.search-card, .table-card { border-radius: 8px; }
</style>
