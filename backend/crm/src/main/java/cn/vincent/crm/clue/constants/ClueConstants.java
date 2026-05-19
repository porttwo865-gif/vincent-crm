package cn.vincent.crm.clue.constants;

/**
 * 线索模块常量定义
 */
public class ClueConstants {

    private ClueConstants() {
        // 常量类禁止实例化
    }

    // ========== 操作类型（负责人变更历史） ==========
    /** 新增 */
    public static final String OPERATION_ADD = "ADD";

    /** 转移 */
    public static final String OPERATION_TRANSFER = "TRANSFER";

    /** 领取 */
    public static final String OPERATION_CLAIM = "CLAIM";

    /** 分配 */
    public static final String OPERATION_ASSIGN = "ASSIGN";

    /** 移入线索池 */
    public static final String OPERATION_MOVE_POOL = "MOVE_POOL";

    // ========== 转化模式 ==========
    /** 新建客户 */
    public static final String TRANSFORM_MODE_NEW = "NEW";

    /** 关联已有客户 */
    public static final String TRANSFORM_MODE_LINK = "LINK";

    // ========== 转化类型 ==========
    /** 转化为客户 */
    public static final String TRANSITION_TYPE_CUSTOMER = "CUSTOMER";
}
