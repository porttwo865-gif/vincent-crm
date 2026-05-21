<script setup lang="ts">
// NTag 供 h() 渲染函数使用，组件自动导入仅覆盖模板，脚本中需显式导入
import { NTag } from 'naive-ui';
import { formatDate } from '@/utils';
import type { LoginLog, Notification } from '@/api/personal';

const message = useMessage();
const activeTab = ref('profile');
const profileForm = reactive({ name: '张三', email: 'zhangsan@vincent.com', phone: '13800138001', avatar: '' });
const passwordForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' });
const passwordRef = ref();

const passwordRules = {
  oldPassword: { required: true, message: '请输入旧密码', trigger: 'blur' },
  newPassword: { required: true, message: '请输入新密码', trigger: 'blur' },
  confirmPassword: { required: true, message: '请确认新密码', trigger: 'blur', validator: (_rule: any, value: string) => value === passwordForm.newPassword ? true : new Error('两次输入的密码不一致') },
};

const loginLogs = ref<LoginLog[]>([
  { id: '1', ip: '192.168.1.1', location: '上海', browser: 'Chrome 120', os: 'macOS', loginTime: Date.now() - 3600000, status: 'success' },
  { id: '2', ip: '192.168.1.2', location: '北京', browser: 'Safari 17', os: 'iOS', loginTime: Date.now() - 86400000, status: 'success' },
  { id: '3', ip: '10.0.0.1', location: '深圳', browser: 'Firefox 121', os: 'Windows', loginTime: Date.now() - 86400000 * 3, status: 'failed' },
]);

const notifications = ref<Notification[]>([
  { id: '1', title: '合同审批通知', content: '您发起的合同审批已通过', type: 'approval', isRead: false, senderName: '系统', createTime: Date.now() - 3600000, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '2', title: '待办提醒', content: '您有3个待跟进任务', type: 'todo', isRead: false, senderName: '系统', createTime: Date.now() - 7200000, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
  { id: '3', title: '系统公告', content: '系统将于本周日凌晨进行维护', type: 'notice', isRead: true, senderName: '管理员', createTime: Date.now() - 86400000, updateTime: Date.now(), createUser: 'sys', updateUser: 'sys', organizationId: 'o1', enable: true },
]);

const handleUpdateProfile = () => { message.success('个人信息更新成功'); };
const handleChangePassword = async () => {
  try { await passwordRef.value?.validate(); message.success('密码修改成功'); passwordForm.oldPassword = ''; passwordForm.newPassword = ''; passwordForm.confirmPassword = ''; }
  catch {}
};
const loginLogColumns = [
  { title: 'IP地址', key: 'ip', width: 140 },
  { title: '地点', key: 'location', width: 100 },
  { title: '浏览器', key: 'browser', width: 140 },
  { title: '操作系统', key: 'os', width: 120 },
  { title: '登录时间', key: 'loginTime', width: 160, render: (row: LoginLog) => formatDate(row.loginTime, 'YYYY-MM-DD HH:mm') },
  { title: '状态', key: 'status', width: 80, render: (row: LoginLog) => h(NTag, { size: 'small', type: row.status === 'success' ? 'success' : 'error' }, { default: () => row.status === 'success' ? '成功' : '失败' }) },
];

const handleMarkRead = (id: string) => { const n = notifications.value.find(i => i.id === id); if (n) n.isRead = true; };
const handleMarkAllRead = () => { notifications.value.forEach(n => n.isRead = true); message.success('全部标记已读'); };
</script>

<template>
  <div class="page-container">
    <n-card :bordered="false">
      <n-tabs v-model:value="activeTab" type="line">
        <n-tab-pane name="profile" tab="个人信息">
          <n-form label-width="80" style="max-width: 480px">
            <n-form-item label="姓名"><n-input v-model:value="profileForm.name" /></n-form-item>
            <n-form-item label="邮箱"><n-input v-model:value="profileForm.email" /></n-form-item>
            <n-form-item label="手机"><n-input v-model:value="profileForm.phone" /></n-form-item>
            <n-form-item><n-button type="primary" @click="handleUpdateProfile">保存</n-button></n-form-item>
          </n-form>
        </n-tab-pane>
        <n-tab-pane name="password" tab="修改密码">
          <n-form ref="passwordRef" :model="passwordForm" :rules="passwordRules" label-width="100" style="max-width: 480px">
            <n-form-item label="旧密码" path="oldPassword"><n-input v-model:value="passwordForm.oldPassword" type="password" show-password-on="click" /></n-form-item>
            <n-form-item label="新密码" path="newPassword"><n-input v-model:value="passwordForm.newPassword" type="password" show-password-on="click" /></n-form-item>
            <n-form-item label="确认密码" path="confirmPassword"><n-input v-model:value="passwordForm.confirmPassword" type="password" show-password-on="click" /></n-form-item>
            <n-form-item><n-button type="primary" @click="handleChangePassword">确认修改</n-button></n-form-item>
          </n-form>
        </n-tab-pane>
        <n-tab-pane name="loginLog" tab="登录日志">
          <n-data-table :columns="loginLogColumns" :data="loginLogs" :bordered="false" size="small" />
        </n-tab-pane>
        <n-tab-pane name="notification" tab="消息通知">
          <template #tab><n-space align="center">消息通知<n-badge v-if="notifications.some(n => !n.isRead)" :value="notifications.filter(n => !n.isRead).length" /></n-space></template>
          <n-space vertical>
            <n-space justify="end"><n-button text type="primary" @click="handleMarkAllRead">全部标记已读</n-button></n-space>
            <n-list hoverable clickable>
              <n-list-item v-for="item in notifications" :key="item.id" @click="handleMarkRead(item.id)">
                <n-thing :title="item.title" :description="item.content">
                  <template #header-extra>
                    <n-tag v-if="!item.isRead" size="small" type="error">未读</n-tag>
                    <n-tag v-else size="small" type="default">已读</n-tag>
                  </template>
                  <template #description>
                    <n-space align="center" size="small">
                      <span style="color: var(--text-n4); font-size: 12px">{{ item.senderName }} · {{ formatDate(item.createTime, 'MM-DD HH:mm') }}</span>
                    </n-space>
                  </template>
                </n-thing>
              </n-list-item>
            </n-list>
          </n-space>
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </div>
</template>

<style lang="less" scoped>
.page-container { display: flex; flex-direction: column; gap: 16px; }
</style>
