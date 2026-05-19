package cn.vincent.common.util;

import cn.vincent.common.dto.ConditionDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 条件过滤工具类 - 解析前端传入的条件筛选参数
 * <p>
 * 支持的操作符：EQUALS, NOT_EQUALS, CONTAINS, NOT_CONTAINS,
 * STARTS_WITH, ENDS_WITH, IS_EMPTY, IS_NOT_EMPTY,
 * GT, GTE, LT, LTE, BETWEEN, IN, NOT_IN
 */
public class ConditionFilterUtils {

    private ConditionFilterUtils() {
        // 工具类禁止实例化
    }

    /**
     * 解析条件（将前端条件参数转换为可用于 MyBatis 动态 SQL 的结构）
     * <p>
     * 从 request 对象中通过反射获取 conditions 字段，
     * 将条件转换为可用于 MyBatis 动态 SQL 的结构
     *
     * @param request 分页请求对象
     * @param formKey 表单标识
     */
    public static void parseCondition(Object request, String formKey) {
        if (request == null) {
            return;
        }

        try {
            // 通过反射获取 conditions 字段
            java.lang.reflect.Field conditionsField = request.getClass().getDeclaredField("conditions");
            conditionsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            List<ConditionDTO> conditions = (List<ConditionDTO>) conditionsField.get(request);
            if (conditions == null || conditions.isEmpty()) {
                return;
            }

            // 将解析后的条件存储到 request 的 parsedConditions 字段中
            List<ParsedCondition> parsedConditions = new ArrayList<>();
            for (ConditionDTO condition : conditions) {
                ParsedCondition parsed = parseSingleCondition(condition);
                if (parsed != null) {
                    parsedConditions.add(parsed);
                }
            }

            // 通过反射设置 parsedConditions 字段
            try {
                java.lang.reflect.Field parsedField = request.getClass().getDeclaredField("parsedConditions");
                parsedField.setAccessible(true);
                parsedField.set(request, parsedConditions);
            } catch (NoSuchFieldException e) {
                // 如果 request 没有 parsedConditions 字段，跳过设置
            }

        } catch (NoSuchFieldException e) {
            // request 没有 conditions 字段，跳过
        } catch (IllegalAccessException e) {
            // 访问失败，跳过
        }
    }

    /**
     * 解析单个条件
     *
     * @param condition 条件 DTO
     * @return 解析后的条件
     */
    private static ParsedCondition parseSingleCondition(ConditionDTO condition) {
        if (condition == null || StringUtils.isBlank(condition.getOperator())) {
            return null;
        }

        ParsedCondition parsed = new ParsedCondition();
        parsed.setFieldId(condition.getFieldId());
        parsed.setFieldType(condition.getFieldType());
        parsed.setInternalKey(condition.getInternalKey());
        parsed.setOperator(condition.getOperator());
        parsed.setValue(condition.getValue());

        return parsed;
    }

    /**
     * 根据 internalKey 判断条件是否作用于固定字段（而非自定义字段）
     *
     * @param condition 解析后的条件
     * @return true 表示作用于固定字段
     */
    public static boolean isInternalField(ParsedCondition condition) {
        return StringUtils.isNotBlank(condition.getInternalKey());
    }

    /**
     * 构建条件 SQL 片段（用于 MyBatis 动态 SQL）
     *
     * @param condition 解析后的条件
     * @return SQL 片段
     */
    public static String buildConditionSql(ParsedCondition condition) {
        String column = isInternalField(condition)
                ? camelToSnake(condition.getInternalKey())
                : condition.getFieldId();

        return switch (condition.getOperator()) {
            case "EQUALS" -> column + " = #{value}";
            case "NOT_EQUALS" -> column + " != #{value}";
            case "CONTAINS" -> column + " LIKE CONCAT('%', #{value}, '%')";
            case "NOT_CONTAINS" -> column + " NOT LIKE CONCAT('%', #{value}, '%')";
            case "STARTS_WITH" -> column + " LIKE CONCAT(#{value}, '%')";
            case "ENDS_WITH" -> column + " LIKE CONCAT('%', #{value})";
            case "IS_EMPTY" -> "(" + column + " IS NULL OR " + column + " = '')";
            case "IS_NOT_EMPTY" -> "(" + column + " IS NOT NULL AND " + column + " != '')";
            case "GT" -> column + " > #{value}";
            case "GTE" -> column + " >= #{value}";
            case "LT" -> column + " < #{value}";
            case "LTE" -> column + " <= #{value}";
            case "BETWEEN" -> column + " BETWEEN #{value} AND #{value2}";
            case "IN" -> column + " IN #{value}";
            case "NOT_IN" -> column + " NOT IN #{value}";
            default -> "";
        };
    }

    /**
     * 驼峰转下划线
     *
     * @param camelStr 驼峰字符串
     * @return 下划线字符串
     */
    private static String camelToSnake(String camelStr) {
        if (StringUtils.isBlank(camelStr)) {
            return camelStr;
        }
        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(camelStr.charAt(0)));
        for (int i = 1; i < camelStr.length(); i++) {
            char ch = camelStr.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append('_').append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    /**
     * 解析后的条件结构
     */
    public static class ParsedCondition {

        /** 字段 ID */
        private String fieldId;

        /** 字段类型 */
        private String fieldType;

        /** 内部键 */
        private String internalKey;

        /** 操作符 */
        private String operator;

        /** 值 */
        private Object value;

        public String getFieldId() {
            return fieldId;
        }

        public void setFieldId(String fieldId) {
            this.fieldId = fieldId;
        }

        public String getFieldType() {
            return fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }

        public String getInternalKey() {
            return internalKey;
        }

        public void setInternalKey(String internalKey) {
            this.internalKey = internalKey;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
