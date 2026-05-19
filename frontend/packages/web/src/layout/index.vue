<script setup lang="ts">
import Header from './Header.vue';
import Sider from './Sider.vue';
import Content from './Content.vue';

/** 侧边栏折叠状态 */
const collapsed = ref(false);

/** 切换侧边栏折叠 */
const toggleCollapsed = () => {
  collapsed.value = !collapsed.value;
};
</script>

<template>
  <n-layout class="app-layout" has-sider>
    <Sider :collapsed="collapsed" />
    <n-layout>
      <Header :collapsed="collapsed" @toggle="toggleCollapsed" />
      <Content />
    </n-layout>
  </n-layout>
</template>

<style lang="less" scoped>
.app-layout {
  height: 100vh;
  overflow: hidden;
}
</style>
