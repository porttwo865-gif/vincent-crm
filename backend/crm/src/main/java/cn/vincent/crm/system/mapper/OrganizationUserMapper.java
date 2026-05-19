package cn.vincent.crm.system.mapper;

import cn.vincent.mybatis.BaseMapper;
import cn.vincent.crm.system.domain.OrganizationUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织用户关联通用 Mapper
 */
@Mapper
public interface OrganizationUserMapper extends BaseMapper<OrganizationUser> {

}
