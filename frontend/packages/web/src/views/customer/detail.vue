<script setup lang="ts">
import { h } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { formatDate } from '@/utils';
import type { Customer, CustomerContact } from '@/api/customer';
import type { FollowRecord } from '@/api/clue';

const route = useRoute();
const router = useRouter();
const customerId = route.params.id as string;
const activeTab = ref('info');

const customer = ref<Customer>({
  id: customerId, name: '华为技术有限公司', phone: '0755-12345678', email: 'contact@huawei.com',
  company: '华为技术', industry: '通信', source: '官网', ownerId: 'u1', ownerName: '张三',
  level: 'A', address: '深圳市龙岗区坂田华为基地', createTime: Date.now() - 86400000 * 30,
  updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true,
  remark: '全球领先的ICT基础设施和智能终端提供商',
});

const contacts = ref<CustomerContact[]>([
  { id: 'c1', customerId, name: '任正非', phone: '13800138001', email: 'ceo@huawei.com', position: 'CEO', isPrimary: true, createTime: Date.now(), updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: 'c2', customerId, name: '孟晚舟', phone: '13800138002', email: 'cfo@huawei.com', position: 'CFO', isPrimary: false, createTime: Date.now(), updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
]);

const followList = ref<FollowRecord[]>([
  { id: 'f1', customerId, content: '季度业务回顾会议', followType: 'visit', followTime: Date.now() - 86400000 * 7, followUserName: '张三' },
  { id: 'f2', customerId, content: '确认下一季度采购计划', followType: 'phone', followTime: Date.now() - 86400000 * 3, followUserName: '张三' },
]);

const followTypeMap: Record<string, string> = { phone: '电话', email: '邮件', visit: '拜访', wechat: '微信', other: '其他' };

const contactColumns = [
  { title: '姓名', key: 'name' },
  { title: '手机', key: 'phone' },
  { title: '邮箱', key: 'email' },
  { title: '职位', key: 'position' },
  { title: '首要', key: 'isPrimary', render: (row: CustomerContact) => row.isPrimary ? h(NTag, { type: 'success', size: 'small' }, { default: () => '是' }) : h(NTag, { size: 'small' }, { default: () => '否' }) },
];
</script>

<template>
  <div class="page-container">
    <n-card :bordered="false">
      <template #header>
        <n-space align="center">
          <n-button text @click="router.back()"><template #icon><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="20" height="20"><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/></svg></template></n-button>
          <span class="detail-title">客户详情</span>
        </n-space>
      </template>
      <n-tabs v-model:value="activeTab" type="line">
        <n-tab-pane name="info" tab="基本信息">
          <n-descriptions :column="3" bordered label-placement="left">
            <n-descriptions-item label="名称">{{ customer.name }}</n-descriptions-item>
            <n-descriptions-item label="手机">{{ customer.phone || '-' }}</n-descriptions-item>
            <n-descriptions-item label="邮箱">{{ customer.email || '-' }}</n-descriptions-item>
            <n-descriptions-item label="公司">{{ customer.company || '-' }}</n-descriptions-item>
            <n-descriptions-item label="行业">{{ customer.industry || '-' }}</n-descriptions-item>
            <n-descriptions-item label="等级">{{ customer.level }}</n-descriptions-item>
            <n-descriptions-item label="来源">{{ customer.source }}</n-descriptions-item>
            <n-descriptions-item label="负责人">{{ customer.ownerName || '-' }}</n-descriptions-item>
            <n-descriptions-item label="地址">{{ customer.address || '-' }}</n-descriptions-item>
            <n-descriptions-item label="创建时间">{{ formatDate(customer.createTime, 'YYYY-MM-DD HH:mm') }}</n-descriptions-item>
            <n-descriptions-item label="备注" :span="3">{{ customer.remark || '-' }}</n-descriptions-item>
          </n-descriptions>
        </n-tab-pane>
        <n-tab-pane name="contact" tab="联系人">
          <n-data-table :columns="contactColumns" :data="contacts" :bordered="false" size="small" />
        </n-tab-pane>
        <n-tab-pane name="follow" tab="跟进记录">
          <n-timeline>
            <n-timeline-item v-for="item in followList" :key="item.id" type="info">
              <n-card size="small" :bordered="false" class="follow-card">
                <div class="follow-header">
                  <n-tag size="small">{{ followTypeMap[item.followType] || item.followType }}</n-tag>
                  <span class="follow-user">{{ item.followUserName }}</span>
                  <span class="follow-time">{{ formatDate(item.followTime, 'YYYY-MM-DD HH:mm') }}</span>
                </div>
                <div class="follow-content">{{ item.content }}</div>
              </n-card>
            </n-timeline-item>
          </n-timeline>
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </div>
</template>

<style lang="less" scoped>
.detail-title { font-size: 18px; font-weight: 600; }
.follow-card { background: var(--text-n9); margin-bottom: 8px; }
.follow-header { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
.follow-user { font-weight: 600; }
.follow-time { color: var(--text-n4); font-size: 12px; margin-left: auto; }
.follow-content { line-height: 1.6; }
</style>
