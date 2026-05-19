package cn.vincent.crm.customer.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.customer.domain.CustomerContact;
import cn.vincent.crm.customer.dto.request.ContactAddRequest;
import cn.vincent.crm.customer.dto.request.ContactUpdateRequest;
import cn.vincent.crm.customer.dto.response.ContactListResponse;
import cn.vincent.crm.customer.service.ContactService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 联系人管理控制器
 */
@RestController
@RequestMapping("/contact")
@Tag(name = "联系人管理")
public class ContactController {

    /** 联系人服务 */
    @Resource
    private ContactService contactService;

    /**
     * 联系人列表
     *
     * @param customerId 客户 ID
     * @return 联系人列表
     */
    @GetMapping("/list/{customerId}")
    @RequiresPermissions(PermissionConstants.CUSTOMER_MANAGEMENT_READ)
    @Operation(summary = "联系人列表")
    public List<ContactListResponse> list(@PathVariable String customerId) {
        return contactService.listByCustomerId(customerId);
    }

    /**
     * 新增联系人
     *
     * @param request 新增请求
     * @return 新增的联系人实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.CUSTOMER_MANAGEMENT_UPDATE)
    @Operation(summary = "新增联系人")
    public CustomerContact add(@Validated @RequestBody ContactAddRequest request) {
        return contactService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新联系人
     *
     * @param request 更新请求
     * @return 更新后的联系人实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.CUSTOMER_MANAGEMENT_UPDATE)
    @Operation(summary = "更新联系人")
    public CustomerContact update(@Validated @RequestBody ContactUpdateRequest request) {
        return contactService.update(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 删除联系人
     *
     * @param id 联系人 ID
     */
    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.CUSTOMER_MANAGEMENT_UPDATE)
    @Operation(summary = "删除联系人")
    public void delete(@PathVariable String id) {
        contactService.delete(id);
    }
}
