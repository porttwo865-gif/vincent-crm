package cn.vincent.crm.workbench.mapper;

import cn.vincent.crm.workbench.dto.response.WorkbenchRecentResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 工作台自定义 Mapper
 */
@Mapper
public interface ExtWorkbenchMapper {

    // ========== 业绩概览 ==========

    /**
     * 统计我的线索总数
     *
     * @param orgId   组织 ID
     * @param ownerId 负责人 ID
     * @return 线索总数
     */
    Long countClueByOwner(@Param("orgId") String orgId, @Param("ownerId") String ownerId);

    /**
     * 统计我的客户总数
     *
     * @param orgId   组织 ID
     * @param ownerId 负责人 ID
     * @return 客户总数
     */
    Long countCustomerByOwner(@Param("orgId") String orgId, @Param("ownerId") String ownerId);

    /**
     * 统计我的商机总数
     *
     * @param orgId   组织 ID
     * @param ownerId 负责人 ID
     * @return 商机总数
     */
    Long countOpportunityByOwner(@Param("orgId") String orgId, @Param("ownerId") String ownerId);

    /**
     * 统计我的商机总金额
     *
     * @param orgId   组织 ID
     * @param ownerId 负责人 ID
     * @return 商机总金额
     */
    BigDecimal sumOpportunityAmountByOwner(@Param("orgId") String orgId, @Param("ownerId") String ownerId);

    /**
     * 统计本月新签合同数
     *
     * @param orgId      组织 ID
     * @param ownerId    负责人 ID
     * @param monthStart 本月开始时间戳
     * @param monthEnd   本月结束时间戳
     * @return 合同数
     */
    Long countContractThisMonthByOwner(@Param("orgId") String orgId, @Param("ownerId") String ownerId,
                                        @Param("monthStart") Long monthStart, @Param("monthEnd") Long monthEnd);

    /**
     * 统计本月合同总金额
     *
     * @param orgId      组织 ID
     * @param ownerId    负责人 ID
     * @param monthStart 本月开始时间戳
     * @param monthEnd   本月结束时间戳
     * @return 合同总金额
     */
    BigDecimal sumContractAmountThisMonthByOwner(@Param("orgId") String orgId, @Param("ownerId") String ownerId,
                                                  @Param("monthStart") Long monthStart, @Param("monthEnd") Long monthEnd);

    /**
     * 统计本月订单数
     *
     * @param orgId      组织 ID
     * @param ownerId    负责人 ID
     * @param monthStart 本月开始时间戳
     * @param monthEnd   本月结束时间戳
     * @return 订单数
     */
    Long countOrderThisMonthByOwner(@Param("orgId") String orgId, @Param("ownerId") String ownerId,
                                     @Param("monthStart") Long monthStart, @Param("monthEnd") Long monthEnd);

    /**
     * 统计本月订单总金额
     *
     * @param orgId      组织 ID
     * @param ownerId    负责人 ID
     * @param monthStart 本月开始时间戳
     * @param monthEnd   本月结束时间戳
     * @return 订单总金额
     */
    BigDecimal sumOrderAmountThisMonthByOwner(@Param("orgId") String orgId, @Param("ownerId") String ownerId,
                                               @Param("monthStart") Long monthStart, @Param("monthEnd") Long monthEnd);

    // ========== 待办事项 ==========

    /**
     * 统计待跟进计划数量
     *
     * @param orgId   组织 ID
     * @param ownerId 负责人 ID
     * @return 待跟进计划数量
     */
    Long countPendingFollowPlans(@Param("orgId") String orgId, @Param("ownerId") String ownerId);

    /**
     * 统计待我审批数量
     *
     * @param orgId      组织 ID
     * @param approverId 审批人 ID
     * @return 待审批数量
     */
    Long countPendingApprovals(@Param("orgId") String orgId, @Param("approverId") String approverId);

    /**
     * 统计即将到期合同数量（30天内）
     *
     * @param orgId      组织 ID
     * @param ownerId    负责人 ID
     * @param now        当前时间戳
     * @param expireTime 30天后时间戳
     * @return 即将到期合同数量
     */
    Long countExpiringContracts(@Param("orgId") String orgId, @Param("ownerId") String ownerId,
                                 @Param("now") Long now, @Param("expireTime") Long expireTime);

    /**
     * 统计逾期回款计划数量
     *
     * @param orgId   组织 ID
     * @param ownerId 负责人 ID
     * @param now     当前时间戳
     * @return 逾期回款计划数量
     */
    Long countOverduePaymentPlans(@Param("orgId") String orgId, @Param("ownerId") String ownerId,
                                   @Param("now") Long now);

    // ========== 最近动态 ==========

    /**
     * 查询最近跟进记录
     *
     * @param orgId       组织 ID
     * @param userId      当前用户 ID
     * @param sevenDaysAgo 7天前时间戳
     * @param limit       限制条数
     * @return 最近跟进记录列表
     */
    List<WorkbenchRecentResponse> selectRecentFollowRecords(@Param("orgId") String orgId,
                                                             @Param("userId") String userId,
                                                             @Param("sevenDaysAgo") Long sevenDaysAgo,
                                                             @Param("limit") int limit);

    /**
     * 查询最近跟进记录（分页，不带 LIMIT）
     *
     * @param orgId        组织 ID
     * @param userId       当前用户 ID
     * @param sevenDaysAgo 7天前时间戳
     * @return 最近跟进记录列表
     */
    List<WorkbenchRecentResponse> selectFollowRecordsForActivity(@Param("orgId") String orgId,
                                                                  @Param("userId") String userId,
                                                                  @Param("sevenDaysAgo") Long sevenDaysAgo);
}
