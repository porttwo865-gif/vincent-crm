package cn.vincent.crm.customer.constants;

/**
 * 客户模块常量定义
 */
public class CustomerConstants {

    private CustomerConstants() {
        // 常量类禁止实例化
    }

    /** 操作类型：转移 */
    public static final String OPERATION_TYPE_TRANSFER = "TRANSFER";

    /** 操作类型：领取 */
    public static final String OPERATION_TYPE_CLAIM = "CLAIM";

    /** 操作类型：分配 */
    public static final String OPERATION_TYPE_ASSIGN = "ASSIGN";

    /** 操作类型：移入公海 */
    public static final String OPERATION_TYPE_MOVE_POOL = "MOVE_POOL";
}
