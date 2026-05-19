import axios from 'axios';
import type { AxiosInstance, AxiosRequestConfig, InternalAxiosRequestConfig, AxiosResponse } from 'axios';

/**
 * 基础 Axios 实例配置
 * 作为 web/mobile 端 HTTP 客户端的基础，各端可在此基础上扩展
 */
const baseURL = '/api/crm/v1';

function createBaseAxios(config?: AxiosRequestConfig): AxiosInstance {
  const instance = axios.create({
    baseURL,
    timeout: 30000,
    withCredentials: true,
    ...config,
  });

  // 请求拦截器 - 预留 token/session 处理
  instance.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
      // 后续添加 token/session 处理逻辑
      return config;
    },
    (error) => Promise.reject(error)
  );

  // 响应拦截器 - 统一处理响应格式
  instance.interceptors.response.use(
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

  return instance;
}

export { createBaseAxios, baseURL };
export default createBaseAxios;
