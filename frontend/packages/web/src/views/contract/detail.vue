<script setup lang="ts">
import { h } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { formatDate } from '@/utils';
import type { Contract, PaymentPlan, PaymentRecord, Invoice } from '@/api/contract';

const route = useRoute();
const router = useRouter();
const contractId = route.params.id as string;
const activeTab = ref('info');

const contract = ref<Contract>({
  id: contractId, name: '软件服务合同2024', customerId: 'c1', customerName: '华为技术',
  opportunityId: 'o1', amount: 500000, signDate: Date.now() - 86400000 * 30,
  startDate: Date.now() - 86400000 * 30, endDate: Date.now() + 86400000 * 335,
  status: 'executing', ownerId: 'u1', ownerName: '张三', remark: '年度软件服务合同',
  createTime: Date.now() - 86400000 * 30, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true,
});

const paymentPlans = ref<PaymentPlan[]>([
  { id: 'p1', contractId, amount: 150000, plannedDate: Date.now() - 86400000 * 20, actualDate: Date.now() - 86400000 * 18, status: 'paid', remark: '首付款' },
  { id: 'p2', contractId, amount: 175000, plannedDate: Date.now() + 86400000 * 90, status: 'pending', remark: '二期款' },
  { id: 'p3', contractId, amount: 175000, plannedDate: Date.now() + 86400000 * 180, status: 'pending', remark: '尾款' },
]);

const paymentRecords = ref<PaymentRecord[]>([
  { id: 'r1', contractId, amount: 150000, paymentDate: Date.now() - 86400000 * 18, paymentMethod: '银行转账', status: 'confirmed', remark: '首付款' },
]);

const invoices = ref<Invoice[]>([
  { id: 'i1', contractId, invoiceNo: '20240001', amount: 150000, invoiceDate: Date.now() - 86400000 * 19, type: '专票', status: 'issued', remark: '首付款发票' },
]);

const statusMap: Record<string, string> = { draft: '草稿', executing: '执行中', completed: '已完成', terminated: '终止' };

const planColumns = [
  { title: '金额', key: 'amount', render: (row: PaymentPlan) => `¥${row.amount.toLocaleString()}` },
  { title: '计划日期', key: 'plannedDate', render: (row: PaymentPlan) => formatDate(row.plannedDate, 'YYYY-MM-DD') },
  { title: '实际日期', key: 'actualDate', render: (row: PaymentPlan) => row.actualDate ? formatDate(row.actualDate, 'YYYY-MM-DD') : '-' },
  { title: '状态', key: 'status', render: (row: PaymentPlan) => h(NTag, { size: 'small', type: row.status === 'paid' ? 'success' : 'warning' }, { default: () => row.status === 'paid' ? '已回款' : '待回款' }) },
  { title: '备注', key: 'remark' },
];

const recordColumns = [
  { title: '金额', key: 'amount', render: (row: PaymentRecord) => `¥${row.amount.toLocaleString()}` },
  { title: '回款日期', key: 'paymentDate', render: (row: PaymentRecord) => formatDate(row.paymentDate, 'YYYY-MM-DD') },
  { title: '方式', key: 'paymentMethod' },
  { title: '状态', key: 'status', render: (row: PaymentRecord) => h(NTag, { size: 'small', type: 'success' }, { default: () => '已确认' }) },
  { title: '备注', key: 'remark' },
];

const invoiceColumns = [
  { title: '发票号', key: 'invoiceNo' },
  { title: '金额', key: 'amount', render: (row: Invoice) => `¥${row.amount.toLocaleString()}` },
  { title: '开票日期', key: 'invoiceDate', render: (row: Invoice) => formatDate(row.invoiceDate, 'YYYY-MM-DD') },
  { title: '类型', key: 'type' },
  { title: '状态', key: 'status', render: (row: Invoice) => h(NTag, { size: 'small', type: row.status === 'issued' ? 'success' : 'default' }, { default: () => row.status === 'issued' ? '已开票' : '未开票' }) },
];
</script>

<template>
  <div class="page-container">
    <n-card :bordered="false">
      <template #header>
        <n-space align="center">
          <n-button text @click="router.back()"><template #icon><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="20" height="20"><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/></svg></template></n-button>
          <span class="detail-title">合同详情</span>
          <n-tag :type="contract.status === 'completed' ? 'success' : contract.status === 'terminated' ? 'error' : 'info'" size="small">
            {{ statusMap[contract.status] || contract.status }}
          </n-tag>
        </n-space>
      </template>
      <n-tabs v-model:value="activeTab" type="line">
        <n-tab-pane name="info" tab="基本信息">
          <n-descriptions :column="3" bordered label-placement="left">
            <n-descriptions-item label="合同名称">{{ contract.name }}</n-descriptions-item>
            <n-descriptions-item label="客户">{{ contract.customerName || '-' }}</n-descriptions-item>
            <n-descriptions-item label="金额">¥{{ contract.amount.toLocaleString() }}</n-descriptions-item>
            <n-descriptions-item label="签订日期">{{ contract.signDate ? formatDate(contract.signDate, 'YYYY-MM-DD') : '-' }}</n-descriptions-item>
            <n-descriptions-item label="开始日期">{{ contract.startDate ? formatDate(contract.startDate, 'YYYY-MM-DD') : '-' }}</n-descriptions-item>
            <n-descriptions-item label="结束日期">{{ contract.endDate ? formatDate(contract.endDate, 'YYYY-MM-DD') : '-' }}</n-descriptions-item>
            <n-descriptions-item label="负责人">{{ contract.ownerName || '-' }}</n-descriptions-item>
            <n-descriptions-item label="状态">{{ statusMap[contract.status] || contract.status }}</n-descriptions-item>
            <n-descriptions-item label="创建时间">{{ formatDate(contract.createTime, 'YYYY-MM-DD HH:mm') }}</n-descriptions-item>
            <n-descriptions-item label="备注" :span="3">{{ contract.remark || '-' }}</n-descriptions-item>
          </n-descriptions>
        </n-tab-pane>
        <n-tab-pane name="plan" tab="回款计划">
          <n-data-table :columns="planColumns" :data="paymentPlans" :bordered="false" size="small" />
        </n-tab-pane>
        <n-tab-pane name="record" tab="回款记录">
          <n-data-table :columns="recordColumns" :data="paymentRecords" :bordered="false" size="small" />
        </n-tab-pane>
        <n-tab-pane name="invoice" tab="发票">
          <n-data-table :columns="invoiceColumns" :data="invoices" :bordered="false" size="small" />
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </div>
</template>

<style lang="less" scoped>
.detail-title { font-size: 18px; font-weight: 600; }
</style>
