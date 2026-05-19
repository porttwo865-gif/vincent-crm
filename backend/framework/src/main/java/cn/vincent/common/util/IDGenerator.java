package cn.vincent.common.util;

import java.util.UUID;

/**
 * ID 生成器 - 生成 32 位唯一标识
 */
public class IDGenerator {

    private IDGenerator() {
        // 工具类禁止实例化
    }

    /**
     * 生成 32 位无连字符的唯一标识
     *
     * @return 32 位字符串 ID
     */
    public static String nextStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
