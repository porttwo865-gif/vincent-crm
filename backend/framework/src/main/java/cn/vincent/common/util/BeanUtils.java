package cn.vincent.common.util;

/**
 * 对象拷贝工具
 */
public class BeanUtils {

    private BeanUtils() {
        // 工具类禁止实例化
    }

    /**
     * 将 source 的属性拷贝到 target 并返回 target
     *
     * @param target 目标对象
     * @param source 源对象
     * @param <T>    目标类型
     * @return 拷贝后的目标对象
     */
    public static <T> T copyBean(T target, Object source) {
        if (source == null || target == null) {
            return target;
        }
        org.springframework.beans.BeanUtils.copyProperties(source, target);
        return target;
    }
}
