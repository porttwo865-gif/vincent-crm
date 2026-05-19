<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import { useMessage } from 'naive-ui';
import { formatDate } from '@/utils';
import type { Clue, FollowRecord } from '@/api/clue';
import { getClue, getClueFollowRecords } from '@/api/clue';

const route = useRoute();
const router = useRouter();
const message = useMessage();

const clueId = route.params.id as string;
const clue = ref<Clue | null>(null);
const followList = ref<FollowRecord[]>([]);
const loading = ref(false);
const followLoading = ref(false);
const activeTab = ref('info');

const followTypeMap: Record<string, string> = {
  phone: '电话',
  email: '邮件',
  visit: '拜访',
  wechat: '微信',
  other: '其他',
};

const loadClue = async () => {
  loading.value = true;
  try {
    clue.value = await getClue(clueId);
  } catch {
    clue.value = {
      id: clueId, name: '张三', phone: '13800138001', email: 'zhangsan@example.com',
      company: 'ABC公司', source: '官网', ownerId: 'u1', ownerName: '李四',
      status: 'new', createTime: Date.now() - 86400000 * 2, updateTime: Date.now(),
      createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true, remark: '重点跟进客户',
    };
  } finally {
    loading.value = false;
  }
};

const loadFollow = async () => {
  followLoading.value = true;
  try {
    followList.value = await getClueFollowRecords(clueId);
  } catch {
    followList.value = [
      { id: 'f1', clueId, content: '首次电话联系，客户有兴趣了解产品', followType: 'phone', followTime: Date.now() - 86400000, followUserName: '李四', nextFollowTime: Date.now() + 86400000 },
      { id: 'f2', clueId, content: '发送产品资料到客户邮箱', followType: 'email', followTime: Date.now() - 43200000, followUserName: '李四' },
    ];
  } finally {
    followLoading.value = false;
  }
};

onMounted(() => {
  loadClue();
  loadFollow();
});
</script>

<template>
  <div class="page-container">
    <n-card :bordered="false">
      <template #header>
        <n-space align="center">
          <n-button text @click="router.back()">
            <template #icon>
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
                <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z" />
              </svg>
            </template>
          </n-button>
          <span class="detail-title">线索详情</span>
        </n-space>
      </template>

      <n-tabs v-model:value="activeTab" type="line">
        <n-tab-pane name="info" tab="基本信息">
          <n-descriptions v-if="clue" :column="3" bordered label-placement="left">
            <n-descriptions-item label="名称">{{ clue.name }}</n-descriptions-item>
            <n-descriptions-item label="手机">{{ clue.phone }}</n-descriptions-item>
            <n-descriptions-item label="邮箱">{{ clue.email || '-' }}</n-descriptions-item>
            <n-descriptions-item label="公司">{{ clue.company || '-' }}</n-descriptions-item>
            <n-descriptions-item label="来源">{{ clue.source }}</n-descriptions-item>
            <n-descriptions-item label="负责人">{{ clue.ownerName || '-' }}</n-descriptions-item>
            <n-descriptions-item label="状态">{{ clue.status }}</n-descriptions-item>
            <n-descriptions-item label="创建时间">{{ formatDate(clue.createTime, 'YYYY-MM-DD HH:mm') }}</n-descriptions-item>
            <n-descriptions-item label="备注">{{ clue.remark || '-' }}</n-descriptions-item>
          </n-descriptions>
        </n-tab-pane>

        <n-tab-pane name="follow" tab="跟进记录">
          <n-timeline>
            <n-timeline-item v-for="item in followList" :key="item.id" type="info">
              <n-card size="small" :bordered="false" class="follow-card">
                <div class="follow-header">
                  <n-tag size="small" :type="item.followType === 'phone' ? 'success' : 'info'">
                    {{ followTypeMap[item.followType] || item.followType }}
                  </n-tag>
                  <span class="follow-user">{{ item.followUserName }}</span>
                  <span class="follow-time">{{ formatDate(item.followTime, 'YYYY-MM-DD HH:mm') }}</span>
                </div>
                <div class="follow-content">{{ item.content }}</div>
                <div v-if="item.nextFollowTime" class="follow-next">
                  下次跟进：{{ formatDate(item.nextFollowTime, 'YYYY-MM-DD HH:mm') }}
                </div>
              </n-card>
            </n-timeline-item>
          </n-timeline>
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </div>
</template>

<style lang="less" scoped>
.detail-title {
  font-size: 18px;
  font-weight: 600;
}

.follow-card {
  background: var(--text-n9);
  margin-bottom: 8px;
}

.follow-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.follow-user {
  font-weight: 600;
  color: var(--text-n1);
}

.follow-time {
  color: var(--text-n4);
  font-size: 12px;
  margin-left: auto;
}

.follow-content {
  color: var(--text-n1);
  line-height: 1.6;
}

.follow-next {
  color: var(--warning);
  font-size: 12px;
  margin-top: 8px;
}
</style>
