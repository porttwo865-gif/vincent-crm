SET SESSION innodb_lock_wait_timeout = 7200;

-- ============================================
-- 商机表单及字段
-- ============================================
INSERT INTO module_form (id, form_key, name, organization_id, create_user, update_user, create_time, update_time)
VALUES ('form_opportunity', 'opportunity', '商机', 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000)
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO module_field (id, form_id, form_key, name, field_key, field_type, internal_key, is_system, required, default_value, options, sort, visible, editable, section_name, section_sort, organization_id, create_user, update_user, create_time, update_time) VALUES
('fld_opp_name', 'form_opportunity', 'opportunity', '商机名称', 'name', 'text', 'name', 1, 1, NULL, NULL, 1, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_opp_customer', 'form_opportunity', 'opportunity', '客户', 'customer', 'text', 'customerId', 1, 0, NULL, NULL, 2, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_opp_contact', 'form_opportunity', 'opportunity', '联系人', 'contact', 'text', 'contactId', 1, 0, NULL, NULL, 3, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_opp_amount', 'form_opportunity', 'opportunity', '金额', 'amount', 'number', 'amount', 1, 0, NULL, NULL, 4, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_opp_expected_date', 'form_opportunity', 'opportunity', '预计成交日期', 'expected_date', 'date', 'expectedCloseTime', 1, 0, NULL, NULL, 5, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_opp_stage', 'form_opportunity', 'opportunity', '阶段', 'stage', 'select', 'stage', 1, 0, NULL, NULL, 6, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_opp_remark', 'form_opportunity', 'opportunity', '备注', 'remark', 'textarea', 'remark', 1, 0, NULL, NULL, 7, 1, 1, '其他信息', 2, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000);

-- ============================================
-- 合同表单及字段
-- ============================================
INSERT INTO module_form (id, form_key, name, organization_id, create_user, update_user, create_time, update_time)
VALUES ('form_contract', 'contract', '合同', 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000)
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO module_field (id, form_id, form_key, name, field_key, field_type, internal_key, is_system, required, default_value, options, sort, visible, editable, section_name, section_sort, organization_id, create_user, update_user, create_time, update_time) VALUES
('fld_ctr_name', 'form_contract', 'contract', '合同名称', 'name', 'text', 'name', 1, 1, NULL, NULL, 1, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_ctr_customer', 'form_contract', 'contract', '客户', 'customer', 'text', 'customerId', 1, 0, NULL, NULL, 2, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_ctr_opportunity', 'form_contract', 'contract', '商机', 'opportunity', 'text', 'opportunityId', 1, 0, NULL, NULL, 3, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_ctr_amount', 'form_contract', 'contract', '金额', 'amount', 'number', 'amount', 1, 0, NULL, NULL, 4, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_ctr_start_date', 'form_contract', 'contract', '开始日期', 'start_date', 'date', 'startDate', 1, 0, NULL, NULL, 5, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_ctr_end_date', 'form_contract', 'contract', '结束日期', 'end_date', 'date', 'endDate', 1, 0, NULL, NULL, 6, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_ctr_sign_date', 'form_contract', 'contract', '签订日期', 'sign_date', 'date', 'signedDate', 1, 0, NULL, NULL, 7, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_ctr_status', 'form_contract', 'contract', '状态', 'status', 'select', 'status', 1, 0, NULL, NULL, 8, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_ctr_remark', 'form_contract', 'contract', '备注', 'remark', 'textarea', 'remark', 1, 0, NULL, NULL, 9, 1, 1, '其他信息', 2, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000);

