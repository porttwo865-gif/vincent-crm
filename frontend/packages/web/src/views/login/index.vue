<script setup lang="ts">
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import { useMessage } from 'naive-ui';
import { getRsaKey, login } from '@/api/auth';
import JSEncrypt from 'jsencrypt';
import { useUserStore } from '@/store/modules/user';

const { t } = useI18n();
const router = useRouter();
const message = useMessage();
const userStore = useUserStore();

/** 后端错误码到可读消息的映射 */
const backendErrorMap: Record<string, string> = {
  'user.password.error': '用户名或密码错误',
  'user.not.exist': '用户不存在',
  'user.disabled': '用户已被禁用',
  'rsa.key.expired': '密钥已过期，请重试',
};

/** 将后端错误码转换为可读消息 */
function getReadableError(errorMsg: string): string {
  return backendErrorMap[errorMsg] || errorMsg || t('login.failed');
}

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
 * 流程：获取 RSA 公钥 → 加密密码 → 调用登录接口
 */
const handleLogin = async () => {
  try {
    await formRef.value?.validate();
    loading.value = true;

    // 1. 获取 RSA 公钥
    const rsaData = await getRsaKey();

    // 2. 用公钥加密密码（后端返回原始 Base64，JSEncrypt 3.x 可直接使用）
    const encrypt = new JSEncrypt();
    encrypt.setPublicKey(rsaData.publicKey);
    const encryptedPassword = encrypt.encrypt(loginForm.password);
    if (!encryptedPassword) {
      message.error('密码加密失败，请重试');
      return;
    }

    // 3. 调用登录接口
    const loginResult = await login({
      username: loginForm.account,
      password: encryptedPassword,
      rsaKey: rsaData.rsaKey,
    });

    // 4. 存储用户信息并跳转
    userStore.setUserInfo({
      id: loginResult.userId,
      name: loginResult.name,
      account: loginResult.username,
      organizationId: loginResult.organizationId,
      permissions: loginResult.permissions,
    });
    message.success(t('login.success'));
    await router.push('/workbench');
  } catch (error: any) {
    // 登录失败提示（将后端 i18n key 转换为可读消息）
    const errorMsg = error?.message || '';
    message.error(getReadableError(errorMsg));
  } finally {
    loading.value = false;
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
