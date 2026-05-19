/**
 * Web 端通用工具函数
 */

/**
 * 防抖函数
 * @param fn 目标函数
 * @param delay 延迟时间（毫秒）
 */
export function debounce<T extends (...args: unknown[]) => unknown>(
  fn: T,
  delay = 300
): (...args: Parameters<T>) => void {
  let timer: ReturnType<typeof setTimeout> | null = null;
  return (...args: Parameters<T>) => {
    if (timer) clearTimeout(timer);
    timer = setTimeout(() => fn(...args), delay);
  };
}

/**
 * 节流函数
 * @param fn 目标函数
 * @param interval 间隔时间（毫秒）
 */
export function throttle<T extends (...args: unknown[]) => unknown>(
  fn: T,
  interval = 300
): (...args: Parameters<T>) => void {
  let lastTime = 0;
  return (...args: Parameters<T>) => {
    const now = Date.now();
    if (now - lastTime >= interval) {
      lastTime = now;
      fn(...args);
    }
  };
}

/**
 * 格式化时间戳为日期字符串
 * @param timestamp 时间戳（毫秒）
 * @param format 格式化模板
 */
export function formatDate(
  timestamp: number,
  format = 'YYYY-MM-DD HH:mm:ss'
): string {
  const date = new Date(timestamp);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  return format
    .replace('YYYY', String(year))
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds);
}

/**
 * 深拷贝（结构化克隆）
 * @param obj 源对象
 */
export function deepClone<T>(obj: T): T {
  return structuredClone(obj);
}
