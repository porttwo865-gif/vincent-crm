import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';

// Vant 基础样式
import 'vant/lib/index.css';

const app = createApp(App);

// 状态管理
const pinia = createPinia();
app.use(pinia);

// 路由
app.use(router);

app.mount('#app');
