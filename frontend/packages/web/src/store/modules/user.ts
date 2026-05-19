import { defineStore } from 'pinia';
import type { UserInfo } from '@vincent-crm/shared';

/**
 * 用户状态管理
 * 管理登录态、用户信息、权限等
 */
export const useUserStore = defineStore('user', () => {
  /** 用户信息 */
  const userInfo = ref<UserInfo | null>(null);

  /** 是否已登录 */
  const isLoggedIn = computed(() => !!userInfo.value);

  /** 用户名 */
  const userName = computed(() => userInfo.value?.name ?? '');

  /** 用户权限列表 */
  const permissions = computed(() => userInfo.value?.permissions ?? []);

  /** 组织 ID */
  const organizationId = computed(() => userInfo.value?.organizationId ?? '');

  /**
   * 设置用户信息
   */
  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info;
  };

  /**
   * 清除用户信息（退出登录）
   */
  const clearUserInfo = () => {
    userInfo.value = null;
  };

  /**
   * 检查是否拥有指定权限
   */
  const hasPermission = (permission: string): boolean => {
    return permissions.value.includes(permission);
  };

  return {
    userInfo,
    isLoggedIn,
    userName,
    permissions,
    organizationId,
    setUserInfo,
    clearUserInfo,
    hasPermission,
  };
});
