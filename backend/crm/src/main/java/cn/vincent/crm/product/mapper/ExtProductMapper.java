package cn.vincent.crm.product.mapper;

import cn.vincent.crm.product.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtProductMapper {

    /**
     * 根据编码查询产品（用于编码唯一性校验）
     *
     * @param code          产品编码
     * @param organizationId 组织 ID
     * @return 产品列表
     */
    List<Product> selectByCode(@Param("code") String code,
                               @Param("organizationId") String organizationId);

    /**
     * 产品分页列表查询（关键词搜索 + 分类/状态过滤）
     *
     * @param organizationId 组织 ID
     * @param keyword        搜索关键词
     * @param category       分类
     * @param enable         是否启用
     * @return 产品列表
     */
    List<Product> selectProductPage(@Param("organizationId") String organizationId,
                                    @Param("keyword") String keyword,
                                    @Param("category") String category,
                                    @Param("enable") Boolean enable);
}
