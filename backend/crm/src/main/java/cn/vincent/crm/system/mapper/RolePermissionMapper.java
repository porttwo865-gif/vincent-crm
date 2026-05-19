package cn.vincent.crm.system.mapper;

import cn.vincent.mybatis.BaseMapper;
import cn.vincent.crm.system.domain.RolePermission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色权限关联通用 Mapper
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

}
