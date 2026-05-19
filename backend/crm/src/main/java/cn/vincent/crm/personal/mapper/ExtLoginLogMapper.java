package cn.vincent.crm.personal.mapper;

import cn.vincent.crm.personal.domain.LoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 登录日志自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtLoginLogMapper {

    /**
     * 分页查询指定用户的登录日志列表
     *
     * @param userId 用户 ID
     * @return 登录日志列表
     */
    List<LoginLog> selectByUserId(@Param("userId") String userId);
}
