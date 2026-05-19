import CDR from './http';
import type { BaseEntity, PagerResult } from '@vincent-crm/shared';

/** 订单明细 */
export interface OrderItem {
  id: string;
  orderId: string;
  productId: string;
  productName?: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
  remark?: string;
}

/** 订单 */
export interface Order extends BaseEntity {
  orderNo: string;
  customerId: string;
  customerName?: string;
  contractId?: string;
  totalAmount: number;
  status: string;
  orderDate: number;
  deliveryDate?: number;
  ownerId: string;
  ownerName?: string;
  remark?: string;
  items: OrderItem[];
}

/** 订单分页请求 */
export interface OrderPageRequest {
  pageNum: number;
  pageSize: number;
  keyword?: string;
  status?: string;
  customerId?: string;
}

/** 订单保存请求 */
export interface OrderSaveRequest {
  id?: string;
  customerId: string;
  contractId?: string;
  orderDate: number;
  deliveryDate?: number;
  ownerId: string;
  remark?: string;
  items: { productId: string; quantity: number; unitPrice: number; remark?: string }[];
}

/** 订单分页列表 */
export function getOrderPage(data: OrderPageRequest) {
  return CDR.post<PagerResult<Order>>('/order/page', data);
}

/** 订单详情 */
export function getOrder(id: string) {
  return CDR.get<Order>(`/order/get/${id}`);
}

/** 新增订单 */
export function addOrder(data: OrderSaveRequest) {
  return CDR.post<Order>('/order/add', data);
}

/** 更新订单 */
export function updateOrder(data: OrderSaveRequest) {
  return CDR.post<Order>('/order/update', data);
}

/** 删除订单 */
export function deleteOrder(id: string) {
  return CDR.get<void>(`/order/delete/${id}`);
}

/** 变更订单状态 */
export function changeOrderStatus(id: string, status: string) {
  return CDR.post<void>('/order/status', { id, status });
}
