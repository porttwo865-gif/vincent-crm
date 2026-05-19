<script setup lang="ts">
import { useI18n } from 'vue-i18n';

const { t } = useI18n();
</script>

<template>
  <div class="workbench-page">
    <div class="page-header">
      <h2 class="page-title">{{ t('workbench.title') }}</h2>
    </div>

    <div class="workbench-content">
      <!-- 快捷统计卡片 -->
      <n-grid :cols="4" :x-gap="16" :y-gap="16">
        <n-gi>
          <n-card class="stat-card">
            <div class="stat-label">{{ t('workbench.todayFollow') }}</div>
            <div class="stat-value">0</div>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card class="stat-card">
            <div class="stat-label">{{ t('workbench.myCustomer') }}</div>
            <div class="stat-value">0</div>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card class="stat-card">
            <div class="stat-label">{{ t('workbench.myOpportunity') }}</div>
            <div class="stat-value">0</div>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card class="stat-card">
            <div class="stat-label">{{ t('workbench.recentActivity') }}</div>
            <div class="stat-value">0</div>
          </n-card>
        </n-gi>
      </n-grid>

      <!-- 占位提示 -->
      <n-card class="placeholder-card">
        <n-empty description="工作台功能开发中，敬请期待..." />
      </n-card>
    </div>
  </div>
</template>

<style lang="less" scoped>
.workbench-page {
  padding: 0;
}

.page-header {
  margin-bottom: 24px;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-n1);
}

.stat-card {
  text-align: center;
}

.stat-label {
  font-size: 13px;
  color: var(--text-n4);
  margin-bottom: 8px;
}

.stat-value {
  font-size: 32px;
  font-weight: 600;
  color: var(--primary-8);
}

.placeholder-card {
  margin-top: 24px;
}
</style>
