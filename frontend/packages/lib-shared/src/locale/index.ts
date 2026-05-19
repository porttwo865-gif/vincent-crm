import { createI18n } from 'vue-i18n';
import zhCN from './zh-CN.json';
import enUS from './en-US.json';

/**
 * 共享国际化实例
 * Web 和 Mobile 端均可使用，也可在此基础上扩展
 */
function setupI18n(locale = 'zh-CN') {
  const i18n = createI18n({
    legacy: false,
    locale,
    fallbackLocale: 'zh-CN',
    messages: {
      'zh-CN': zhCN,
      'en-US': enUS,
    },
  });
  return i18n;
}

export { setupI18n };
export default setupI18n;
