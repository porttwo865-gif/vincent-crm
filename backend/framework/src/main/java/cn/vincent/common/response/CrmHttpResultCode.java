package cn.vincent.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * HTTP 响应结果码枚举
 */
@Getter
@AllArgsConstructor
public enum CrmHttpResultCode {

    /** 成功 */
    SUCCESS(200, "success"),

    /** 请求参数错误 */
    BAD_REQUEST(400, "请求参数错误"),

    /** 未认证 */
    UNAUTHORIZED(401, "未认证"),

    /** 无权限 */
    FORBIDDEN(403, "无权限"),

    /** 资源不存在 */
    NOT_FOUND(404, "资源不存在"),

    /** 参数校验失败 */
    VALIDATE_FAILED(422, "参数校验失败"),

    /** 系统内部错误 */
    INTERNAL_ERROR(500, "系统内部错误");

    /** 响应码 */
    private final int code;

    /** 响应消息 */
    private final String message;
}
