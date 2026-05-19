package cn.vincent.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段类型枚举
 */
@Getter
@AllArgsConstructor
public enum FieldType {

    TEXT("text", "单行文本"),
    TEXTAREA("textarea", "多行文本"),
    NUMBER("number", "数字"),
    DATE_TIME("date_time", "日期时间"),
    DATE("date", "日期"),
    SELECT("select", "下拉选择"),
    MULTI_SELECT("multi_select", "多选"),
    CHECKBOX("checkbox", "复选框"),
    RADIO("radio", "单选"),
    DATASOURCE("datasource", "数据源关联"),
    DATASOURCE_MULTIPLE("datasource_multiple", "数据源多选关联"),
    SUB_FORM("sub_form", "子表单"),
    FILE("file", "附件"),
    PHONE("phone", "电话"),
    EMAIL("email", "邮箱"),
    WEBSITE("website", "网址"),
    ADDRESS("address", "地址"),
    MONEY("money", "金额"),
    PERCENT("percent", "百分比");

    /** 类型编码 */
    private final String code;

    /** 类型名称 */
    private final String label;
}
