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
}
