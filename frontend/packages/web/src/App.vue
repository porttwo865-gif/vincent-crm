<script setup lang="ts">
import type { GlobalThemeOverrides } from 'naive-ui';

/**
 * Naive UI 主题覆盖配置
 * 扁平化设计：无阴影，使用品牌色系
 */
const themeOverrides: GlobalThemeOverrides = {
  common: {
    primaryColor: '#1E88E5',
    primaryColorHover: '#1565C0',
    primaryColorPressed: '#0B3D91',
    primaryColorSuppl: '#1E88E5',
    borderRadius: '4px',
    borderRadiusSmall: '2px',
    boxShadow1: 'none',
    boxShadow2: 'none',
    boxShadow3: 'none',
    fontFamily: "'Helvetica Neue', Arial, 'PingFang SC', 'Source Han Serif', sans-serif",
  },
  Button: {
    borderRadiusMedium: '2px',
    borderRadiusSmall: '2px',
    borderRadiusLarge: '2px',
  },
  Card: {
    borderRadius: '4px',
    boxShadow: 'none',
  },
  Input: {
    borderRadius: '2px',
  },
  DataTable: {
    borderRadius: '4px',
  },
};
</script>

<template>
  <n-config-provider :theme-overrides="themeOverrides">
    <n-message-provider>
      <n-dialog-provider>
        <n-notification-provider>
          <n-loading-bar-provider>
            <router-view />
          </n-loading-bar-provider>
        </n-notification-provider>
      </n-dialog-provider>
    </n-message-provider>
  </n-config-provider>
</template>

<style lang="less">
/* App 根样式 */
</style>
