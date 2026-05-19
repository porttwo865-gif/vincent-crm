<script setup lang="ts">
/**
 * Mobile 端首页
 */
</script>

<template>
  <div class="home-page">
    <van-nav-bar title="VincentCRM" />

    <div class="home-content">
      <van-cell-group inset>
        <van-cell title="今日跟进" value="0" />
        <van-cell title="我的客户" value="0" />
        <van-cell title="我的商机" value="0" />
      </van-cell-group>

      <div class="home-empty">
        <van-empty description="移动端功能开发中，敬请期待..." />
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
.home-page {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 50px;
}

.home-content {
  padding: 16px;
}

.home-empty {
  margin-top: 40px;
}
</style>
