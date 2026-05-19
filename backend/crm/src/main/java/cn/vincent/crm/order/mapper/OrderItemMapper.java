package cn.vincent.crm.order.mapper;

import cn.vincent.crm.order.domain.OrderItem;
import cn.vincent.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单明细通用 Mapper
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
