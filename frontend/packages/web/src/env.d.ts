/// <reference types="vite/client" />

/**
 * Vue 组件类型声明
 * 使 TypeScript 正确识别 .vue 文件模块
 */
declare module '*.vue' {
  import type { DefineComponent } from 'vue';
  const component: DefineComponent<object, object, unknown>;
  export default component;
}
