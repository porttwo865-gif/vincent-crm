/**
 * 共享类型定义
 * 工具类型、泛型辅助等
 */

/** 可空类型 */
export type Nullable<T> = T | null;

/** 可选类型 */
export type Optional<T> = T | undefined;

/** 深度部分类型 */
export type DeepPartial<T> = {
  [P in keyof T]?: T[P] extends object ? DeepPartial<T[P]> : T[P];
};

/** 键值对 */
export type RecordType<T = unknown> = Record<string, T>;

/** 函数类型 */
export type Fn<T = void> = () => T;

/** 异步函数类型 */
export type AsyncFn<T = void> = () => Promise<T>;

/** 事件处理函数 */
export type EventHandler<T = Event> = (event: T) => void;
