package cn.vincent.common.response;

import lombok.Data;

/**
 * 统一响应包装
 *
 * @param <T> 响应数据类型
 */
@Data
public class ResultHolder<T> {

    /** 响应码 */
    private int code;

    /** 响应消息 */
    private String message;

    /** 响应数据 */
    private T data;

    /**
     * 成功响应
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 统一响应对象
     */
    public static <T> ResultHolder<T> success(T data) {
        ResultHolder<T> holder = new ResultHolder<>();
        holder.setCode(200);
        holder.setMessage("success");
        holder.setData(data);
        return holder;
    }

    /**
     * 错误响应（无数据）
     *
     * @param code    错误码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 统一响应对象
     */
    public static <T> ResultHolder<T> error(int code, String message) {
        ResultHolder<T> holder = new ResultHolder<>();
        holder.setCode(code);
        holder.setMessage(message);
        return holder;
    }

    /**
     * 错误响应（带数据）
     *
     * @param code    错误码
     * @param message 错误消息
     * @param data    附加数据
     * @param <T>     数据类型
     * @return 统一响应对象
     */
    public static <T> ResultHolder<T> error(int code, String message, T data) {
        ResultHolder<T> holder = new ResultHolder<>();
        holder.setCode(code);
        holder.setMessage(message);
        holder.setData(data);
        return holder;
    }
}
