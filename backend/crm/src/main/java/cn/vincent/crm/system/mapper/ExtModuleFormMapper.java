package cn.vincent.crm.system.mapper;

import cn.vincent.crm.system.domain.ModuleForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 模块表单自定义 Mapper
 */
@Mapper
public interface ExtModuleFormMapper {

    /**
     * 按表单 key 和组织 ID 查询表单
     *
     * @param formKey 表单标识
     * @param orgId   组织 ID
     * @return 表单实体
     */
    ModuleForm selectByFormKeyAndOrgId(@Param("formKey") String formKey, @Param("orgId") String orgId);
}
