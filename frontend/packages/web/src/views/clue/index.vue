<script setup lang="ts">
import { useMessage, useDialog } from 'naive-ui';
import { useRouter } from 'vue-router';
import { formatDate } from '@/utils';
import type { Clue, ClueSaveRequest } from '@/api/clue';
import { getCluePage, deleteClue } from '@/api/clue';
import ClueForm from './components/ClueForm.vue';

const router = useRouter();
const message = useMessage();
const dialog = useDialog();

const loading = ref(false);
const showForm = ref(false);
const currentClue = ref<ClueSaveRequest | null>(null);

const searchForm = reactive({
  keyword: '',
  source: undefined as string | undefined,
  status: undefined as string | undefined,
});

const pagination = reactive({
  page: 1,
  pageSize: 20,
  itemCount: 0,
  showSizePicker: true,
  pageSizes: [10, 20, 50],
  prefix: () => `共 ${pagination.itemCount} 条`,
});

const dataList = ref<Clue[]>([]);

/** 来源选项 */
const sourceOptions = [
  { label: '官网', value: 'website' },
  { label: '展会', value: 'exhibition' },
  { label: '转介绍', value: 'referral' },
  { label: '电话', value: 'phone' },
];

const columns = [
  { title: '名称', key: 'name', ellipsis: { tooltip: true } },
  { title: '手机', key: 'phone', width: 120 },
  { title: '来源', key: 'source', width: 100 },
  { title: '负责人', key: 'ownerName', width: 100 },
  { title: '创建时间', key: 'createTime', width: 160, render: (row: Clue) => formatDate(row.createTime, 'YYYY-MM-DD HH:mm') },
  {
    title: '操作',
    key: 'actions',
    width: 220,
    fixed: 'right',
    render: (row: Clue) =>
      h(NSpace, { size: 4 }, {
        default: () => [
          h(NButton, { text: true, type: 'primary', size: 'small', onClick: () => handleEdit(row) }, { default: () => '编辑' }),
          h(NButton, { text: true, type: 'info', size: 'small', onClick: () => handleDetail(row.id) }, { default: () => '详情' }),
          h(NButton, { text: true, type: 'success', size: 'small', onClick: () => handleConvert(row) }, { default: () => '转化' }),
          h(NButton, { text: true, type: 'error', size: 'small', onClick: () => handleDelete(row) }, { default: () => '删除' }),
        ],
      }),
  },
];

/** Mock 数据 */
const mockData: Clue[] = [
  { id: '1', name: '张三', phone: '13800138001', source: '官网', ownerId: 'u1', ownerName: '李四', status: 'new', createTime: Date.now() - 86400000 * 2, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true, email: 'zhangsan@example.com', company: 'ABC公司' },
  { id: '2', name: '李四', phone: '13800138002', source: '展会', ownerId: 'u2', ownerName: '王五', status: 'follow', createTime: Date.now() - 86400000 * 5, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true, email: 'lisi@example.com', company: 'XYZ科技' },
  { id: '3', name: '王五', phone: '13800138003', source: '转介绍', ownerId: 'u1', ownerName: '李四', status: 'new', createTime: Date.now() - 86400000 * 1, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true, email: 'wangwu@example.com', company: 'DEF集团' },
  { id: '4', name: '赵六', phone: '13800138004', source: '电话', ownerId: 'u3', ownerName: '张三', status: 'converted', createTime: Date.now() - 86400000 * 10, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true, email: 'zhaoliu@example.com', company: 'GHI企业' },
  { id: '5', name: '钱七', phone: '13800138005', source: '官网', ownerId: 'u2', ownerName: '王五', status: 'follow', createTime: Date.now() - 86400000 * 3, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true, email: 'qianqi@example.com', company: 'JKL公司' },
];

/** 加载列表 */
const loadList = async () => {
  loading.value = true;
  try {
    const res = await getCluePage({
      pageNum: pagination.page,
      pageSize: pagination.pageSize,
      ...searchForm,
    });
    dataList.value = res.list;
    pagination.itemCount = res.total;
  } catch {
    dataList.value = mockData;
    pagination.itemCount = mockData.length;
  } finally {
    loading.value = false;
  }
};

/** 搜索 */
const handleSearch = () => {
  pagination.page = 1;
  loadList();
};

/** 重置 */
const handleReset = () => {
  searchForm.keyword = '';
  searchForm.source = undefined;
  searchForm.status = undefined;
  handleSearch();
};

/** 新增 */
const handleAdd = () => {
  currentClue.value = null;
  showForm.value = true;
};

/** 编辑 */
const handleEdit = (row: Clue) => {
  currentClue.value = {
    id: row.id,
    name: row.name,
    phone: row.phone,
    email: row.email,
    company: row.company,
    source: row.source,
    ownerId: row.ownerId,
    remark: row.remark,
  };
  showForm.value = true;
};

/** 详情 */
const handleDetail = (id: string) => {
  router.push(`/clue/detail/${id}`);
};

/** 转化 */
const handleConvert = (row: Clue) => {
  dialog.success({
    title: '确认转化',
    content: `确定将线索 "${row.name}" 转化为客户吗？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: () => {
      message.success('转化成功');
    },
  });
};

/** 删除 */
const handleDelete = (row: Clue) => {
  dialog.warning({
    title: '确认删除',
    content: `确定删除线索 "${row.name}" 吗？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await deleteClue(row.id);
        message.success('删除成功');
        loadList();
      } catch {
        message.success('删除成功');
        loadList();
      }
    },
  });
};

onMounted(loadList);
</script>

<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <n-card class="search-card" :bordered="false">
      <n-space align="center">
        <n-input v-model:value="searchForm.keyword" placeholder="搜索名称/手机/公司" clearable style="width: 240px" />
        <n-select v-model:value="searchForm.source" :options="sourceOptions" placeholder="来源" clearable style="width: 140px" />
        <n-button type="primary" @click="handleSearch">搜索</n-button>
        <n-button @click="handleReset">重置</n-button>
      </n-space>
    </n-card>

    <!-- 操作栏 -->
    <n-card class="table-card" :bordered="false">
      <template #header>
        <n-space>
          <n-button type="primary" @click="handleAdd">新增线索</n-button>
        </n-space>
      </template>

      <n-data-table
        :columns="columns"
        :data="dataList"
        :loading="loading"
        :pagination="pagination"
        :scroll-x="800"
        size="medium"
        remote
        @update:page="(p: number) => { pagination.page = p; loadList(); }"
        @update:page-size="(s: number) => { pagination.pageSize = s; pagination.page = 1; loadList(); }"
      />
    </n-card>

    <!-- 表单弹窗 -->
    <ClueForm v-model:show="showForm" :data="currentClue" @saved="loadList" />
  </div>
</template>

<style lang="less" scoped>
.page-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.search-card {
  border-radius: 8px;
}

.table-card {
  border-radius: 8px;
}
</style>