-- ============================================
-- 产品表单及字段
-- ============================================
INSERT INTO module_form (id, form_key, name, organization_id, create_user, update_user, create_time, update_time)
VALUES ('form_product', 'product', '产品', 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000)
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO module_field (id, form_id, form_key, name, field_key, field_type, internal_key, is_system, required, default_value, options, sort, visible, editable, section_name, section_sort, organization_id, create_user, update_user, create_time, update_time) VALUES
('fld_prd_name', 'form_product', 'product', '产品名称', 'name', 'text', 'name', 1, 1, NULL, NULL, 1, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_prd_code', 'form_product', 'product', '编码', 'code', 'text', 'code', 1, 0, NULL, NULL, 2, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_prd_category', 'form_product', 'product', '类别', 'category', 'select', 'category', 1, 0, NULL, NULL, 3, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_prd_price', 'form_product', 'product', '单价', 'price', 'number', 'price', 1, 0, NULL, NULL, 4, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_prd_unit', 'form_product', 'product', '单位', 'unit', 'text', 'unit', 1, 0, NULL, NULL, 5, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_prd_description', 'form_product', 'product', '描述', 'description', 'textarea', 'description', 1, 0, NULL, NULL, 6, 1, 1, '其他信息', 2, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000);

-- ============================================
-- 发票表单及字段
-- ============================================
INSERT INTO module_form (id, form_key, name, organization_id, create_user, update_user, create_time, update_time)
VALUES ('form_invoice', 'invoice', '发票', 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000)
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO module_field (id, form_id, form_key, name, field_key, field_type, internal_key, is_system, required, default_value, options, sort, visible, editable, section_name, section_sort, organization_id, create_user, update_user, create_time, update_time) VALUES
('fld_inv_no', 'form_invoice', 'invoice', '发票号', 'invoice_no', 'text', 'invoiceNo', 1, 1, NULL, NULL, 1, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_inv_contract', 'form_invoice', 'invoice', '合同', 'contract', 'text', 'contractId', 1, 0, NULL, NULL, 2, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_inv_customer', 'form_invoice', 'invoice', '客户', 'customer', 'text', 'customerId', 1, 0, NULL, NULL, 3, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_inv_amount', 'form_invoice', 'invoice', '金额', 'amount', 'number', 'amount', 1, 0, NULL, NULL, 4, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_inv_date', 'form_invoice', 'invoice', '开票日期', 'invoice_date', 'date', 'invoiceDate', 1, 0, NULL, NULL, 5, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_inv_type', 'form_invoice', 'invoice', '类型', 'type', 'select', 'invoiceType', 1, 0, NULL, NULL, 6, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_inv_remark', 'form_invoice', 'invoice', '备注', 'remark', 'textarea', 'remark', 1, 0, NULL, NULL, 7, 1, 1, '其他信息', 2, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000);

-- ============================================
-- 订单表单及字段
-- ============================================
INSERT INTO module_form (id, form_key, name, organization_id, create_user, update_user, create_time, update_time)
VALUES ('form_order', 'order_form', '订单', 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000)
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO module_field (id, form_id, form_key, name, field_key, field_type, internal_key, is_system, required, default_value, options, sort, visible, editable, section_name, section_sort, organization_id, create_user, update_user, create_time, update_time) VALUES
('fld_ord_no', 'form_order', 'order_form', '订单号', 'order_no', 'text', 'orderNo', 1, 0, NULL, NULL, 1, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_ord_customer', 'form_order', 'order_form', '客户', 'customer', 'text', 'customerId', 1, 0, NULL, NULL, 2, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_ord_contact', 'form_order', 'order_form', '联系人', 'contact', 'text', 'contactId', 1, 0, NULL, NULL, 3, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_ord_contract', 'form_order', 'order_form', '合同', 'contract', 'text', 'contractId', 1, 0, NULL, NULL, 4, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_ord_total_amount', 'form_order', 'order_form', '总金额', 'total_amount', 'number', 'totalAmount', 1, 0, NULL, NULL, 5, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_ord_status', 'form_order', 'order_form', '状态', 'status', 'select', 'status', 1, 0, NULL, NULL, 6, 1, 1, '基本信息', 1, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000),
('fld_ord_remark', 'form_order', 'order_form', '备注', 'remark', 'textarea', 'remark', 1, 0, NULL, NULL, 7, 1, 1, '其他信息', 2, 'org_default', 'user_admin', 'user_admin', UNIX_TIMESTAMP()*1000, UNIX_TIMESTAMP()*1000);

SET SESSION innodb_lock_wait_timeout = DEFAULT;
