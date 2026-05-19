package cn.vincent.crm.system.mapper;

import cn.vincent.crm.system.domain.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtDepartmentMapper {

    /**
     * 查询组织下所有部门
     *
     * @param orgId 组织 ID
     * @return 部门列表
     */
    List<Department> selectByOrgId(@Param("orgId") String orgId);

    /**
     * 查询指定父部门下的子部门数量
     *
     * @param parentId 父部门 ID
     * @return 子部门数量
     */
    int countByParentId(@Param("parentId") String parentId);

    /**
     * 查询部门下关联的用户数量
     *
     * @param departmentId 部门 ID
     * @return 关联用户数量
     */
    int countUsersByDeptId(@Param("departmentId") String departmentId);
}
