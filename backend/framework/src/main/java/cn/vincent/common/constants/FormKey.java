package cn.vincent.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 模块表单类型枚举
 */
@Getter
@AllArgsConstructor
public enum FormKey {

    CLUE("clue", "线索"),
    CUSTOMER("customer", "客户"),
    CONTACT("contact", "联系人"),
    OPPORTUNITY("opportunity", "商机"),
    QUOTATION("quotation", "报价单"),
    PRODUCT("product", "产品"),
    PRICE("price", "价格"),
    CONTRACT("contract", "合同"),
    PAYMENT_PLAN("payment_plan", "回款计划"),
    PAYMENT_RECORD("payment_record", "回款记录"),
    INVOICE("invoice", "发票"),
    ORDER("order_form", "订单"),
    FOLLOW_RECORD("follow_record", "跟进记录"),
    FOLLOW_PLAN("follow_plan", "跟进计划");

    /** 表单标识 */
    private final String key;

    /** 表单名称 */
    private final String label;
}
