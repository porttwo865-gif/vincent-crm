package cn.vincent.crm.customer.mapper;

import cn.vincent.crm.customer.domain.CustomerContact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户联系人自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtCustomerContactMapper {

    /**
     * 根据客户 ID 查询联系人列表
     *
     * @param customerId 客户 ID
     * @return 联系人列表
     */
    List<CustomerContact> selectByCustomerId(@Param("customerId") String customerId);
}
