<script setup lang="ts">
import { useMessage, useDialog } from 'naive-ui';
import type { SystemDepartment, DepartmentSaveRequest } from '@/api/system';
import { getDepartmentTree, deleteDepartment } from '@/api/system';

const message = useMessage();
const dialog = useDialog();

const loading = ref(false);
const showForm = ref(false);
const currentDept = ref<DepartmentSaveRequest | null>(null);
const treeData = ref<SystemDepartment[]>([]);

const mockData: SystemDepartment[] = [
  { id: '1', name: '总部', sort: 1, createTime: Date.now() - 86400000 * 365, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true, children: [
    { id: '2', name: '技术部', parentId: '1', sort: 1, leaderId: 'u1', leaderName: '张三', createTime: Date.now() - 86400000 * 300, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
    { id: '3', name: '销售部', parentId: '1', sort: 2, leaderId: 'u2', leaderName: '李四', createTime: Date.now() - 86400000 * 280, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true, children: [
      { id: '4', name: '华东区', parentId: '3', sort: 1, createTime: Date.now() - 86400000 * 200, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
      { id: '5', name: '华南区', parentId: '3', sort: 2, createTime: Date.now() - 86400000 * 180, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
    ]},
    { id: '6', name: '财务部', parentId: '1', sort: 3, leaderId: 'u3', leaderName: '王五', createTime: Date.now() - 86400000 * 250, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  ]},
];

const loadTree = async () => {
  loading.value = true;
  try { treeData.value = await getDepartmentTree(); }
  catch { treeData.value = mockData; }
  finally { loading.value = false; }
};

const renderLabel = (info: { option: SystemDepartment }) => {
  const dept = info.option;
  return h(NSpace, { align: 'center', size: 4 }, { default: () => [
    h('span', dept.name),
    dept.leaderName ? h(NTag, { size: 'small', type: 'info' }, { default: () => dept.leaderName }) : null,
    h(NSpace, { size: 4 }, { default: () => [
      h(NButton, { text: true, type: 'primary', size: 'tiny', onClick: () => handleAddChild(dept) }, { default: () => '新增' }),
      h(NButton, { text: true, type: 'primary', size: 'tiny', onClick: () => handleEdit(dept) }, { default: () => '编辑' }),
      h(NButton, { text: true, type: 'error', size: 'tiny', onClick: () => handleDelete(dept) }, { default: () => '删除' }),
    ]}),
  ]});
};

const handleAdd = () => { currentDept.value = null; showForm.value = true; };
const handleAddChild = (dept: SystemDepartment) => { currentDept.value = { name: '', parentId: dept.id, sort: (dept.children?.length || 0) + 1, leaderId: undefined }; showForm.value = true; };
const handleEdit = (dept: SystemDepartment) => { currentDept.value = { id: dept.id, name: dept.name, parentId: dept.parentId, sort: dept.sort, leaderId: dept.leaderId }; showForm.value = true; };
const handleDelete = (dept: SystemDepartment) => { dialog.warning({ title: '确认删除', content: `确定删除部门 "${dept.name}" 吗？`, positiveText: '确认', negativeText: '取消', onPositiveClick: async () => { await deleteDepartment(dept.id); message.success('删除成功'); loadTree(); } }); };
onMounted(loadTree);
</script>

<template>
  <div class="page-container">
    <n-card class="table-card" :bordered="false">
      <template #header><n-space><n-button type="primary" @click="handleAdd">新增部门</n-button></n-space></template>
      <n-tree :data="treeData" :render-label="renderLabel as any" block-line default-expand-all />
    </n-card>
  </div>
</template>

<style lang="less" scoped>
.page-container { display: flex; flex-direction: column; gap: 16px; }
.table-card { border-radius: 8px; }
</style>
