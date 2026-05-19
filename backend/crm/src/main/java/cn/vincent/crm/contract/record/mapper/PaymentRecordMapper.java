package cn.vincent.crm.contract.record.mapper;

import cn.vincent.crm.contract.record.domain.PaymentRecord;
import cn.vincent.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 回款记录通用 Mapper
 */
@Mapper
public interface PaymentRecordMapper extends BaseMapper<PaymentRecord> {
}
