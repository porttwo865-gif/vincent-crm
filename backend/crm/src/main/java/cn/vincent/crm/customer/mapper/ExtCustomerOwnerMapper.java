package cn.vincent.crm.customer.mapper;

import cn.vincent.crm.customer.domain.CustomerOwner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户负责人变更历史自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtCustomerOwnerMapper {

    /**
     * 根据客户 ID 查询负责人变更历史
     *
     * @param customerId 客户 ID
     * @return 变更历史列表
     */
    List<CustomerOwner> selectByCustomerId(@Param("customerId") String customerId);
}
