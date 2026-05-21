package cn.vincent.crm.personal.controller;

import cn.vincent.common.response.PagerWithOption;
import cn.vincent.crm.personal.dto.request.LoginLogPageRequest;
import cn.vincent.crm.personal.dto.request.NotificationPageRequest;
import cn.vincent.crm.personal.dto.request.PasswordChangeRequest;
import cn.vincent.crm.personal.dto.request.PersonalUpdateRequest;
import cn.vincent.crm.personal.dto.response.LoginLogListResponse;
import cn.vincent.crm.personal.dto.response.NotificationListResponse;
import cn.vincent.crm.personal.dto.response.PersonalInfoResponse;
import cn.vincent.crm.personal.dto.response.UnreadCountResponse;
import cn.vincent.crm.personal.service.LoginLogService;
import cn.vincent.crm.personal.service.NotificationService;
import cn.vincent.crm.personal.service.PersonalService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 个人中心控制器 - 提供个人信息、密码修改、登录日志、消息通知接口
 */
@RestController
@RequestMapping("/personal")
@Tag(name = "个人中心")
public class PersonalController {

    /** 个人信息服务 */
    @Resource
    private PersonalService personalService;

    /** 登录日志服务 */
    @Resource
    private LoginLogService loginLogService;

    /** 消息通知服务 */
    @Resource
    private NotificationService notificationService;

    // ==================== 个人信息 ====================

    /**
     * 获取当前用户个人信息
     *
     * @return 个人信息响应
     */
    @GetMapping("/profile")
    @Operation(summary = "获取个人信息")
    public PersonalInfoResponse getInfo() {
        return personalService.getPersonalInfo(SessionUtils.getUserId());
    }

    /**
     * 修改个人信息（昵称、手机号、邮箱、头像）
     *
     * @param request 修改请求
     */
    @PostMapping("/profile/update")
    @Operation(summary = "修改个人信息")
    public void update(@RequestBody PersonalUpdateRequest request) {
        personalService.updatePersonalInfo(request, SessionUtils.getUserId());
    }

    /**
     * 修改密码
     *
     * @param request 修改密码请求
     */
    @PostMapping("/password/change")
    @Operation(summary = "修改密码")
    public void changePassword(@Validated @RequestBody PasswordChangeRequest request) {
        personalService.changePassword(request, SessionUtils.getUserId());
    }

    // ==================== 登录日志 ====================

    /**
     * 分页查询当前用户登录日志
     *
     * @param request 分页请求
     * @return 登录日志分页结果
     */
    @PostMapping("/login/log")
    @Operation(summary = "登录日志列表")
    public PagerWithOption<List<LoginLogListResponse>> loginLog(@RequestBody LoginLogPageRequest request) {
        return loginLogService.page(request, SessionUtils.getUserId());
    }

    // ==================== 消息通知 ====================

    /**
     * 分页查询我的消息通知
     *
     * @param request 分页请求
     * @return 通知分页结果
     */
    @PostMapping("/notification")
    @Operation(summary = "消息通知列表")
    public PagerWithOption<List<NotificationListResponse>> notificationList(@RequestBody NotificationPageRequest request) {
        return notificationService.page(request, SessionUtils.getUserId());
    }

    /**
     * 标记单条通知为已读
     *
     * @param id 通知 ID
     */
    @PostMapping("/notification/read/{id}")
    @Operation(summary = "标记通知已读")
    public void markRead(@PathVariable String id) {
        notificationService.markRead(id, SessionUtils.getUserId());
    }

    /**
     * 标记所有通知为已读
     */
    @PostMapping("/notification/read/all")
    @Operation(summary = "全部标记已读")
    public void markAllRead() {
        notificationService.markAllRead(SessionUtils.getUserId());
    }

    /**
     * 获取未读通知数量
     *
     * @return 未读数量响应
     */
    @GetMapping("/notification/unread-count")
    @Operation(summary = "未读通知数量")
    public UnreadCountResponse unreadCount() {
        return notificationService.getUnreadCount(SessionUtils.getUserId());
    }
}
