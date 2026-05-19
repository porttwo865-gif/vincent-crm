package cn.vincent.crm.opportunity.mapper;

import cn.vincent.crm.opportunity.domain.Opportunity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商机自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtOpportunityMapper {

    /**
     * 根据组织 ID 和筛选条件查询商机列表
     *
     * @param orgId    组织 ID
     * @param ownerIds 可见负责人 ID 列表（数据权限过滤）
     * @param stage    按阶段筛选
     * @return 商机列表
     */
    List<Opportunity> selectByCondition(@Param("orgId") String orgId,
                                        @Param("ownerIds") List<String> ownerIds,
                                        @Param("stage") String stage);
}
