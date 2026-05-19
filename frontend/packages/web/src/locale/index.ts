import { createI18n } from 'vue-i18n';
import zhCN from './zh-CN.json';
import enUS from './en-US.json';
// 合并共享国际化
import sharedZhCN from '@vincent-crm/shared/src/locale/zh-CN.json';
import sharedEnUS from '@vincent-crm/shared/src/locale/en-US.json';

/**
 * Web 端国际化初始化
 * 合并共享国际化与 Web 端特有国际化
 */
function setupI18n(locale = 'zh-CN') {
  const i18n = createI18n({
    legacy: false,
    locale,
    fallbackLocale: 'zh-CN',
    messages: {
      'zh-CN': { ...sharedZhCN, ...zhCN },
      'en-US': { ...sharedEnUS, ...enUS },
    },
  });
  return i18n;
}

export { setupI18n };
export default setupI18n;
