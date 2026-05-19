package cn.vincent.crm.order.service;

import cn.vincent.common.constants.FormKey;
import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.order.domain.Order;
import cn.vincent.crm.order.domain.OrderItem;
import cn.vincent.crm.order.dto.request.OrderAddRequest;
import cn.vincent.crm.order.dto.request.OrderItemRequest;
import cn.vincent.crm.order.dto.request.OrderPageRequest;
import cn.vincent.crm.order.dto.request.OrderStatusRequest;
import cn.vincent.crm.order.dto.request.OrderUpdateRequest;
import cn.vincent.crm.order.dto.response.OrderGetResponse;
import cn.vincent.crm.order.dto.response.OrderItemResponse;
import cn.vincent.crm.order.dto.response.OrderListResponse;
import cn.vincent.crm.order.mapper.ExtOrderMapper;
import cn.vincent.crm.order.mapper.OrderItemMapper;
import cn.vincent.crm.order.mapper.OrderMapper;
import cn.vincent.crm.system.service.BaseService;
import cn.vincent.crm.system.service.ModuleFieldValueService;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 订单服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private ExtOrderMapper extOrderMapper;

    @Resource
    private BaseService baseService;

    @Resource
    private ModuleFieldValueService moduleFieldValueService;

    /**
     * 生成订单编号
     *
     * @return 订单编号
     */
    private String generateOrderNo() {
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 10000);
        return "ORD-" + dateStr + "-" + randomNum;
    }

    /**
     * 新增订单
     *
     * @param request 新增请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 新增的订单实体
     */
    public Order add(OrderAddRequest request, String userId, String orgId) {
        Order order = new Order();
        order.setId(IDGenerator.nextStr());
        order.setOrderNo(generateOrderNo());
        order.setCustomerId(request.getCustomerId());
        order.setContactId(request.getContactId());
        order.setContractId(request.getContractId());
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus(request.getStatus());
        order.setRemark(request.getRemark());
        order.setOrganizationId(orgId);
        order.setCreateUser(userId);
        order.setUpdateUser(userId);
        order.setCreateTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());
        orderMapper.insert(order);

        // 保存订单明细
        saveOrderItems(order.getId(), request.getItems(), userId);

        // 保存自定义字段值
        moduleFieldValueService.saveFieldValues(
                FormKey.ORDER.getKey(), order.getId(), request.getModuleFields(), userId);

        return order;
    }

    /**
     * 更新订单
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 更新后的订单实体
     */
    public Order update(OrderUpdateRequest request, String userId, String orgId) {
        Order order = orderMapper.selectByPrimaryKey(request.getId());
        if (order == null) {
            throw new GenericException(Translator.get("order.not.exist"));
        }

        if (request.getCustomerId() != null) {
            order.setCustomerId(request.getCustomerId());
        }
        if (request.getContactId() != null) {
            order.setContactId(request.getContactId());
        }
        if (request.getContractId() != null) {
            order.setContractId(request.getContractId());
        }
        if (request.getTotalAmount() != null) {
            order.setTotalAmount(request.getTotalAmount());
        }
        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
        }
        if (request.getRemark() != null) {
            order.setRemark(request.getRemark());
        }
        order.setUpdateUser(userId);
        order.setUpdateTime(System.currentTimeMillis());
        orderMapper.update(order);

        // 更新订单明细：先删除后新增
        if (request.getItems() != null) {
            deleteOrderItemsByOrderId(order.getId());
            saveOrderItems(order.getId(), request.getItems(), userId);
        }

        // 保存自定义字段值
        moduleFieldValueService.saveFieldValues(
                FormKey.ORDER.getKey(), order.getId(), request.getModuleFields(), userId);

        return order;
    }

    /**
     * 删除订单
     *
     * @param id 订单 ID
     */
    public void delete(String id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        if (order == null) {
            throw new GenericException(Translator.get("order.not.exist"));
        }

        // 删除订单明细
        deleteOrderItemsByOrderId(id);

        // 删除自定义字段值
        moduleFieldValueService.deleteFieldValues(FormKey.ORDER.getKey(), id);

        // 删除订单
        orderMapper.deleteByIds(List.of(id));
    }

    /**
     * 订单列表（分页）
     *
     * @param request 分页请求
     * @param orgId   当前组织 ID
     * @return 分页结果
     */
    public PagerWithOption<List<OrderListResponse>> list(OrderPageRequest request, String orgId) {
        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        List<Order> orders = extOrderMapper.selectOrderPage(
                orgId, request.getKeyword(), request.getStatus(), request.getCustomerId());
        PageInfo<Order> pageInfo = new PageInfo<>(orders);

        // 批量查询自定义字段值
        List<String> orderIds = orders.stream().map(Order::getId).toList();
        Map<String, List<ModuleFieldValueDTO>> fieldValuesMap = Collections.emptyMap();
        if (!orderIds.isEmpty()) {
            fieldValuesMap = moduleFieldValueService.batchGetFieldValues(
                    FormKey.ORDER.getKey(), orderIds);
        }

        Map<String, List<ModuleFieldValueDTO>> finalFieldValuesMap = fieldValuesMap;

        List<OrderListResponse> responseList = orders.stream()
                .map(order -> {
                    OrderListResponse response = BeanUtils.copyBean(new OrderListResponse(), order);
                    response.setModuleFields(
                            finalFieldValuesMap.getOrDefault(order.getId(), Collections.emptyList()));
                    return response;
                })
                .toList();

        // 批量设置创建人姓名
        responseList = baseService.setCreateAndUpdateUserName(responseList);

        return PagerWithOption.of(responseList, pageInfo.getTotal(),
                request.getCurrent(), request.getPageSize());
    }

    /**
     * 订单详情（含数据权限校验）
     *
     * @param id     订单 ID
     * @param userId 当前用户 ID
     * @param orgId  当前组织 ID
     * @return 订单详情响应
     */
    public OrderGetResponse getWithDataPermissionCheck(String id, String userId, String orgId) {
        OrderGetResponse response = get(id);
        if (response == null) {
            throw new GenericException(Translator.get("order.not.exist"));
        }
        return response;
    }

    /**
     * 订单详情
     *
     * @param id 订单 ID
     * @return 订单详情响应
     */
    public OrderGetResponse get(String id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        if (order == null) {
            return null;
        }

        OrderGetResponse response = BeanUtils.copyBean(new OrderGetResponse(), order);

        // 查询自定义字段值
        List<ModuleFieldValueDTO> fieldValues = moduleFieldValueService.getFieldValues(
                FormKey.ORDER.getKey(), id);
        response.setModuleFields(fieldValues);

        // 设置创建人/更新人姓名
        baseService.setCreateUpdateOwnerUserName(response);

        // 查询订单明细
        List<OrderItem> items = extOrderMapper.selectItemsByOrderId(id);
        List<OrderItemResponse> itemResponses = items.stream()
                .map(item -> BeanUtils.copyBean(new OrderItemResponse(), item))
                .toList();
        response.setItems(itemResponses);

        return response;
    }

    /**
     * 变更订单状态
     *
     * @param request 状态变更请求
     * @param userId  当前用户 ID
     * @return 更新后的订单实体
     */
    public Order changeStatus(OrderStatusRequest request, String userId) {
        Order order = orderMapper.selectByPrimaryKey(request.getId());
        if (order == null) {
            throw new GenericException(Translator.get("order.not.exist"));
        }
        order.setStatus(request.getStatus());
        order.setUpdateUser(userId);
        order.setUpdateTime(System.currentTimeMillis());
        orderMapper.update(order);
        return order;
    }

    /**
     * 保存订单明细
     *
     * @param orderId 订单 ID
     * @param items   明细列表
     * @param userId  当前用户 ID
     */
    private void saveOrderItems(String orderId, List<OrderItemRequest> items, String userId) {
        if (items == null || items.isEmpty()) {
            return;
        }
        long now = System.currentTimeMillis();
        for (OrderItemRequest itemRequest : items) {
            OrderItem item = new OrderItem();
            item.setId(IDGenerator.nextStr());
            item.setOrderId(orderId);
            item.setProductId(itemRequest.getProductId());
            item.setProductName(itemRequest.getProductName());
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(itemRequest.getUnitPrice());
            item.setAmount(itemRequest.getAmount());
            item.setCreateUser(userId);
            item.setUpdateUser(userId);
            item.setCreateTime(now);
            item.setUpdateTime(now);
            orderItemMapper.insert(item);
        }
    }

    /**
     * 根据订单 ID 删除订单明细
     *
     * @param orderId 订单 ID
     */
    private void deleteOrderItemsByOrderId(String orderId) {
        List<cn.vincent.crm.order.domain.OrderItem> items = extOrderMapper.selectItemsByOrderId(orderId);
        if (!items.isEmpty()) {
            List<String> itemIds = items.stream().map(cn.vincent.crm.order.domain.OrderItem::getId).toList();
            orderItemMapper.deleteByIds(itemIds);
        }
    }
}
