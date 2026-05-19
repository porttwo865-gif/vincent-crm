package cn.vincent.crm.invoice.mapper;

import cn.vincent.crm.invoice.domain.Invoice;
import cn.vincent.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 发票通用 Mapper
 */
@Mapper
public interface InvoiceMapper extends BaseMapper<Invoice> {
}
