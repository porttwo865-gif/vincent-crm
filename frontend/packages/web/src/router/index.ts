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
      { path: 'workbench', name: 'Workbench', component: () => import('@/views/workbench/index.vue'), meta: { title: '工作台', requiresAuth: true } },
      { path: 'clue', name: 'Clue', component: () => import('@/views/clue/index.vue'), meta: { title: '线索管理', requiresAuth: true } },
      { path: 'clue/detail/:id', name: 'ClueDetail', component: () => import('@/views/clue/detail.vue'), meta: { title: '线索详情', requiresAuth: true } },
      { path: 'customer', name: 'Customer', component: () => import('@/views/customer/index.vue'), meta: { title: '客户管理', requiresAuth: true } },
      { path: 'customer/detail/:id', name: 'CustomerDetail', component: () => import('@/views/customer/detail.vue'), meta: { title: '客户详情', requiresAuth: true } },
      { path: 'opportunity', name: 'Opportunity', component: () => import('@/views/opportunity/index.vue'), meta: { title: '商机管理', requiresAuth: true } },
      { path: 'contract', name: 'Contract', component: () => import('@/views/contract/index.vue'), meta: { title: '合同管理', requiresAuth: true } },
      { path: 'contract/detail/:id', name: 'ContractDetail', component: () => import('@/views/contract/detail.vue'), meta: { title: '合同详情', requiresAuth: true } },
      { path: 'product', name: 'Product', component: () => import('@/views/product/index.vue'), meta: { title: '产品管理', requiresAuth: true } },
      { path: 'order', name: 'Order', component: () => import('@/views/order/index.vue'), meta: { title: '订单管理', requiresAuth: true } },
      { path: 'invoice', name: 'Invoice', component: () => import('@/views/invoice/index.vue'), meta: { title: '发票管理', requiresAuth: true } },
      { path: 'approval/template', name: 'ApprovalTemplate', component: () => import('@/views/approval/template/index.vue'), meta: { title: '审批模板', requiresAuth: true } },
      { path: 'approval/instance', name: 'ApprovalInstance', component: () => import('@/views/approval/instance/index.vue'), meta: { title: '审批实例', requiresAuth: true } },
      { path: 'system/user', name: 'SystemUser', component: () => import('@/views/system/user/index.vue'), meta: { title: '用户管理', requiresAuth: true } },
      { path: 'system/role', name: 'SystemRole', component: () => import('@/views/system/role/index.vue'), meta: { title: '角色管理', requiresAuth: true } },
      { path: 'system/department', name: 'SystemDepartment', component: () => import('@/views/system/department/index.vue'), meta: { title: '部门管理', requiresAuth: true } },
      { path: 'personal', name: 'Personal', component: () => import('@/views/personal/index.vue'), meta: { title: '个人中心', requiresAuth: true } },
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
