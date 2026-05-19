package cn.vincent.crm.product.service;

import cn.vincent.common.constants.FormKey;
import cn.vincent.common.exception.GenericException;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.product.domain.Product;
import cn.vincent.crm.product.dto.request.ProductAddRequest;
import cn.vincent.crm.product.dto.request.ProductPageRequest;
import cn.vincent.crm.product.dto.request.ProductUpdateRequest;
import cn.vincent.crm.product.dto.response.ProductGetResponse;
import cn.vincent.crm.product.dto.response.ProductListResponse;
import cn.vincent.crm.product.mapper.ExtProductMapper;
import cn.vincent.crm.product.mapper.ProductMapper;
import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import cn.vincent.crm.system.service.BaseService;
import cn.vincent.crm.system.service.ModuleFieldValueService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 产品管理服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ProductService {

    /** 产品通用 Mapper */
    @Resource
    private ProductMapper productMapper;

    /** 产品自定义 Mapper */
    @Resource
    private ExtProductMapper extProductMapper;

    /** 通用基础服务 */
    @Resource
    private BaseService baseService;

    /** 自定义字段值服务 */
    @Resource
    private ModuleFieldValueService moduleFieldValueService;

    /**
     * 新增产品
     *
     * @param request 新增请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 新增的产品实体
     */
    public Product add(ProductAddRequest request, String userId, String orgId) {
        // 校验产品编码唯一性
        if (StringUtils.isNotBlank(request.getCode())) {
            List<Product> existing = extProductMapper.selectByCode(request.getCode(), orgId);
            if (existing != null && !existing.isEmpty()) {
                throw new GenericException(Translator.get("product.code.duplicate"));
            }
        }

        Product product = new Product();
        product.setId(IDGenerator.nextStr());
        product.setName(request.getName());
        product.setCode(request.getCode());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setUnit(request.getUnit());
        product.setDescription(request.getDescription());
        product.setEnable(true);
        product.setSort(0);
        product.setOrganizationId(orgId);
        product.setCreateUser(userId);
        product.setUpdateUser(userId);
        product.setCreateTime(System.currentTimeMillis());
        product.setUpdateTime(System.currentTimeMillis());
        productMapper.insert(product);

        // 保存自定义字段值
        moduleFieldValueService.saveFieldValues(
                FormKey.PRODUCT.getKey(), product.getId(), request.getModuleFields(), userId);

        return product;
    }

    /**
     * 更新产品
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 更新后的产品实体
     */
    public Product update(ProductUpdateRequest request, String userId, String orgId) {
        Product product = productMapper.selectByPrimaryKey(request.getId());
        if (product == null) {
            throw new GenericException(Translator.get("product.not.exist"));
        }

        // 校验产品编码唯一性（排除自身）
        if (StringUtils.isNotBlank(request.getCode())) {
            List<Product> existing = extProductMapper.selectByCode(request.getCode(), orgId);
            if (existing != null && existing.stream().anyMatch(p -> !p.getId().equals(request.getId()))) {
                throw new GenericException(Translator.get("product.code.duplicate"));
            }
        }

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getCode() != null) {
            product.setCode(request.getCode());
        }
        if (request.getCategory() != null) {
            product.setCategory(request.getCategory());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getUnit() != null) {
            product.setUnit(request.getUnit());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        product.setUpdateUser(userId);
        product.setUpdateTime(System.currentTimeMillis());
        productMapper.update(product);

        // 保存自定义字段值
        moduleFieldValueService.saveFieldValues(
                FormKey.PRODUCT.getKey(), product.getId(), request.getModuleFields(), userId);

        return product;
    }

    /**
     * 删除产品
     *
     * @param id 产品 ID
     */
    public void delete(String id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null) {
            throw new GenericException(Translator.get("product.not.exist"));
        }

        // 删除自定义字段值
        moduleFieldValueService.deleteFieldValues(FormKey.PRODUCT.getKey(), id);

        // 删除产品
        productMapper.deleteByIds(List.of(id));
    }

    /**
     * 产品列表（分页）
     *
     * @param request 分页请求
     * @param orgId   当前组织 ID
     * @return 分页结果
     */
    public PagerWithOption<List<ProductListResponse>> list(ProductPageRequest request, String orgId) {
        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        List<Product> products = extProductMapper.selectProductPage(
                orgId, request.getKeyword(), request.getCategory(), request.getEnable());
        PageInfo<Product> pageInfo = new PageInfo<>(products);

        // 批量查询自定义字段值
        List<String> productIds = products.stream().map(Product::getId).toList();
        Map<String, List<ModuleFieldValueDTO>> fieldValuesMap = Collections.emptyMap();
        if (!productIds.isEmpty()) {
            fieldValuesMap = moduleFieldValueService.batchGetFieldValues(
                    FormKey.PRODUCT.getKey(), productIds);
        }

        Map<String, List<ModuleFieldValueDTO>> finalFieldValuesMap = fieldValuesMap;

        // 转换为响应 DTO
        List<ProductListResponse> responseList = products.stream()
                .map(product -> {
                    ProductListResponse response = BeanUtils.copyBean(new ProductListResponse(), product);
                    // 设置自定义字段值
                    response.setModuleFields(
                            finalFieldValuesMap.getOrDefault(product.getId(), Collections.emptyList()));
                    return response;
                })
                .toList();

        return PagerWithOption.of(responseList, pageInfo.getTotal(),
                request.getCurrent(), request.getPageSize());
    }

    /**
     * 产品详情（含数据权限校验）
     *
     * @param id     产品 ID
     * @param userId 当前用户 ID
     * @param orgId  当前组织 ID
     * @return 产品详情响应
     */
    public ProductGetResponse getWithDataPermissionCheck(String id, String userId, String orgId) {
        ProductGetResponse response = get(id);
        if (response == null) {
            throw new GenericException(Translator.get("product.not.exist"));
        }
        return response;
    }

    /**
     * 产品详情
     *
     * @param id 产品 ID
     * @return 产品详情响应
     */
    public ProductGetResponse get(String id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null) {
            return null;
        }

        ProductGetResponse response = BeanUtils.copyBean(new ProductGetResponse(), product);

        // 查询自定义字段值
        List<ModuleFieldValueDTO> fieldValues = moduleFieldValueService.getFieldValues(
                FormKey.PRODUCT.getKey(), id);
        response.setModuleFields(fieldValues);

        // 设置创建人/更新人姓名
        baseService.setCreateUpdateOwnerUserName(response);

        return response;
    }

    /**
     * 启用产品
     *
     * @param id 产品 ID
     */
    public void enable(String id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null) {
            throw new GenericException(Translator.get("product.not.exist"));
        }
        product.setEnable(true);
        product.setUpdateTime(System.currentTimeMillis());
        productMapper.update(product);
    }

    /**
     * 禁用产品
     *
     * @param id 产品 ID
     */
    public void disable(String id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null) {
            throw new GenericException(Translator.get("product.not.exist"));
        }
        product.setEnable(false);
        product.setUpdateTime(System.currentTimeMillis());
        productMapper.update(product);
    }
}
