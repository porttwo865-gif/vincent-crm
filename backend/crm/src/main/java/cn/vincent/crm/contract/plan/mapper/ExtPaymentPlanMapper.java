package cn.vincent.crm.contract.plan.mapper;

import cn.vincent.crm.contract.plan.domain.PaymentPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 回款计划自定义 Mapper
 */
@Mapper
public interface ExtPaymentPlanMapper {

    /**
     * 根据合同 ID 查询回款计划列表
     *
     * @param contractId 合同 ID
     * @return 回款计划列表
     */
    List<PaymentPlan> selectByContractId(@Param("contractId") String contractId);
}
