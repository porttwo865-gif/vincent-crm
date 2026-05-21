package cn.vincent.crm.personal.service;

import cn.vincent.common.response.PagerWithOption;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.crm.personal.domain.Notification;
import cn.vincent.crm.personal.dto.request.NotificationPageRequest;
import cn.vincent.crm.personal.dto.response.NotificationListResponse;
import cn.vincent.crm.personal.dto.response.UnreadCountResponse;
import cn.vincent.crm.personal.mapper.ExtNotificationMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 消息通知服务 - 处理通知的分页查询、标记已读和未读统计
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class NotificationService {

    /** 消息通知自定义 Mapper */
    @Resource
    private ExtNotificationMapper extNotificationMapper;

    /**
     * 分页查询当前用户的消息通知
     *
     * @param request 分页请求
     * @param userId  当前用户 ID
     * @return 通知分页结果
     */
    public PagerWithOption<List<NotificationListResponse>> page(NotificationPageRequest request, String userId) {
        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        List<Notification> notifications = extNotificationMapper.selectByUserIdAndType(userId, request.getType());
        PageInfo<Notification> pageInfo = new PageInfo<>(notifications);

        List<NotificationListResponse> responseList = notifications.stream()
                .map(notification -> BeanUtils.copyBean(new NotificationListResponse(), notification))
                .toList();

        return PagerWithOption.of(responseList, pageInfo.getTotal(), request.getCurrent(), request.getPageSize());
    }

    /**
     * 标记单条通知为已读
     *
     * @param id     通知 ID
     * @param userId 当前用户 ID
     */
    public void markRead(String id, String userId) {
        extNotificationMapper.batchMarkRead(List.of(id), userId);
    }

    /**
     * 标记当前用户所有通知为已读
     *
     * @param userId 当前用户 ID
     */
    public void markAllRead(String userId) {
        extNotificationMapper.markAllRead(userId);
    }

    /**
     * 获取当前用户未读通知数量
     *
     * @param userId 当前用户 ID
     * @return 未读数量响应
     */
    public UnreadCountResponse getUnreadCount(String userId) {
        long count = extNotificationMapper.countUnreadByUserId(userId);
        return UnreadCountResponse.of(count);
    }
}
