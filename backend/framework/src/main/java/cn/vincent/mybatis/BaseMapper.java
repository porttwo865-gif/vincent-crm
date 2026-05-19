package cn.vincent.mybatis;

import java.util.List;

/**
 * 通用 Mapper 基类 - 提供标准 CRUD 操作
 *
 * @param <T> 实体类型
 */
public interface BaseMapper<T> {

    /**
     * 根据主键查询
     *
     * @param id 主键 ID
     * @return 实体对象
     */
    T selectByPrimaryKey(String id);

    /**
     * 根据主键批量查询
     *
     * @param ids 主键 ID 列表
     * @return 实体列表
     */
    List<T> selectByIds(List<String> ids);

    /**
     * 新增记录
     *
     * @param record 实体对象
     * @return 影响行数
     */
    int insert(T record);

    /**
     * 批量新增
     *
     * @param records 实体列表
     * @return 影响行数
     */
    int batchInsert(List<T> records);

    /**
     * 更新记录
     *
     * @param record 实体对象
     * @return 影响行数
     */
    int update(T record);

    /**
     * 根据主键批量删除
     *
     * @param ids 主键 ID 列表
     * @return 影响行数
     */
    int deleteByIds(List<String> ids);
}
