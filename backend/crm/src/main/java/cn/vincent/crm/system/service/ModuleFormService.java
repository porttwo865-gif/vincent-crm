package cn.vincent.crm.system.service;

import cn.vincent.common.constants.FormKey;
import cn.vincent.common.constants.FieldType;
import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.system.domain.ModuleField;
import cn.vincent.crm.system.domain.ModuleForm;
import cn.vincent.crm.system.dto.response.ModuleFieldDTO;
import cn.vincent.crm.system.dto.response.ModuleFormConfigDTO;
import cn.vincent.crm.system.mapper.ExtModuleFieldMapper;
import cn.vincent.crm.system.mapper.ExtModuleFormMapper;
import cn.vincent.crm.system.mapper.ModuleFieldMapper;
import cn.vincent.crm.system.mapper.ModuleFormMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块表单服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ModuleFormService {

    /** 模块表单通用 Mapper */
    @Resource
    private ModuleFormMapper moduleFormMapper;

    /** 模块字段通用 Mapper */
    @Resource
    private ModuleFieldMapper moduleFieldMapper;

    /** 模块表单自定义 Mapper */
    @Resource
    private ExtModuleFormMapper extModuleFormMapper;

    /** 模块字段自定义 Mapper */
    @Resource
    private ExtModuleFieldMapper extModuleFieldMapper;

    /**
     * 获取表单配置（含字段列表）
     *
     * @param formKey 表单标识
     * @param orgId   组织 ID
     * @return 表单配置
     */
    public ModuleFormConfigDTO getFormConfig(String formKey, String orgId) {
        // 查询表单
        ModuleForm form = extSelectByFormKey(formKey, orgId);
        if (form == null) {
            throw new GenericException(Translator.get("module.form.not.exist"));
        }

        // 查询字段列表
        List<ModuleField> fields = extModuleFieldMapper.selectByFormKey(formKey, orgId);

        // 构建响应
        ModuleFormConfigDTO config = new ModuleFormConfigDTO();
        config.setFormId(form.getId());
        config.setFormKey(form.getFormKey());
        config.setFormName(form.getName());
        config.setFields(convertToFieldDTOs(fields));
        return config;
    }

    /**
     * 初始化模块表单（新组织创建时调用）
     * <p>
     * 为指定组织创建所有模块的表单定义和系统内置字段
     *
     * @param formKey 表单标识
     * @param orgId   组织 ID
     * @param userId  操作用户
     */
    public void initModuleForm(String formKey, String orgId, String userId) {
        // 检查表单是否已存在
        ModuleForm existingForm = extSelectByFormKey(formKey, orgId);
        if (existingForm != null) {
            log.info("表单已存在, formKey: {}, orgId: {}", formKey, orgId);
            return;
        }

        // 查找枚举
        FormKey fk = findByKey(formKey);
        if (fk == null) {
            log.warn("未知的表单标识: {}", formKey);
            return;
        }

        long now = System.currentTimeMillis();

        // 创建表单
        ModuleForm form = new ModuleForm();
        form.setId(IDGenerator.nextStr());
        form.setFormKey(fk.getKey());
        form.setName(fk.getLabel());
        form.setOrganizationId(orgId);
        form.setCreateUser(userId);
        form.setUpdateUser(userId);
        form.setCreateTime(now);
        form.setUpdateTime(now);
        moduleFormMapper.insert(form);

        // 初始化系统内置字段
        List<ModuleField> systemFields = buildSystemFields(form.getId(), fk, orgId, userId, now);
        if (!systemFields.isEmpty()) {
            moduleFieldMapper.batchInsert(systemFields);
        }

        log.info("初始化模块表单完成, formKey: {}, orgId: {}, 字段数: {}", formKey, orgId, systemFields.size());
    }

    /**
     * 为组织初始化所有模块表单
     *
     * @param orgId  组织 ID
     * @param userId 操作用户
     */
    public void initAllModuleForms(String orgId, String userId) {
        for (FormKey fk : FormKey.values()) {
            initModuleForm(fk.getKey(), orgId, userId);
        }
    }

    /**
     * 按表单 key 查询表单（内部查询）
     *
     * @param formKey 表单标识
     * @param orgId   组织 ID
     * @return 表单实体
     */
    private ModuleForm extSelectByFormKey(String formKey, String orgId) {
        return extModuleFormMapper.selectByFormKeyAndOrgId(formKey, orgId);
    }

    /**
     * 根据 key 查找 FormKey 枚举
     *
     * @param key 表单 key
     * @return FormKey 枚举
     */
    private FormKey findByKey(String key) {
        for (FormKey fk : FormKey.values()) {
            if (fk.getKey().equals(key)) {
                return fk;
            }
        }
        return null;
    }

    /**
     * 构建系统内置字段列表
     *
     * @param formId 表单 ID
     * @param fk     表单枚举
     * @param orgId  组织 ID
     * @param userId 操作用户
     * @param now    当前时间
     * @return 字段列表
     */
    private List<ModuleField> buildSystemFields(String formId, FormKey fk, String orgId, String userId, long now) {
        List<ModuleField> fields = new ArrayList<>();

        // 通用系统字段
        fields.add(buildField(formId, fk.getKey(), "name", "名称", FieldType.TEXT.getCode(),
                "name", true, true, null, null, 1, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "owner", "负责人", FieldType.DATASOURCE.getCode(),
                "owner", true, false, null, null, 2, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "create_user", "创建人", FieldType.DATASOURCE.getCode(),
                "createUser", true, false, null, null, 3, true, false, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "create_time", "创建时间", FieldType.DATE_TIME.getCode(),
                "createTime", true, false, null, null, 4, true, false, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "update_time", "更新时间", FieldType.DATE_TIME.getCode(),
                "updateTime", true, false, null, null, 5, true, false, "基本信息", 1, orgId, userId, now));

        // 根据模块类型添加特定字段
        switch (fk) {
            case CLUE -> addClueFields(fields, formId, fk, orgId, userId, now);
            case CUSTOMER -> addCustomerFields(fields, formId, fk, orgId, userId, now);
            case OPPORTUNITY -> addOpportunityFields(fields, formId, fk, orgId, userId, now);
            case CONTRACT -> addContractFields(fields, formId, fk, orgId, userId, now);
            case PRODUCT -> addProductFields(fields, formId, fk, orgId, userId, now);
            default -> { /* 其他模块暂无特定字段 */ }
        }

        return fields;
    }

    /**
     * 线索特定字段
     */
    private void addClueFields(List<ModuleField> fields, String formId, FormKey fk, String orgId, String userId, long now) {
        fields.add(buildField(formId, fk.getKey(), "source", "线索来源", FieldType.SELECT.getCode(),
                "source", true, false, null, null, 10, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "status", "线索状态", FieldType.SELECT.getCode(),
                "status", true, false, null, null, 11, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "phone", "电话", FieldType.PHONE.getCode(),
                "phone", true, false, null, null, 12, true, true, "联系方式", 2, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "email", "邮箱", FieldType.EMAIL.getCode(),
                "email", true, false, null, null, 13, true, true, "联系方式", 2, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "address", "地址", FieldType.ADDRESS.getCode(),
                "address", true, false, null, null, 14, true, true, "联系方式", 2, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "remark", "备注", FieldType.TEXTAREA.getCode(),
                "remark", true, false, null, null, 15, true, true, "其他信息", 3, orgId, userId, now));
    }

    /**
     * 客户特定字段
     */
    private void addCustomerFields(List<ModuleField> fields, String formId, FormKey fk, String orgId, String userId, long now) {
        fields.add(buildField(formId, fk.getKey(), "customer_type", "客户类型", FieldType.SELECT.getCode(),
                "customerType", true, false, null, null, 10, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "industry", "行业", FieldType.SELECT.getCode(),
                "industry", true, false, null, null, 11, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "phone", "电话", FieldType.PHONE.getCode(),
                "phone", true, false, null, null, 12, true, true, "联系方式", 2, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "website", "网址", FieldType.WEBSITE.getCode(),
                "website", true, false, null, null, 13, true, true, "联系方式", 2, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "address", "地址", FieldType.ADDRESS.getCode(),
                "address", true, false, null, null, 14, true, true, "联系方式", 2, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "remark", "备注", FieldType.TEXTAREA.getCode(),
                "remark", true, false, null, null, 15, true, true, "其他信息", 3, orgId, userId, now));
    }

    /**
     * 商机特定字段
     */
    private void addOpportunityFields(List<ModuleField> fields, String formId, FormKey fk, String orgId, String userId, long now) {
        fields.add(buildField(formId, fk.getKey(), "amount", "商机金额", FieldType.MONEY.getCode(),
                "amount", true, false, null, null, 10, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "stage", "商机阶段", FieldType.SELECT.getCode(),
                "stage", true, false, null, null, 11, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "expected_date", "预计成交日期", FieldType.DATE.getCode(),
                "expectedDate", true, false, null, null, 12, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "remark", "备注", FieldType.TEXTAREA.getCode(),
                "remark", true, false, null, null, 13, true, true, "其他信息", 2, orgId, userId, now));
    }

    /**
     * 合同特定字段
     */
    private void addContractFields(List<ModuleField> fields, String formId, FormKey fk, String orgId, String userId, long now) {
        fields.add(buildField(formId, fk.getKey(), "amount", "合同金额", FieldType.MONEY.getCode(),
                "amount", true, false, null, null, 10, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "start_date", "开始日期", FieldType.DATE.getCode(),
                "startDate", true, false, null, null, 11, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "end_date", "结束日期", FieldType.DATE.getCode(),
                "endDate", true, false, null, null, 12, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "status", "合同状态", FieldType.SELECT.getCode(),
                "status", true, false, null, null, 13, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "remark", "备注", FieldType.TEXTAREA.getCode(),
                "remark", true, false, null, null, 14, true, true, "其他信息", 2, orgId, userId, now));
    }

    /**
     * 产品特定字段
     */
    private void addProductFields(List<ModuleField> fields, String formId, FormKey fk, String orgId, String userId, long now) {
        fields.add(buildField(formId, fk.getKey(), "price", "价格", FieldType.MONEY.getCode(),
                "price", true, false, null, null, 10, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "unit", "单位", FieldType.TEXT.getCode(),
                "unit", true, false, null, null, 11, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "category", "分类", FieldType.SELECT.getCode(),
                "category", true, false, null, null, 12, true, true, "基本信息", 1, orgId, userId, now));
        fields.add(buildField(formId, fk.getKey(), "remark", "备注", FieldType.TEXTAREA.getCode(),
                "remark", true, false, null, null, 13, true, true, "其他信息", 2, orgId, userId, now));
    }

    /**
     * 构建字段实体
     */
    private ModuleField buildField(String formId, String formKey, String fieldKey, String name,
                                   String fieldType, String internalKey, boolean isSystem, boolean required,
                                   String defaultValue, String options, int sort, boolean visible,
                                   boolean editable, String sectionName, int sectionSort,
                                   String orgId, String userId, long now) {
        ModuleField field = new ModuleField();
        field.setId(IDGenerator.nextStr());
        field.setFormId(formId);
        field.setFormKey(formKey);
        field.setName(name);
        field.setFieldKey(fieldKey);
        field.setFieldType(fieldType);
        field.setInternalKey(internalKey);
        field.setIsSystem(isSystem);
        field.setRequired(required);
        field.setDefaultValue(defaultValue);
        field.setOptions(options);
        field.setSort(sort);
        field.setVisible(visible);
        field.setEditable(editable);
        field.setSectionName(sectionName);
        field.setSectionSort(sectionSort);
        field.setOrganizationId(orgId);
        field.setCreateUser(userId);
        field.setUpdateUser(userId);
        field.setCreateTime(now);
        field.setUpdateTime(now);
        return field;
    }

    /**
     * 转换字段实体列表为 DTO 列表
     *
     * @param fields 字段实体列表
     * @return DTO 列表
     */
    private List<ModuleFieldDTO> convertToFieldDTOs(List<ModuleField> fields) {
        if (fields == null || fields.isEmpty()) {
            return List.of();
        }
        return fields.stream().map(field -> {
            ModuleFieldDTO dto = BeanUtils.copyBean(new ModuleFieldDTO(), field);
            return dto;
        }).toList();
    }
}
