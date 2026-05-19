package cn.vincent.crm.clue.mapper;

import cn.vincent.crm.clue.domain.Clue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 线索自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtClueMapper {

    /**
     * 根据组织 ID 和筛选条件查询线索列表
     *
     * @param orgId           组织 ID
     * @param inSharedPool    是否在线索池（null=全部）
     * @param ownerIds        可见负责人 ID 列表（数据权限过滤）
     * @param keyword         关键词搜索（名称/联系人/电话）
     * @return 线索列表
     */
    List<Clue> selectByCondition(@Param("orgId") String orgId,
                                 @Param("inSharedPool") Boolean inSharedPool,
                                 @Param("ownerIds") List<String> ownerIds,
                                 @Param("keyword") String keyword);

    /**
     * 批量更新线索池状态
     *
     * @param ids          线索 ID 列表
     * @param inSharedPool 是否在线索池
     * @param owner        负责人 ID（移入池时为 null）
     * @param poolId       线索池 ID
     * @param reasonId     原因 ID
     * @param updateUser   更新人
     * @param updateTime   更新时间
     */
    void batchUpdatePoolStatus(@Param("ids") List<String> ids,
                               @Param("inSharedPool") Boolean inSharedPool,
                               @Param("owner") String owner,
                               @Param("poolId") String poolId,
                               @Param("reasonId") String reasonId,
                               @Param("updateUser") String updateUser,
                               @Param("updateTime") Long updateTime);
}
