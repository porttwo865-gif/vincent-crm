import { createRouter, createWebHistory } from 'vue-router';
import type { RouteRecordRaw } from 'vue-router';

/**
 * Mobile 端路由配置
 * 底部 TabBar 对应的主页面
 */
const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/home/index.vue'),
    meta: { title: '首页' },
  },
  {
    path: '/customer',
    name: 'Customer',
    component: () => import('@/views/home/index.vue'),
    meta: { title: '客户' },
  },
  {
    path: '/message',
    name: 'Message',
    component: () => import('@/views/home/index.vue'),
    meta: { title: '消息' },
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/home/index.vue'),
    meta: { title: '我的' },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, _from, next) => {
  const title = to.meta.title as string;
  if (title) {
    document.title = `${title} - VincentCRM`;
  }
  next();
});

export default router;
