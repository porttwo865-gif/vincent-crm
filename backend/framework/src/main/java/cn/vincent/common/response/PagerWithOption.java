package cn.vincent.common.response;

import lombok.Data;

import java.util.List;

/**
 * 通用分页响应包装（含选项列表）
 *
 * @param <T> 列表数据类型
 */
@Data
public class PagerWithOption<T> {

    /** 列表数据 */
    private T list;

    /** 总记录数 */
    private long total;

    /** 当前页码 */
    private int current;

    /** 每页条数 */
    private int pageSize;

    /**
     * 构建分页响应对象
     *
     * @param list     列表数据
     * @param total    总记录数
     * @param current  当前页码
     * @param pageSize 每页条数
     * @param <T>      列表元素类型
     * @return 分页响应对象
     */
    public static <T> PagerWithOption<List<T>> of(List<T> list, long total, int current, int pageSize) {
        PagerWithOption<List<T>> pager = new PagerWithOption<>();
        pager.setList(list);
        pager.setTotal(total);
        pager.setCurrent(current);
        pager.setPageSize(pageSize);
        return pager;
    }
}
