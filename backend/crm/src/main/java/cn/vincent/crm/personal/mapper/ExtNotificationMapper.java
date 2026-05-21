package cn.vincent.crm.personal.mapper;

import cn.vincent.crm.personal.domain.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息通知自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtNotificationMapper {

    /**
     * 分页查询指定用户的通知列表
     *
     * @param userId 用户 ID
     * @param type   通知类型（可选，null 表示查全部）
     * @return 通知列表
     */
    List<Notification> selectByUserIdAndType(@Param("userId") String userId,
                                             @Param("type") String type);

    /**
     * 统计指定用户的未读通知数量
     *
     * @param userId 用户 ID
     * @return 未读数量
     */
    long countUnreadByUserId(@Param("userId") String userId);

    /**
     * 批量将指定 ID 的通知标记为已读
     *
     * @param ids    通知 ID 列表
     * @param userId 当前用户 ID（防止越权）
     */
    void batchMarkRead(@Param("ids") List<String> ids, @Param("userId") String userId);

    /**
     * 将指定用户的所有未读通知标记为已读
     *
     * @param userId 当前用户 ID
     */
    void markAllRead(@Param("userId") String userId);
}
