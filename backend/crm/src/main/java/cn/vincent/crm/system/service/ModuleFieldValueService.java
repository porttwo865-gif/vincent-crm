package cn.vincent.crm.system.service;

import cn.vincent.common.util.IDGenerator;
import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import cn.vincent.crm.system.mapper.ExtModuleFieldMapper;
import cn.vincent.mybatis.BaseResourceField;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 通用字段值读写服务 - 负责各业务模块自定义字段值的 CRUD 操作
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ModuleFieldValueService {

    /** 模块字段自定义 Mapper */
    @Resource
    private ExtModuleFieldMapper extModuleFieldMapper;

    /** JDBC 模板（用于动态表插入） */
    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * formKey 到字段值表名的映射
     */
    private static final Map<String, String> FORM_KEY_TABLE_MAP = new HashMap<>();

    static {
        FORM_KEY_TABLE_MAP.put("clue", "clue_field");
        FORM_KEY_TABLE_MAP.put("customer", "customer_field");
        FORM_KEY_TABLE_MAP.put("opportunity", "opportunity_field");
        FORM_KEY_TABLE_MAP.put("contract", "contract_field");
        FORM_KEY_TABLE_MAP.put("product", "product_field");
    }

    /**
     * 需要使用 blob 表存储的字段类型（长文本/JSON）
     */
    private static final Set<String> BLOB_FIELD_TYPES = Set.of(
            "textarea", "sub_form", "file", "multi_select", "datasource_multiple"
    );

    /**
     * 保存资源的自定义字段值
     *
     * @param formKey      表单类型
     * @param resourceId   业务实体 ID
     * @param fieldValues  字段值列表
     * @param userId       操作用户
     */
    public void saveFieldValues(String formKey, String resourceId, List<ModuleFieldValueDTO> fieldValues, String userId) {
        if (fieldValues == null || fieldValues.isEmpty()) {
            return;
        }

        String tableName = getTableName(formKey);
        if (tableName == null) {
            log.warn("未找到表单 key {} 对应的字段值表", formKey);
            return;
        }

        // 先删除旧值
        deleteFieldValues(formKey, resourceId);

        long now = System.currentTimeMillis();

        for (ModuleFieldValueDTO fv : fieldValues) {
            if (StringUtils.isBlank(fv.getFieldId())) {
                continue;
            }

            // 判断是否需要存储到 blob 表
            String targetTable = tableName;
            if (BLOB_FIELD_TYPES.contains(fv.getFieldType())) {
                targetTable = tableName + "_blob";
            }

            String id = IDGenerator.nextStr();
            String sql = "INSERT INTO " + targetTable
                    + " (id, resource_id, field_id, field_type, name, internal_key, value, create_user, create_time)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, id, resourceId, fv.getFieldId(), fv.getFieldType(),
                    fv.getName(), null, fv.getValue(), userId, now);
        }
    }

    /**
     * 读取资源的自定义字段值
     *
     * @param formKey    表单类型
     * @param resourceId 业务实体 ID
     * @return 字段值列表
     */
    public List<ModuleFieldValueDTO> getFieldValues(String formKey, String resourceId) {
        String tableName = getTableName(formKey);
        if (tableName == null) {
            return Collections.emptyList();
        }

        List<ModuleFieldValueDTO> result = new ArrayList<>();

        // 查询普通字段值表
        List<BaseResourceField> fields = extModuleFieldMapper.selectFieldValuesByResourceId(tableName, resourceId);
        for (BaseResourceField f : fields) {
            ModuleFieldValueDTO dto = new ModuleFieldValueDTO();
            dto.setFieldId(f.getFieldId());
            dto.setName(f.getName());
            dto.setValue(f.getValue());
            dto.setFieldType(f.getFieldType());
            result.add(dto);
        }

        // 查询 blob 字段值表
        String blobTableName = tableName + "_blob";
        List<BaseResourceField> blobFields = extModuleFieldMapper.selectFieldValuesByResourceId(blobTableName, resourceId);
        for (BaseResourceField f : blobFields) {
            ModuleFieldValueDTO dto = new ModuleFieldValueDTO();
            dto.setFieldId(f.getFieldId());
            dto.setName(f.getName());
            dto.setValue(f.getValue());
            dto.setFieldType(f.getFieldType());
            result.add(dto);
        }

        return result;
    }

    /**
     * 批量读取资源的自定义字段值
     *
     * @param formKey     表单类型
     * @param resourceIds 业务实体 ID 列表
     * @return Map&lt;resourceId, List&lt;ModuleFieldValueDTO&gt;&gt;
     */
    public Map<String, List<ModuleFieldValueDTO>> batchGetFieldValues(String formKey, List<String> resourceIds) {
        if (resourceIds == null || resourceIds.isEmpty()) {
            return Collections.emptyMap();
        }

        String tableName = getTableName(formKey);
        if (tableName == null) {
            return Collections.emptyMap();
        }

        Map<String, List<ModuleFieldValueDTO>> result = new HashMap<>();

        // 初始化 Map
        for (String rid : resourceIds) {
            result.put(rid, new ArrayList<>());
        }

        // 查询普通字段值表
        List<BaseResourceField> fields = extModuleFieldMapper.selectFieldValuesByResourceIds(tableName, resourceIds);
        for (BaseResourceField f : fields) {
            ModuleFieldValueDTO dto = new ModuleFieldValueDTO();
            dto.setFieldId(f.getFieldId());
            dto.setName(f.getName());
            dto.setValue(f.getValue());
            dto.setFieldType(f.getFieldType());
            result.computeIfAbsent(f.getResourceId(), k -> new ArrayList<>()).add(dto);
        }

        // 查询 blob 字段值表
        String blobTableName = tableName + "_blob";
        List<BaseResourceField> blobFields = extModuleFieldMapper.selectFieldValuesByResourceIds(blobTableName, resourceIds);
        for (BaseResourceField f : blobFields) {
            ModuleFieldValueDTO dto = new ModuleFieldValueDTO();
            dto.setFieldId(f.getFieldId());
            dto.setName(f.getName());
            dto.setValue(f.getValue());
            dto.setFieldType(f.getFieldType());
            result.computeIfAbsent(f.getResourceId(), k -> new ArrayList<>()).add(dto);
        }

        return result;
    }

    /**
     * 删除资源的所有自定义字段值
     *
     * @param formKey    表单类型
     * @param resourceId 业务实体 ID
     */
    public void deleteFieldValues(String formKey, String resourceId) {
        String tableName = getTableName(formKey);
        if (tableName == null) {
            return;
        }

        // 删除普通字段值表
        extModuleFieldMapper.deleteFieldValuesByResourceId(tableName, resourceId);

        // 删除 blob 字段值表
        String blobTableName = tableName + "_blob";
        extModuleFieldMapper.deleteFieldValuesByResourceId(blobTableName, resourceId);
    }

    /**
     * 根据 formKey 获取字段值表名
     *
     * @param formKey 表单标识
     * @return 表名
     */
    private String getTableName(String formKey) {
        return FORM_KEY_TABLE_MAP.get(formKey);
    }
}
