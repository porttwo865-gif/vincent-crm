package cn.vincent.common.constants;

/**
 * 权限常量定义
 */
public class PermissionConstants {

    private PermissionConstants() {
        // 常量类禁止实例化
    }

    // ========== 系统管理 ==========
    public static final String SYSTEM_USER_READ = "SYSTEM_USER_READ";
    public static final String SYSTEM_USER_ADD = "SYSTEM_USER_ADD";
    public static final String SYSTEM_USER_UPDATE = "SYSTEM_USER_UPDATE";
    public static final String SYSTEM_USER_DELETE = "SYSTEM_USER_DELETE";

    public static final String SYSTEM_ROLE_READ = "SYSTEM_ROLE_READ";
    public static final String SYSTEM_ROLE_ADD = "SYSTEM_ROLE_ADD";
    public static final String SYSTEM_ROLE_UPDATE = "SYSTEM_ROLE_UPDATE";
    public static final String SYSTEM_ROLE_DELETE = "SYSTEM_ROLE_DELETE";

    public static final String SYSTEM_DEPARTMENT_READ = "SYSTEM_DEPARTMENT_READ";
    public static final String SYSTEM_DEPARTMENT_ADD = "SYSTEM_DEPARTMENT_ADD";
    public static final String SYSTEM_DEPARTMENT_UPDATE = "SYSTEM_DEPARTMENT_UPDATE";
    public static final String SYSTEM_DEPARTMENT_DELETE = "SYSTEM_DEPARTMENT_DELETE";

    // ========== 认证管理 ==========
    public static final String AUTH_LOGIN = "AUTH_LOGIN";
    public static final String AUTH_LOGOUT = "AUTH_LOGOUT";

    // ========== 模块表单管理 ==========
    public static final String MODULE_FORM_READ = "MODULE_FORM_READ";
    public static final String MODULE_FORM_UPDATE = "MODULE_FORM_UPDATE";

    // ========== 线索管理 ==========
    public static final String CLUE_MANAGEMENT_READ = "CLUE_MANAGEMENT_READ";
    public static final String CLUE_MANAGEMENT_ADD = "CLUE_MANAGEMENT_ADD";
    public static final String CLUE_MANAGEMENT_UPDATE = "CLUE_MANAGEMENT_UPDATE";
    public static final String CLUE_MANAGEMENT_DELETE = "CLUE_MANAGEMENT_DELETE";
    public static final String CLUE_MANAGEMENT_TRANSFORM = "CLUE_MANAGEMENT_TRANSFORM";
    public static final String CLUE_MANAGEMENT_RECYCLE = "CLUE_MANAGEMENT_RECYCLE";

    // ========== 线索池 ==========
    public static final String CLUE_POOL_READ = "CLUE_POOL_READ";
    public static final String CLUE_POOL_CLAIM = "CLUE_POOL_CLAIM";
    public static final String CLUE_POOL_ASSIGN = "CLUE_POOL_ASSIGN";

    // ========== 客户管理 ==========
    public static final String CUSTOMER_MANAGEMENT_READ = "CUSTOMER_MANAGEMENT_READ";
    public static final String CUSTOMER_MANAGEMENT_ADD = "CUSTOMER_MANAGEMENT_ADD";
    public static final String CUSTOMER_MANAGEMENT_UPDATE = "CUSTOMER_MANAGEMENT_UPDATE";
    public static final String CUSTOMER_MANAGEMENT_DELETE = "CUSTOMER_MANAGEMENT_DELETE";
    public static final String CUSTOMER_MANAGEMENT_RECYCLE = "CUSTOMER_MANAGEMENT_RECYCLE";

    // ========== 公海池 ==========
    public static final String CUSTOMER_POOL_READ = "CUSTOMER_POOL_READ";
    public static final String CUSTOMER_POOL_CLAIM = "CUSTOMER_POOL_CLAIM";
    public static final String CUSTOMER_POOL_ASSIGN = "CUSTOMER_POOL_ASSIGN";

