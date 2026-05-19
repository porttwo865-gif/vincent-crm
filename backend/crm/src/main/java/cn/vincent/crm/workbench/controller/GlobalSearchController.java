package cn.vincent.crm.workbench.controller;

import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.workbench.dto.request.GlobalSearchRequest;
import cn.vincent.crm.workbench.dto.response.GlobalSearchResponse;
import cn.vincent.crm.workbench.service.GlobalSearchService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局搜索控制器
 */
@RestController
@RequestMapping("/crm/v1/search")
@Tag(name = "全局搜索")
public class GlobalSearchController {

    /** 全局搜索服务 */
    @Resource
    private GlobalSearchService globalSearchService;

    /**
     * 全局搜索
     *
     * @param request 搜索请求
     * @return 搜索结果
     */
    @PostMapping("/global")
    @Operation(summary = "全局搜索")
    public GlobalSearchResponse globalSearch(@Validated @RequestBody GlobalSearchRequest request) {
        return globalSearchService.search(
                request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }
}
