package cn.vincent.crm.product.mapper;

import cn.vincent.crm.product.domain.Product;
import cn.vincent.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 产品通用 Mapper
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}
