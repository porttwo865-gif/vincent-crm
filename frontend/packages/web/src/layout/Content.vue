<script setup lang="ts">
/**
 * 右侧内容区域组件
 * 嵌套 router-view 渲染页面内容
 */
</script>

<template>
  <n-layout-content class="app-content" :native-scrollbar="false">
    <router-view />
  </n-layout-content>
</template>

<style lang="less" scoped>
.app-content {
  padding: 24px;
  background-color: var(--text-n9);
  min-height: calc(100vh - var(--header-height));
}
</style>
