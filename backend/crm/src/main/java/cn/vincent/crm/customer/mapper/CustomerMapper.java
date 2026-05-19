package cn.vincent.crm.customer.mapper;

import cn.vincent.crm.customer.domain.Customer;
import cn.vincent.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户通用 Mapper
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

}
