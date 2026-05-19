package cn.vincent.crm.customer.mapper;

import cn.vincent.crm.customer.domain.CustomerContact;
import cn.vincent.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户联系人通用 Mapper
 */
@Mapper
public interface CustomerContactMapper extends BaseMapper<CustomerContact> {

}
