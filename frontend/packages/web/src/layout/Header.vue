<script setup lang="ts">
import { useI18n } from 'vue-i18n';

const { t } = useI18n();

defineProps<{
  /** 侧边栏是否折叠 */
  collapsed: boolean;
}>();

defineEmits<{
  /** 切换侧边栏折叠 */
  (e: 'toggle'): void;
}>();

const router = useRouter();

/** 用户下拉菜单选项 */
const userOptions = [
  { label: '个人中心', key: 'profile' },
  { label: '退出登录', key: 'logout' },
];

/** 处理用户菜单选择 */
const handleUserSelect = (key: string) => {
  if (key === 'profile') {
    router.push('/personal');
  } else if (key === 'logout') {
    window.location.href = '/login';
  }
};
</script>

<template>
  <n-layout-header class="app-header" bordered>
    <div class="header-left">
      <!-- 折叠按钮 -->
      <n-button quaternary @click="$emit('toggle')">
        <template #icon>
          <n-icon size="20">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
              <path v-if="collapsed" d="M3 18h18v-2H3v2zm0-5h18v-2H3v2zm0-7v2h18V6H3z" />
              <path v-else d="M3 18h12v-2H3v2zm0-5h10v-2H3v2zm0-7v2h12V6H3zm14 0v12l6-6z" />
            </svg>
          </n-icon>
        </template>
      </n-button>
      <!-- 面包屑导航占位 -->
    </div>

    <div class="header-right">
      <!-- 用户下拉菜单 -->
      <n-dropdown :options="userOptions" @select="handleUserSelect">
        <n-button quaternary class="user-btn">
          <template #icon>
            <n-icon size="18">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
              </svg>
            </n-icon>
          </template>
          张三
        </n-button>
      </n-dropdown>
    </div>
  </n-layout-header>
</template>

<style lang="less" scoped>
.app-header {
  height: var(--header-height);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  background-color: var(--text-n10);
  border-bottom: 1px solid var(--text-n8);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-btn {
  font-weight: normal;
}
</style>
