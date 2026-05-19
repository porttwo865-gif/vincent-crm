package cn.vincent.crm.follow.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.follow.domain.FollowRecord;
import cn.vincent.crm.follow.dto.request.FollowRecordAddRequest;
import cn.vincent.crm.follow.dto.request.FollowRecordListRequest;
import cn.vincent.crm.follow.dto.request.FollowRecordUpdateRequest;
import cn.vincent.crm.follow.dto.response.FollowRecordListResponse;
import cn.vincent.crm.follow.service.FollowRecordService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 跟进记录控制器
 */
@RestController
@RequestMapping("/crm/v1/follow/record")
@Tag(name = "跟进记录管理")
public class FollowRecordController {

    /** 跟进记录服务 */
    @Resource
    private FollowRecordService followRecordService;

    /**
     * 添加跟进记录
     *
     * @param request 添加请求
     * @return 新增的跟进记录实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.FOLLOW_RECORD_ADD)
    @Operation(summary = "添加跟进记录")
    public FollowRecord add(@Validated @RequestBody FollowRecordAddRequest request) {
        return followRecordService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新跟进记录
     *
     * @param request 更新请求
     * @return 更新后的跟进记录实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.FOLLOW_RECORD_UPDATE)
    @Operation(summary = "更新跟进记录")
    public FollowRecord update(@Validated @RequestBody FollowRecordUpdateRequest request) {
        return followRecordService.update(request, SessionUtils.getUserId());
    }

    /**
     * 删除跟进记录
     *
     * @param id 跟进记录 ID
     */
    @PostMapping("/delete")
    @RequiresPermissions(PermissionConstants.FOLLOW_RECORD_DELETE)
    @Operation(summary = "删除跟进记录")
    public void delete(@RequestBody String id) {
        followRecordService.delete(id);
    }

    /**
     * 查询跟进记录列表
     *
     * @param request 列表查询请求
     * @return 跟进记录列表
     */
    @PostMapping("/list")
    @RequiresPermissions(PermissionConstants.FOLLOW_RECORD_READ)
    @Operation(summary = "跟进记录列表")
    public List<FollowRecordListResponse> list(@Validated @RequestBody FollowRecordListRequest request) {
        return followRecordService.list(request.getBizType(), request.getBizId());
    }
}
