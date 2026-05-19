<script setup lang="ts">
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';

const { t } = useI18n();
const router = useRouter();

/** 登录表单数据 */
const loginForm = reactive({
  account: '',
  password: '',
});

/** 登录加载状态 */
const loading = ref(false);

/** 表单引用 */
const formRef = ref();

/** 表单校验规则 */
const rules = computed(() => ({
  account: {
    required: true,
    message: t('login.accountRequired'),
    trigger: 'blur',
  },
  password: {
    required: true,
    message: t('login.passwordRequired'),
    trigger: 'blur',
  },
}));

/**
 * 提交登录
 * 后续对接后端 API，当前 Mock 跳转
 */
const handleLogin = async () => {
  try {
    await formRef.value?.validate();
    loading.value = true;
    // Mock 登录 - 后续替换为真实 API 调用
    setTimeout(() => {
      loading.value = false;
      router.push('/workbench');
    }, 800);
  } catch {
    // 表单校验失败
  }
};
</script>

<template>
  <div class="login-page">
    <!-- 左侧品牌区 -->
    <div class="login-brand">
      <div class="brand-content">
        <h1 class="brand-title">VincentCRM</h1>
        <p class="brand-subtitle">智能客户关系管理系统</p>
      </div>
    </div>

    <!-- 右侧表单区 -->
    <div class="login-form-wrapper">
      <div class="login-form-container">
        <h2 class="form-title">{{ t('login.title') }}</h2>
        <p class="form-desc">欢迎回来，请登录您的账号</p>

        <n-form ref="formRef" :model="loginForm" :rules="rules" size="large">
          <n-form-item path="account" :label="t('login.account')">
            <n-input
              v-model:value="loginForm.account"
              :placeholder="t('login.accountPlaceholder')"
              @keyup.enter="handleLogin"
            />
          </n-form-item>

          <n-form-item path="password" :label="t('login.password')">
            <n-input
              v-model:value="loginForm.password"
              type="password"
              show-password-on="click"
              :placeholder="t('login.passwordPlaceholder')"
              @keyup.enter="handleLogin"
            />
          </n-form-item>

          <n-button
            type="primary"
            block
            :loading="loading"
            class="login-btn"
            @click="handleLogin"
          >
            {{ t('login.submit') }}
          </n-button>
        </n-form>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
.login-page {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.login-brand {
  width: 60%;
  background-color: var(--primary-0);
  display: flex;
  align-items: center;
  justify-content: center;
}

.brand-content {
  text-align: center;
  color: white;
}

.brand-title {
  font-size: 48px;
  font-weight: 700;
  letter-spacing: 2px;
  margin-bottom: 16px;
}

.brand-subtitle {
  font-size: 18px;
  opacity: 0.85;
  font-weight: 300;
}

.login-form-wrapper {
  width: 40%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: white;
}

.login-form-container {
  width: 360px;
  padding: 0 24px;
}

.form-title {
  font-size: 28px;
  font-weight: 600;
  color: var(--text-n1);
  margin-bottom: 8px;
}

.form-desc {
  font-size: 14px;
  color: var(--text-n4);
  margin-bottom: 32px;
}

.login-btn {
  height: 44px;
  font-size: 16px;
  margin-top: 8px;
}
</style>
