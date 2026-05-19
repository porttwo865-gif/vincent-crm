<script setup lang="ts">
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';

const { t } = useI18n();
const router = useRouter();

defineProps<{
  /** 侧边栏是否折叠 */
  collapsed: boolean;
}>();

/** 菜单项 */
const menuOptions = computed(() => [
  {
    label: t('menu.workbench'),
    key: '/workbench',
    icon: () =>
      h('svg', { xmlns: 'http://www.w3.org/2000/svg', viewBox: '0 0 24 24', fill: 'currentColor', width: '1em', height: '1em' }, [
        h('path', { d: 'M3 13h8V3H3v10zm0 8h8v-6H3v6zm10 0h8V11h-8v10zm0-18v6h8V3h-8z' }),
      ]),
  },
  {
    label: t('menu.customer'),
    key: '/customer',
    icon: () =>
      h('svg', { xmlns: 'http://www.w3.org/2000/svg', viewBox: '0 0 24 24', fill: 'currentColor', width: '1em', height: '1em' }, [
        h('path', { d: 'M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z' }),
      ]),
  },
  {
    label: t('menu.clue'),
    key: '/clue',
    icon: () =>
      h('svg', { xmlns: 'http://www.w3.org/2000/svg', viewBox: '0 0 24 24', fill: 'currentColor', width: '1em', height: '1em' }, [
        h('path', { d: 'M9.5 2c-1.82 0-3.53.5-5 1.35l2.99 2.99A6.47 6.47 0 009.5 6C13.09 6 16 8.91 16 12.5c0 .73-.13 1.43-.35 2.08l2.99 2.99C19.5 16.11 20 14.39 20 12.5 20 6.81 15.19 2 9.5 2z' }),
      ]),
  },
  {
    label: t('menu.opportunity'),
    key: '/opportunity',
    icon: () =>
      h('svg', { xmlns: 'http://www.w3.org/2000/svg', viewBox: '0 0 24 24', fill: 'currentColor', width: '1em', height: '1em' }, [
        h('path', { d: 'M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z' }),
      ]),
  },
  {
    label: t('menu.contract'),
    key: '/contract',
    icon: () =>
      h('svg', { xmlns: 'http://www.w3.org/2000/svg', viewBox: '0 0 24 24', fill: 'currentColor', width: '1em', height: '1em' }, [
        h('path', { d: 'M14 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V8l-6-6zm-1 7V3.5L18.5 9H13z' }),
      ]),
  },
  {
    label: t('menu.product'),
    key: '/product',
    icon: () =>
      h('svg', { xmlns: 'http://www.w3.org/2000/svg', viewBox: '0 0 24 24', fill: 'currentColor', width: '1em', height: '1em' }, [
        h('path', { d: 'M20 4H4c-1.11 0-1.99.89-1.99 2L2 18c0 1.11.89 2 2 2h16c1.11 0 2-.89 2-2V6c0-1.11-.89-2-2-2zm0 14H4v-6h16v6z' }),
      ]),
  },
  {
    label: t('menu.system'),
    key: '/system',
    icon: () =>
      h('svg', { xmlns: 'http://www.w3.org/2000/svg', viewBox: '0 0 24 24', fill: 'currentColor', width: '1em', height: '1em' }, [
        h('path', { d: 'M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58a.49.49 0 00.12-.61l-1.92-3.32a.49.49 0 00-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54a.484.484 0 00-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96a.49.49 0 00-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.07.62-.07.94s.02.64.07.94l-2.03 1.58a.49.49 0 00-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6A3.6 3.6 0 1112 8.4a3.6 3.6 0 010 7.2z' }),
      ]),
  },
]);

/** 当前激活的菜单 */
const activeKey = ref('/workbench');

/** 菜单点击 - 路由跳转 */
const handleMenuClick = (key: string) => {
  activeKey.value = key;
  router.push(key);
};

/** 监听路由变化，同步激活菜单 */
watch(
  () => router.currentRoute.value.path,
  (path) => {
    activeKey.value = path;
  },
  { immediate: true }
);
</script>

<template>
  <n-layout-sider
    class="app-sider"
    :collapsed="collapsed"
    :collapsed-width="56"
    :width="180"
    collapse-mode="width"
    show-trigger="bar"
    bordered
    :native-scrollbar="false"
  >
    <!-- 品牌标识 -->
    <div class="sider-logo">
      <span v-if="!collapsed" class="logo-text">VincentCRM</span>
      <span v-else class="logo-text-mini">V</span>
    </div>

    <!-- 导航菜单 -->
    <n-menu
      v-model:value="activeKey"
      :collapsed="collapsed"
      :collapsed-width="56"
      :collapsed-icon-size="20"
      :options="menuOptions"
      @update:value="handleMenuClick"
    />
  </n-layout-sider>
</template>

<style lang="less" scoped>
.app-sider {
  background-color: var(--text-n10);
}

.sider-logo {
  height: var(--header-height);
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid var(--text-n8);
  overflow: hidden;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: var(--primary-0);
  white-space: nowrap;
}

.logo-text-mini {
  font-size: 22px;
  font-weight: 700;
  color: var(--primary-0);
}
</style>
