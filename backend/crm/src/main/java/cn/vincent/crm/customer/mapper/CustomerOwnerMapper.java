package cn.vincent.crm.customer.mapper;

import cn.vincent.crm.customer.domain.CustomerOwner;
import cn.vincent.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户负责人变更历史通用 Mapper
 */
@Mapper
public interface CustomerOwnerMapper extends BaseMapper<CustomerOwner> {

}
