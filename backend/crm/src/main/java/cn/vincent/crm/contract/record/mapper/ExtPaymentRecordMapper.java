package cn.vincent.crm.contract.record.mapper;

import cn.vincent.crm.contract.record.domain.PaymentRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 回款记录自定义 Mapper
 */
@Mapper
public interface ExtPaymentRecordMapper {

    /**
     * 根据合同 ID 查询回款记录列表
     *
     * @param contractId 合同 ID
     * @return 回款记录列表
     */
    List<PaymentRecord> selectByContractId(@Param("contractId") String contractId);
}
