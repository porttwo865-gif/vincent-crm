import CDR from './http';
import type { BaseEntity, PagerResult } from '@vincent-crm/shared';

/** 产品 */
export interface Product extends BaseEntity {
  name: string;
  code: string;
  category?: string;
  price: number;
  unit?: string;
  description?: string;
  spec?: string;
}

/** 产品分页请求 */
export interface ProductPageRequest {
  pageNum: number;
  pageSize: number;
  keyword?: string;
  category?: string;
}

/** 产品保存请求 */
export interface ProductSaveRequest {
  id?: string;
  name: string;
  code: string;
  category?: string;
  price: number;
  unit?: string;
  description?: string;
  spec?: string;
}

/** 产品分页列表 */
export function getProductPage(data: ProductPageRequest) {
  return CDR.post<PagerResult<Product>>('/product/page', data);
}

/** 产品详情 */
export function getProduct(id: string) {
  return CDR.get<Product>(`/product/get/${id}`);
}

/** 新增产品 */
export function addProduct(data: ProductSaveRequest) {
  return CDR.post<Product>('/product/add', data);
}

/** 更新产品 */
export function updateProduct(data: ProductSaveRequest) {
  return CDR.post<Product>('/product/update', data);
}

/** 删除产品 */
export function deleteProduct(id: string) {
  return CDR.get<void>(`/product/delete/${id}`);
}

/** 启用/禁用产品 */
export function toggleProductEnable(id: string, enable: boolean) {
  return CDR.post<void>('/product/enable', { id, enable });
}

/** 产品分类列表 */
export function getProductCategories() {
  return CDR.get<string[]>('/product/categories');
}
