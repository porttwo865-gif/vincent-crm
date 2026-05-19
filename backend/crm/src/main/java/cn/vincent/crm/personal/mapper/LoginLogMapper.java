package cn.vincent.crm.personal.mapper;

import cn.vincent.crm.personal.domain.LoginLog;
import cn.vincent.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志通用 Mapper
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {

}
