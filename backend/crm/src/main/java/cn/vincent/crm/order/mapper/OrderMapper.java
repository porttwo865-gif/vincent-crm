package cn.vincent.crm.order.mapper;

import cn.vincent.crm.order.domain.Order;
import cn.vincent.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单通用 Mapper
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
