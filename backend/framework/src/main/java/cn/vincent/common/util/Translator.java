package cn.vincent.common.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 国际化翻译工具
 */
@Component
public class Translator {

    /** 静态持有 MessageSource 引用，便于静态方法调用 */
    private static MessageSource messageSource;

    /**
     * 构造注入 MessageSource
     *
     * @param messageSource Spring 国际化消息源
     */
    public Translator(MessageSource messageSource) {
        Translator.messageSource = messageSource;
    }

    /**
     * 获取国际化消息
     *
     * @param key 消息键
     * @return 国际化消息文本，找不到时返回 key 本身
     */
    public static String get(String key) {
        try {
            return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return key;
        }
    }

    /**
     * 获取带参数的国际化消息
     *
     * @param key  消息键
     * @param args 消息参数
     * @return 国际化消息文本，找不到时返回 key 本身
     */
    public static String get(String key, Object... args) {
        try {
            return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return key;
        }
    }
}
