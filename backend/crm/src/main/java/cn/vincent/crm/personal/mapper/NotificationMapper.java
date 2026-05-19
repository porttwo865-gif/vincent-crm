package cn.vincent.crm.personal.mapper;

import cn.vincent.crm.personal.domain.Notification;
import cn.vincent.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息通知通用 Mapper
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {

}
