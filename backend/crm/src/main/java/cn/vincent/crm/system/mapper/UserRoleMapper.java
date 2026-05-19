package cn.vincent.crm.system.mapper;

import cn.vincent.mybatis.BaseMapper;
import cn.vincent.crm.system.domain.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关联通用 Mapper
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
