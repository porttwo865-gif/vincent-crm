package cn.vincent.crm.system.service;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.system.domain.ModuleField;
import cn.vincent.crm.system.domain.ModuleForm;
import cn.vincent.crm.system.dto.request.ModuleFieldAddRequest;
import cn.vincent.crm.system.dto.request.ModuleFieldUpdateRequest;
import cn.vincent.crm.system.dto.response.ModuleFieldDTO;
import cn.vincent.crm.system.mapper.ExtModuleFieldMapper;
import cn.vincent.crm.system.mapper.ExtModuleFormMapper;
import cn.vincent.crm.system.mapper.ModuleFieldMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 模块字段服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ModuleFieldService {

    /** 模块字段通用 Mapper */
    @Resource
    private ModuleFieldMapper moduleFieldMapper;

    /** 模块字段自定义 Mapper */
    @Resource
    private ExtModuleFieldMapper extModuleFieldMapper;

    /** 模块表单自定义 Mapper */
    @Resource
    private ExtModuleFormMapper extModuleFormMapper;

    /** 表单缓存服务 */
    @Resource
    private ModuleFormCacheService moduleFormCacheService;

    /**
     * 添加字段
     *
     * @param request 添加请求
     * @param userId  操作用户
     * @param orgId   组织 ID
     * @return 新增的字段实体
     */
    public ModuleField addField(ModuleFieldAddRequest request, String userId, String orgId) {
        // 查询表单
        ModuleForm form = moduleFieldMapper.selectByPrimaryKey(request.getFormId()) != null
                ? extModuleFormMapper.selectByFormKeyAndOrgId(null, orgId) // 通过 formId 获取 formKey
                : null;

        // 校验字段标识唯一性
        ModuleField existing = extModuleFieldMapper.selectByFormKeyAndFieldKey(
                request.getFieldKey(), request.getFieldKey(), orgId);
        // 简化校验：通过 formId 查找对应的 formKey 后进行校验
        List<ModuleField> fieldsByFormId = extModuleFieldMapper.selectByFormId(request.getFormId());
        for (ModuleField f : fieldsByFormId) {
            if (f.getFieldKey().equals(request.getFieldKey())) {
                throw new GenericException(Translator.get("module.field.key.duplicate"));
            }
        }

        long now = System.currentTimeMillis();

        // 获取 formKey
        String formKey = fieldsByFormId.isEmpty() ? "" : fieldsByFormId.get(0).getFormKey();

        ModuleField field = new ModuleField();
        field.setId(IDGenerator.nextStr());
        field.setFormId(request.getFormId());
        field.setFormKey(formKey);
        field.setName(request.getName());
        field.setFieldKey(request.getFieldKey());
        field.setFieldType(request.getFieldType());
        field.setInternalKey(request.getInternalKey());
        field.setIsSystem(request.getIsSystem() != null ? request.getIsSystem() : false);
        field.setRequired(request.getRequired() != null ? request.getRequired() : false);
        field.setDefaultValue(request.getDefaultValue());
        field.setOptions(request.getOptions());
        field.setSort(request.getSort() != null ? request.getSort() : 0);
        field.setVisible(request.getVisible() != null ? request.getVisible() : true);
        field.setEditable(request.getEditable() != null ? request.getEditable() : true);
        field.setSectionName(request.getSectionName());
        field.setSectionSort(request.getSectionSort() != null ? request.getSectionSort() : 0);
        field.setOrganizationId(orgId);
        field.setCreateUser(userId);
        field.setUpdateUser(userId);
        field.setCreateTime(now);
        field.setUpdateTime(now);
        moduleFieldMapper.insert(field);

        // 清除表单缓存
        moduleFormCacheService.clearCache(formKey, orgId);

        return field;
    }

    /**
     * 更新字段
     *
     * @param request 更新请求
     * @param userId  操作用户
     * @return 更新后的字段实体
     */
    public ModuleField updateField(ModuleFieldUpdateRequest request, String userId) {
        ModuleField field = moduleFieldMapper.selectByPrimaryKey(request.getId());
        if (field == null) {
            throw new GenericException(Translator.get("module.field.not.exist"));
        }

        if (request.getName() != null) {
            field.setName(request.getName());
        }
        if (request.getFieldType() != null) {
            field.setFieldType(request.getFieldType());
        }
        if (request.getInternalKey() != null) {
            field.setInternalKey(request.getInternalKey());
        }
        if (request.getRequired() != null) {
            field.setRequired(request.getRequired());
        }
        if (request.getDefaultValue() != null) {
            field.setDefaultValue(request.getDefaultValue());
        }
        if (request.getOptions() != null) {
            field.setOptions(request.getOptions());
        }
        if (request.getSort() != null) {
            field.setSort(request.getSort());
        }
        if (request.getVisible() != null) {
            field.setVisible(request.getVisible());
        }
        if (request.getEditable() != null) {
            field.setEditable(request.getEditable());
        }
        if (request.getSectionName() != null) {
            field.setSectionName(request.getSectionName());
        }
        if (request.getSectionSort() != null) {
            field.setSectionSort(request.getSectionSort());
        }
        field.setUpdateUser(userId);
        field.setUpdateTime(System.currentTimeMillis());
        moduleFieldMapper.update(field);

        // 清除表单缓存
        moduleFormCacheService.clearCache(field.getFormKey(), field.getOrganizationId());

        return field;
    }

    /**
     * 删除字段（仅允许删除非系统字段）
     *
     * @param fieldId 字段 ID
     */
    public void deleteField(String fieldId) {
        ModuleField field = moduleFieldMapper.selectByPrimaryKey(fieldId);
        if (field == null) {
            throw new GenericException(Translator.get("module.field.not.exist"));
        }

        // 系统内置字段不可删除
        if (field.getIsSystem() != null && field.getIsSystem()) {
            throw new GenericException(Translator.get("module.field.system.cannot.delete"));
        }

        moduleFieldMapper.deleteByIds(List.of(fieldId));

        // 清除表单缓存
        moduleFormCacheService.clearCache(field.getFormKey(), field.getOrganizationId());
    }

    /**
     * 获取表单字段列表
     *
     * @param formKey 表单标识
     * @param orgId   组织 ID
     * @return 字段 DTO 列表
     */
    public List<ModuleFieldDTO> getFieldList(String formKey, String orgId) {
        List<ModuleField> fields = extModuleFieldMapper.selectByFormKey(formKey, orgId);
        return fields.stream().map(field -> BeanUtils.copyBean(new ModuleFieldDTO(), field)).toList();
    }

    /**
     * 字段排序
     *
     * @param fieldIds 排序后的字段 ID 列表
     */
    public void sortFields(List<String> fieldIds) {
        if (fieldIds == null || fieldIds.isEmpty()) {
            return;
        }

        for (int i = 0; i < fieldIds.size(); i++) {
            ModuleField field = moduleFieldMapper.selectByPrimaryKey(fieldIds.get(i));
            if (field != null) {
                field.setSort(i + 1);
                field.setUpdateTime(System.currentTimeMillis());
                moduleFieldMapper.update(field);
            }
        }
    }
}
