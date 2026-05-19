import axios from 'axios';
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios';

/**
 * Web 端 Axios 封装（CDR）
 * 基于 /api/crm/v1 前缀，配合 Vite 代理转发到后端
 */
const CDR: AxiosInstance = axios.create({
  baseURL: '/api/crm/v1',
  timeout: 30000,
  withCredentials: true,
});

// 请求拦截器 - 预留 token/session 处理
CDR.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 后续添加 token/session 处理逻辑
    return config;
  },
  (error) => Promise.reject(error)
);

// 响应拦截器 - 统一处理业务状态码
CDR.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response;
    // 业务状态码 200 表示成功，直接返回 data 字段
    if (data.code === 200) {
      return data.data;
    }
    return Promise.reject(new Error(data.message || '请求失败'));
  },
  (error) => {
    // 401 未授权，跳转登录
    if (error.response?.status === 401) {
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default CDR;
