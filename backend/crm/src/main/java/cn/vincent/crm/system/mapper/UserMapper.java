package cn.vincent.crm.system.mapper;

import cn.vincent.mybatis.BaseMapper;
import cn.vincent.crm.system.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户通用 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
