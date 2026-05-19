package cn.vincent.crm.product.controller;

import cn.vincent.aspectj.annotation.RequiresPermissions;
import cn.vincent.common.constants.FormKey;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.context.OrganizationContext;
import cn.vincent.crm.product.domain.Product;
import cn.vincent.crm.product.dto.request.ProductAddRequest;
import cn.vincent.crm.product.dto.request.ProductPageRequest;
import cn.vincent.crm.product.dto.request.ProductUpdateRequest;
import cn.vincent.crm.product.dto.response.ProductGetResponse;
import cn.vincent.crm.product.dto.response.ProductListResponse;
import cn.vincent.crm.product.service.ProductService;
import cn.vincent.crm.system.dto.response.ModuleFormConfigDTO;
import cn.vincent.crm.system.service.ModuleFormCacheService;
import cn.vincent.security.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品管理控制器
 */
@RestController
@RequestMapping("/product")
@Tag(name = "产品管理")
public class ProductController {

    /** 产品服务 */
    @Resource
    private ProductService productService;

    /** 模块表单缓存服务 */
    @Resource
    private ModuleFormCacheService moduleFormCacheService;

    /**
     * 获取产品表单配置
     *
     * @return 表单配置
     */
    @GetMapping("/module/form")
    @RequiresPermissions(PermissionConstants.PRODUCT_MANAGEMENT_READ)
    @Operation(summary = "获取产品表单配置")
    public ModuleFormConfigDTO getModuleFormConfig() {
        return moduleFormCacheService.getBusinessFormConfig(
                FormKey.PRODUCT.getKey(), OrganizationContext.getOrganizationId());
    }

    /**
     * 产品列表
     *
     * @param request 分页请求
     * @return 分页结果
     */
    @PostMapping("/page")
    @RequiresPermissions(PermissionConstants.PRODUCT_MANAGEMENT_READ)
    @Operation(summary = "产品列表")
    public PagerWithOption<List<ProductListResponse>> list(@Validated @RequestBody ProductPageRequest request) {
        return productService.list(request, OrganizationContext.getOrganizationId());
    }

    /**
     * 新增产品
     *
     * @param request 新增请求
     * @return 新增的产品实体
     */
    @PostMapping("/add")
    @RequiresPermissions(PermissionConstants.PRODUCT_MANAGEMENT_ADD)
    @Operation(summary = "新增产品")
    public Product add(@Validated @RequestBody ProductAddRequest request) {
        return productService.add(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 更新产品
     *
     * @param request 更新请求
     * @return 更新后的产品实体
     */
    @PostMapping("/update")
    @RequiresPermissions(PermissionConstants.PRODUCT_MANAGEMENT_UPDATE)
    @Operation(summary = "更新产品")
    public Product update(@Validated @RequestBody ProductUpdateRequest request) {
        return productService.update(request, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 删除产品
     *
     * @param id 产品 ID
     */
    @GetMapping("/delete/{id}")
    @RequiresPermissions(PermissionConstants.PRODUCT_MANAGEMENT_DELETE)
    @Operation(summary = "删除产品")
    public void delete(@PathVariable String id) {
        productService.delete(id);
    }

    /**
     * 产品详情
     *
     * @param id 产品 ID
     * @return 产品详情
     */
    @GetMapping("/get/{id}")
    @RequiresPermissions(PermissionConstants.PRODUCT_MANAGEMENT_READ)
    @Operation(summary = "产品详情")
    public ProductGetResponse get(@PathVariable String id) {
        return productService.getWithDataPermissionCheck(
                id, SessionUtils.getUserId(), OrganizationContext.getOrganizationId());
    }

    /**
     * 启用产品
     *
     * @param id 产品 ID
     */
    @GetMapping("/enable/{id}")
    @RequiresPermissions(PermissionConstants.PRODUCT_MANAGEMENT_UPDATE)
    @Operation(summary = "启用产品")
    public void enable(@PathVariable String id) {
        productService.enable(id);
    }

    /**
     * 禁用产品
     *
     * @param id 产品 ID
     */
    @GetMapping("/disable/{id}")
    @RequiresPermissions(PermissionConstants.PRODUCT_MANAGEMENT_UPDATE)
    @Operation(summary = "禁用产品")
    public void disable(@PathVariable String id) {
        productService.disable(id);
    }
}
