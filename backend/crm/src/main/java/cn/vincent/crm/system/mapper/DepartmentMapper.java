package cn.vincent.crm.system.mapper;

import cn.vincent.mybatis.BaseMapper;
import cn.vincent.crm.system.domain.Department;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门通用 Mapper
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

}
