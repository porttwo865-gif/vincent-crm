package cn.vincent.crm.customer.mapper;

import cn.vincent.crm.customer.domain.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtCustomerMapper {

    /**
     * 查询组织下客户分页列表（非公海池）
     *
     * @param orgId        组织 ID
     * @param ownerIds     数据权限范围内的负责人 ID 列表
     * @param inSharedPool 是否在公海池
     * @return 客户列表
     */
    List<Customer> selectCustomerPage(@Param("orgId") String orgId,
                                      @Param("ownerIds") List<String> ownerIds,
                                      @Param("inSharedPool") Boolean inSharedPool);

    /**
     * 批量更新客户为公海池状态
     *
     * @param ids         客户 ID 列表
     * @param reasonId    移入公海原因 ID
     * @param updateUser  更新人
     * @param updateTime  更新时间
     * @return 影响行数
     */
    int batchMoveToPool(@Param("ids") List<String> ids,
                        @Param("reasonId") String reasonId,
                        @Param("updateUser") String updateUser,
                        @Param("updateTime") Long updateTime);
}
