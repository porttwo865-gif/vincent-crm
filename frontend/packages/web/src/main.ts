import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import { setupI18n } from './locale';

// 全局样式
import '@/assets/styles/reset.less';
import '@/assets/styles/global.less';

const app = createApp(App);

// 状态管理
const pinia = createPinia();
app.use(pinia);

// 路由
app.use(router);

// 国际化
const i18n = setupI18n();
app.use(i18n);

app.mount('#app');
