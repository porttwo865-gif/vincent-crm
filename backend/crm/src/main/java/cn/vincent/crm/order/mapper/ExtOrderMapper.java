package cn.vincent.crm.order.mapper;

import cn.vincent.crm.order.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单自定义 Mapper
 */
@Mapper
public interface ExtOrderMapper {

    /**
     * 订单分页列表查询
     *
     * @param organizationId 组织 ID
     * @param keyword        搜索关键词
     * @param status         状态
     * @param customerId     关联客户 ID
     * @return 订单列表
     */
    List<Order> selectOrderPage(@Param("organizationId") String organizationId,
                                @Param("keyword") String keyword,
                                @Param("status") String status,
                                @Param("customerId") String customerId);

    /**
     * 根据订单 ID 查询明细列表
     *
     * @param orderId 订单 ID
     * @return 订单明细列表
     */
    List<cn.vincent.crm.order.domain.OrderItem> selectItemsByOrderId(@Param("orderId") String orderId);
}
