package cn.vincent.crm.contract.plan.mapper;

import cn.vincent.crm.contract.plan.domain.PaymentPlan;
import cn.vincent.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 回款计划通用 Mapper
 */
@Mapper
public interface PaymentPlanMapper extends BaseMapper<PaymentPlan> {
}
