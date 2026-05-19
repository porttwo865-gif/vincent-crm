package cn.vincent.crm.invoice.mapper;

import cn.vincent.crm.invoice.domain.Invoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 发票自定义 Mapper
 */
@Mapper
public interface ExtInvoiceMapper {

    /**
     * 发票分页列表查询
     *
     * @param organizationId 组织 ID
     * @param keyword        搜索关键词
     * @param status         状态
     * @param customerId     关联客户 ID
     * @return 发票列表
     */
    List<Invoice> selectInvoicePage(@Param("organizationId") String organizationId,
                                    @Param("keyword") String keyword,
                                    @Param("status") String status,
                                    @Param("customerId") String customerId);
}
