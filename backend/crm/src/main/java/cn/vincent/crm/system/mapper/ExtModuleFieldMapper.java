package cn.vincent.crm.system.mapper;

import cn.vincent.crm.system.domain.ModuleField;
import cn.vincent.mybatis.BaseResourceField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模块字段自定义 Mapper
 */
@Mapper
public interface ExtModuleFieldMapper {

    /**
     * 按表单 key 查询所有字段
     *
     * @param formKey 表单标识
     * @param orgId   组织 ID
     * @return 字段列表
     */
    List<ModuleField> selectByFormKey(@Param("formKey") String formKey, @Param("orgId") String orgId);

    /**
     * 按表单 ID 查询字段
     *
     * @param formId 表单 ID
     * @return 字段列表
     */
    List<ModuleField> selectByFormId(@Param("formId") String formId);

    /**
     * 按表单 key 和字段标识查询字段（用于校验重复）
     *
     * @param formKey  表单标识
     * @param fieldKey 字段标识
     * @param orgId    组织 ID
     * @return 字段对象
     */
    ModuleField selectByFormKeyAndFieldKey(@Param("formKey") String formKey,
                                           @Param("fieldKey") String fieldKey,
                                           @Param("orgId") String orgId);

    /**
     * 按表单 key 删除字段（初始化时使用）
     *
     * @param formKey 表单标识
     * @param orgId   组织 ID
     * @return 影响行数
     */
    int deleteByFormKey(@Param("formKey") String formKey, @Param("orgId") String orgId);

    /**
     * 按资源 ID 删除字段值
     *
     * @param tableName 表名
     * @param resourceId 资源 ID
     * @return 影响行数
     */
    int deleteFieldValuesByResourceId(@Param("tableName") String tableName, @Param("resourceId") String resourceId);

    /**
     * 按资源 ID 查询字段值
     *
     * @param tableName  表名
     * @param resourceId 资源 ID
     * @return 字段值列表
     */
    List<BaseResourceField> selectFieldValuesByResourceId(@Param("tableName") String tableName, @Param("resourceId") String resourceId);

    /**
     * 批量按资源 ID 查询字段值
     *
     * @param tableName   表名
     * @param resourceIds 资源 ID 列表
     * @return 字段值列表
     */
    List<BaseResourceField> selectFieldValuesByResourceIds(@Param("tableName") String tableName, @Param("resourceIds") List<String> resourceIds);
}
