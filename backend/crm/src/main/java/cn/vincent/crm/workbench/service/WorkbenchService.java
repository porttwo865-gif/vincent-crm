package cn.vincent.crm.workbench.service;

import cn.vincent.crm.workbench.dto.response.WorkbenchOverviewResponse;
import cn.vincent.crm.workbench.dto.response.WorkbenchRecentResponse;
import cn.vincent.crm.workbench.dto.response.WorkbenchTodoResponse;
import cn.vincent.crm.workbench.mapper.ExtWorkbenchMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * 工作台服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class WorkbenchService {

    /** 工作台自定义 Mapper */
    @Resource
    private ExtWorkbenchMapper extWorkbenchMapper;

    /** 最近动态最大返回条数 */
    private static final int RECENT_LIMIT = 20;

    /**
     * 获取业绩概览
     *
     * @param userId 当前用户 ID
     * @param orgId  当前组织 ID
     * @return 业绩概览
     */
    public WorkbenchOverviewResponse overview(String userId, String orgId) {
        WorkbenchOverviewResponse response = new WorkbenchOverviewResponse();

        // 本月时间范围
        long[] monthRange = getCurrentMonthRange();
        long monthStart = monthRange[0];
        long monthEnd = monthRange[1];

        // 我的线索总数
        Long clueCount = extWorkbenchMapper.countClueByOwner(orgId, userId);
        response.setClueCount(clueCount != null ? clueCount : 0L);

        // 我的客户总数
        Long customerCount = extWorkbenchMapper.countCustomerByOwner(orgId, userId);
        response.setCustomerCount(customerCount != null ? customerCount : 0L);

        // 我的商机总数
        Long opportunityCount = extWorkbenchMapper.countOpportunityByOwner(orgId, userId);
        response.setOpportunityCount(opportunityCount != null ? opportunityCount : 0L);

        // 我的商机总金额
        BigDecimal opportunityAmount = extWorkbenchMapper.sumOpportunityAmountByOwner(orgId, userId);
        response.setOpportunityAmount(opportunityAmount != null ? opportunityAmount : BigDecimal.ZERO);

        // 本月新签合同数
        Long contractCount = extWorkbenchMapper.countContractThisMonthByOwner(orgId, userId, monthStart, monthEnd);
        response.setContractCount(contractCount != null ? contractCount : 0L);

        // 本月合同总金额
        BigDecimal contractAmount = extWorkbenchMapper.sumContractAmountThisMonthByOwner(orgId, userId, monthStart, monthEnd);
        response.setContractAmount(contractAmount != null ? contractAmount : BigDecimal.ZERO);

        // 本月订单数
        Long orderCount = extWorkbenchMapper.countOrderThisMonthByOwner(orgId, userId, monthStart, monthEnd);
        response.setOrderCount(orderCount != null ? orderCount : 0L);

        // 本月订单总金额
        BigDecimal orderAmount = extWorkbenchMapper.sumOrderAmountThisMonthByOwner(orgId, userId, monthStart, monthEnd);
        response.setOrderAmount(orderAmount != null ? orderAmount : BigDecimal.ZERO);

        return response;
    }

    /**
     * 获取待办事项
     *
     * @param userId 当前用户 ID
     * @param orgId  当前组织 ID
     * @return 待办事项
     */
    public WorkbenchTodoResponse todo(String userId, String orgId) {
        WorkbenchTodoResponse response = new WorkbenchTodoResponse();
        long now = System.currentTimeMillis();
        long expireTime = now + 30L * 24 * 60 * 60 * 1000;

        // 待跟进计划数量
        Long pendingFollowPlans = extWorkbenchMapper.countPendingFollowPlans(orgId, userId);
        response.setPendingFollowPlans(pendingFollowPlans != null ? pendingFollowPlans : 0L);

        // 待我审批数量
        Long pendingApprovals = extWorkbenchMapper.countPendingApprovals(orgId, userId);
        response.setPendingApprovals(pendingApprovals != null ? pendingApprovals : 0L);

        // 即将到期合同数量（30天内）
        Long expiringContracts = extWorkbenchMapper.countExpiringContracts(orgId, userId, now, expireTime);
        response.setExpiringContracts(expiringContracts != null ? expiringContracts : 0L);

        // 逾期回款计划数量
        Long overduePaymentPlans = extWorkbenchMapper.countOverduePaymentPlans(orgId, userId, now);
        response.setOverduePaymentPlans(overduePaymentPlans != null ? overduePaymentPlans : 0L);

        return response;
    }

    /**
     * 获取最近动态
     *
     * @param userId 当前用户 ID
     * @param orgId  当前组织 ID
     * @return 最近动态列表
     */
    public List<WorkbenchRecentResponse> recent(String userId, String orgId) {
        long now = System.currentTimeMillis();
        long sevenDaysAgo = now - 7L * 24 * 60 * 60 * 1000;

        List<WorkbenchRecentResponse> list = extWorkbenchMapper.selectRecentFollowRecords(
                orgId, userId, sevenDaysAgo, RECENT_LIMIT);

        return list != null ? list : Collections.emptyList();
    }

    /**
     * 获取当前月的时间范围
     *
     * @return [月初开始时间戳, 月末结束时间戳]
     */
    private long[] getCurrentMonthRange() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long monthStart = cal.getTimeInMillis();

        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.MILLISECOND, -1);
        long monthEnd = cal.getTimeInMillis();

        return new long[]{monthStart, monthEnd};
    }
}
