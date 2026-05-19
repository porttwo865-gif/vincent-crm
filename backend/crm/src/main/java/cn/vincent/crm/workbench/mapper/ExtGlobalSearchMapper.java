package cn.vincent.crm.workbench.mapper;

import cn.vincent.crm.workbench.dto.response.GlobalSearchClueItem;
import cn.vincent.crm.workbench.dto.response.GlobalSearchContractItem;
import cn.vincent.crm.workbench.dto.response.GlobalSearchCustomerItem;
import cn.vincent.crm.workbench.dto.response.GlobalSearchOpportunityItem;
import cn.vincent.crm.workbench.dto.response.GlobalSearchOrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 全局搜索自定义 Mapper
 */
@Mapper
public interface ExtGlobalSearchMapper {

    /**
     * 搜索线索
     *
     * @param orgId    组织 ID
     * @param ownerIds 数据权限范围内的负责人 ID 列表
     * @param keyword  关键词
     * @param limit    限制条数
     * @return 线索搜索结果列表
     */
    List<GlobalSearchClueItem> searchClues(@Param("orgId") String orgId,
                                            @Param("ownerIds") List<String> ownerIds,
                                            @Param("keyword") String keyword,
                                            @Param("limit") int limit);

    /**
     * 搜索客户
     *
     * @param orgId    组织 ID
     * @param ownerIds 数据权限范围内的负责人 ID 列表
     * @param keyword  关键词
     * @param limit    限制条数
     * @return 客户搜索结果列表
     */
    List<GlobalSearchCustomerItem> searchCustomers(@Param("orgId") String orgId,
                                                    @Param("ownerIds") List<String> ownerIds,
                                                    @Param("keyword") String keyword,
                                                    @Param("limit") int limit);

    /**
     * 搜索商机
     *
     * @param orgId    组织 ID
     * @param ownerIds 数据权限范围内的负责人 ID 列表
     * @param keyword  关键词
     * @param limit    限制条数
     * @return 商机搜索结果列表
     */
    List<GlobalSearchOpportunityItem> searchOpportunities(@Param("orgId") String orgId,
                                                           @Param("ownerIds") List<String> ownerIds,
                                                           @Param("keyword") String keyword,
                                                           @Param("limit") int limit);

    /**
     * 搜索合同
     *
     * @param orgId    组织 ID
     * @param ownerIds 数据权限范围内的负责人 ID 列表
     * @param keyword  关键词
     * @param limit    限制条数
     * @return 合同搜索结果列表
     */
    List<GlobalSearchContractItem> searchContracts(@Param("orgId") String orgId,
                                                    @Param("ownerIds") List<String> ownerIds,
                                                    @Param("keyword") String keyword,
                                                    @Param("limit") int limit);

    /**
     * 搜索订单
     *
     * @param orgId   组织 ID
     * @param keyword 关键词
     * @param limit   限制条数
     * @return 订单搜索结果列表
     */
    List<GlobalSearchOrderItem> searchOrders(@Param("orgId") String orgId,
                                              @Param("keyword") String keyword,
                                              @Param("limit") int limit);
}
