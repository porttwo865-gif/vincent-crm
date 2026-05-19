package cn.vincent.crm.contract.plan.service;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.contract.plan.domain.PaymentPlan;
import cn.vincent.crm.contract.plan.dto.request.PaymentPlanAddRequest;
import cn.vincent.crm.contract.plan.dto.request.PaymentPlanUpdateRequest;
import cn.vincent.crm.contract.plan.dto.response.PaymentPlanListResponse;
import cn.vincent.crm.contract.plan.mapper.ExtPaymentPlanMapper;
import cn.vincent.crm.contract.plan.mapper.PaymentPlanMapper;
import cn.vincent.crm.system.service.BaseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 回款计划服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PaymentPlanService {

    @Resource
    private PaymentPlanMapper paymentPlanMapper;

    @Resource
    private ExtPaymentPlanMapper extPaymentPlanMapper;

    @Resource
    private BaseService baseService;

    /**
     * 新增回款计划
     *
     * @param request 新增请求
     * @param userId  当前用户 ID
     * @return 新增的回款计划实体
     */
    public PaymentPlan add(PaymentPlanAddRequest request, String userId) {
        PaymentPlan plan = new PaymentPlan();
        plan.setId(IDGenerator.nextStr());
        plan.setContractId(request.getContractId());
        plan.setPlanNum(request.getPlanNum());
        plan.setAmount(request.getAmount());
        plan.setExpectedDate(request.getExpectedDate());
        plan.setActualDate(request.getActualDate());
        plan.setStatus(request.getStatus());
        plan.setRemark(request.getRemark());
        plan.setCreateUser(userId);
        plan.setUpdateUser(userId);
        plan.setCreateTime(System.currentTimeMillis());
        plan.setUpdateTime(System.currentTimeMillis());
        paymentPlanMapper.insert(plan);
        return plan;
    }

    /**
     * 更新回款计划
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @return 更新后的回款计划实体
     */
    public PaymentPlan update(PaymentPlanUpdateRequest request, String userId) {
        PaymentPlan plan = paymentPlanMapper.selectByPrimaryKey(request.getId());
        if (plan == null) {
            throw new GenericException(Translator.get("payment.plan.not.exist"));
        }

        if (request.getPlanNum() != null) {
            plan.setPlanNum(request.getPlanNum());
        }
        if (request.getAmount() != null) {
            plan.setAmount(request.getAmount());
        }
        if (request.getExpectedDate() != null) {
            plan.setExpectedDate(request.getExpectedDate());
        }
        if (request.getActualDate() != null) {
            plan.setActualDate(request.getActualDate());
        }
        if (request.getStatus() != null) {
            plan.setStatus(request.getStatus());
        }
        if (request.getRemark() != null) {
            plan.setRemark(request.getRemark());
        }
        plan.setUpdateUser(userId);
        plan.setUpdateTime(System.currentTimeMillis());
        paymentPlanMapper.update(plan);
        return plan;
    }

    /**
     * 删除回款计划
     *
     * @param id 回款计划 ID
     */
    public void delete(String id) {
        PaymentPlan plan = paymentPlanMapper.selectByPrimaryKey(id);
        if (plan == null) {
            throw new GenericException(Translator.get("payment.plan.not.exist"));
        }
        paymentPlanMapper.deleteByIds(List.of(id));
    }

    /**
     * 根据合同 ID 查询回款计划列表
     *
     * @param contractId 合同 ID
     * @return 回款计划列表
     */
    public List<PaymentPlanListResponse> listByContractId(String contractId) {
        List<PaymentPlan> plans = extPaymentPlanMapper.selectByContractId(contractId);
        List<PaymentPlanListResponse> responseList = plans.stream()
                .map(plan -> BeanUtils.copyBean(new PaymentPlanListResponse(), plan))
                .toList();
        return baseService.setCreateAndUpdateUserName(responseList);
    }
}
