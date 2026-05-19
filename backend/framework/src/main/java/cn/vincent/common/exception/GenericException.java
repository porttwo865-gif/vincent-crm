package cn.vincent.common.exception;

import cn.vincent.common.response.CrmHttpResultCode;
import lombok.Getter;

/**
 * 通用业务异常
 */
@Getter
public class GenericException extends RuntimeException {

    /** 错误码 */
    private int code = 500;

    /**
     * 带错误消息构造
     *
     * @param message 错误消息
     */
    public GenericException(String message) {
        super(message);
    }

    /**
     * 带结果码构造
     *
     * @param resultCode 结果码枚举
     */
    public GenericException(CrmHttpResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    /**
     * 带结果码和详情构造
     *
     * @param resultCode 结果码枚举
     * @param detail     详情信息
     */
    public GenericException(CrmHttpResultCode resultCode, String detail) {
        super(resultCode.getMessage() + ": " + detail);
        this.code = resultCode.getCode();
    }

    /**
     * 带原因异常构造
     *
     * @param cause 原始异常
     */
    public GenericException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
