import { createRouter, createWebHistory } from 'vue-router';
import type { RouteRecordRaw } from 'vue-router';

/**
 * 路由配置
 * /login 无布局，/ 下使用主布局
 */
const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: {
      title: '登录',
      requiresAuth: false,
    },
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/index.vue'),
    redirect: '/workbench',
    meta: {
      requiresAuth: true,
    },
    children: [
      {
        path: 'workbench',
        name: 'Workbench',
        component: () => import('@/views/workbench/index.vue'),
        meta: {
          title: '工作台',
          requiresAuth: true,
        },
      },
      // 后续模块路由占位
      // {
      //   path: 'customer',
      //   name: 'Customer',
      //   component: () => import('@/views/customer/index.vue'),
      //   meta: { title: '客户管理', requiresAuth: true },
      // },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

/**
 * 路由守卫 - 未登录重定向到登录页
 */
router.beforeEach((to, _from, next) => {
  // 设置页面标题
  const title = to.meta.title as string;
  if (title) {
    document.title = `${title} - VincentCRM`;
  }

  // 登录页直接放行
  if (to.path === '/login') {
    next();
    return;
  }

  // 后续对接登录态判断
  // const userStore = useUserStore();
  // if (!userStore.isLoggedIn) {
  //   next({ path: '/login', query: { redirect: to.fullPath } });
  //   return;
  // }

  next();
});

export default router;