    // ========== 商机管理 ==========
    public static final String OPPORTUNITY_MANAGEMENT_READ = "OPPORTUNITY_MANAGEMENT_READ";
    public static final String OPPORTUNITY_MANAGEMENT_ADD = "OPPORTUNITY_MANAGEMENT_ADD";
    public static final String OPPORTUNITY_MANAGEMENT_UPDATE = "OPPORTUNITY_MANAGEMENT_UPDATE";
    public static final String OPPORTUNITY_MANAGEMENT_DELETE = "OPPORTUNITY_MANAGEMENT_DELETE";

    // ========== 产品管理 ==========
    public static final String PRODUCT_MANAGEMENT_READ = "PRODUCT_MANAGEMENT_READ";
    public static final String PRODUCT_MANAGEMENT_ADD = "PRODUCT_MANAGEMENT_ADD";
    public static final String PRODUCT_MANAGEMENT_UPDATE = "PRODUCT_MANAGEMENT_UPDATE";
    public static final String PRODUCT_MANAGEMENT_DELETE = "PRODUCT_MANAGEMENT_DELETE";

    // ========== 合同管理 ==========
    public static final String CONTRACT_MANAGEMENT_READ = "CONTRACT_MANAGEMENT_READ";
    public static final String CONTRACT_MANAGEMENT_ADD = "CONTRACT_MANAGEMENT_ADD";
    public static final String CONTRACT_MANAGEMENT_UPDATE = "CONTRACT_MANAGEMENT_UPDATE";
    public static final String CONTRACT_MANAGEMENT_DELETE = "CONTRACT_MANAGEMENT_DELETE";

    // ========== 发票管理 ==========
    public static final String INVOICE_MANAGEMENT_READ = "INVOICE_MANAGEMENT_READ";
    public static final String INVOICE_MANAGEMENT_ADD = "INVOICE_MANAGEMENT_ADD";
    public static final String INVOICE_MANAGEMENT_UPDATE = "INVOICE_MANAGEMENT_UPDATE";
    public static final String INVOICE_MANAGEMENT_DELETE = "INVOICE_MANAGEMENT_DELETE";

    // ========== 订单管理 ==========
    public static final String ORDER_MANAGEMENT_READ = "ORDER_MANAGEMENT_READ";
    public static final String ORDER_MANAGEMENT_ADD = "ORDER_MANAGEMENT_ADD";
    public static final String ORDER_MANAGEMENT_UPDATE = "ORDER_MANAGEMENT_UPDATE";
    public static final String ORDER_MANAGEMENT_DELETE = "ORDER_MANAGEMENT_DELETE";

    // ========== 审批模板 ==========
    public static final String APPROVAL_TEMPLATE_READ = "APPROVAL_TEMPLATE_READ";
    public static final String APPROVAL_TEMPLATE_ADD = "APPROVAL_TEMPLATE_ADD";
    public static final String APPROVAL_TEMPLATE_UPDATE = "APPROVAL_TEMPLATE_UPDATE";
    public static final String APPROVAL_TEMPLATE_DELETE = "APPROVAL_TEMPLATE_DELETE";

    // ========== 审批实例 ==========
    public static final String APPROVAL_INSTANCE_READ = "APPROVAL_INSTANCE_READ";
    public static final String APPROVAL_INSTANCE_SUBMIT = "APPROVAL_INSTANCE_SUBMIT";

    // ========== 跟进记录 ==========
    public static final String FOLLOW_RECORD_READ = "FOLLOW_RECORD_READ";
    public static final String FOLLOW_RECORD_ADD = "FOLLOW_RECORD_ADD";
    public static final String FOLLOW_RECORD_UPDATE = "FOLLOW_RECORD_UPDATE";
    public static final String FOLLOW_RECORD_DELETE = "FOLLOW_RECORD_DELETE";

    // ========== 跟进计划 ==========
    public static final String FOLLOW_PLAN_READ = "FOLLOW_PLAN_READ";
    public static final String FOLLOW_PLAN_ADD = "FOLLOW_PLAN_ADD";
    public static final String FOLLOW_PLAN_UPDATE = "FOLLOW_PLAN_UPDATE";
    public static final String FOLLOW_PLAN_DELETE = "FOLLOW_PLAN_DELETE";
}
