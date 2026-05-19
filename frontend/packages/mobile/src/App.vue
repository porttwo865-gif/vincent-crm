<script setup lang="ts">
/**
 * Mobile 端根组件
 * 底部 TabBar 布局
 */

/** TabBar 激活项 */
const activeTab = ref(0);

/** TabBar 配置 */
const tabbars = [
  { name: '首页', icon: 'home-o' },
  { name: '客户', icon: 'friends-o' },
  { name: '消息', icon: 'chat-o' },
  { name: '我的', icon: 'contact' },
];
</script>

<template>
  <div class="mobile-app">
    <router-view />
    <van-tabbar v-model="activeTab" route>
      <van-tabbar-item to="/" icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item to="/customer" icon="friends-o">客户</van-tabbar-item>
      <van-tabbar-item to="/message" icon="chat-o">消息</van-tabbar-item>
      <van-tabbar-item to="/profile" icon="contact">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<style lang="less">
.mobile-app {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
  font-family: 'Helvetica Neue', Arial, 'PingFang SC', 'Source Han Serif', sans-serif;
}
</style>
