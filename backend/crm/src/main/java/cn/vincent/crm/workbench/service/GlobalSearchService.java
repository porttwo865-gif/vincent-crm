package cn.vincent.crm.workbench.service;

import cn.vincent.crm.system.service.BaseService;
import cn.vincent.crm.workbench.dto.request.GlobalSearchRequest;
import cn.vincent.crm.workbench.dto.response.GlobalSearchClueItem;
import cn.vincent.crm.workbench.dto.response.GlobalSearchContractItem;
import cn.vincent.crm.workbench.dto.response.GlobalSearchCustomerItem;
import cn.vincent.crm.workbench.dto.response.GlobalSearchOpportunityItem;
import cn.vincent.crm.workbench.dto.response.GlobalSearchOrderItem;
import cn.vincent.crm.workbench.dto.response.GlobalSearchResponse;
import cn.vincent.crm.workbench.mapper.ExtGlobalSearchMapper;
import cn.vincent.security.DataScopeService;
import cn.vincent.security.dto.DeptDataPermissionDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 全局搜索服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class GlobalSearchService {

    /** 全局搜索自定义 Mapper */
    @Resource
    private ExtGlobalSearchMapper extGlobalSearchMapper;

    /** 数据权限服务 */
    @Resource
    private DataScopeService dataScopeService;

    /** 基础服务 */
    @Resource
    private BaseService baseService;

    /** 每种类型最大返回条数 */
    private static final int SEARCH_LIMIT = 10;

    /**
     * 全局搜索
     *
     * @param request 搜索请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 搜索结果
     */
    public GlobalSearchResponse search(GlobalSearchRequest request, String userId, String orgId) {
        GlobalSearchResponse response = new GlobalSearchResponse();
        String keyword = request.getKeyword();
        if (keyword == null) {
            keyword = "";
        }

        // 获取数据权限范围
        DeptDataPermissionDTO deptDataPermission = dataScopeService.getDeptDataPermission(
                userId, orgId, null, null);
        List<String> ownerIds = null;
        if (deptDataPermission != null && !deptDataPermission.isAll()) {
            ownerIds = deptDataPermission.getUserIds();
        }

        List<String> types = request.getTypes();
        boolean searchAll = CollectionUtils.isEmpty(types);

        // 搜索线索
        if (searchAll || types.contains("CLUE")) {
            List<GlobalSearchClueItem> clues = extGlobalSearchMapper.searchClues(
                    orgId, ownerIds, keyword, SEARCH_LIMIT);
            for (GlobalSearchClueItem item : clues) {
                baseService.setCreateUpdateOwnerUserName(item);
            }
            response.setClues(clues);
        } else {
            response.setClues(Collections.emptyList());
        }

        // 搜索客户
        if (searchAll || types.contains("CUSTOMER")) {
            List<GlobalSearchCustomerItem> customers = extGlobalSearchMapper.searchCustomers(
                    orgId, ownerIds, keyword, SEARCH_LIMIT);
            for (GlobalSearchCustomerItem item : customers) {
                baseService.setCreateUpdateOwnerUserName(item);
            }
            response.setCustomers(customers);
        } else {
            response.setCustomers(Collections.emptyList());
        }

        // 搜索商机
        if (searchAll || types.contains("OPPORTUNITY")) {
            List<GlobalSearchOpportunityItem> opportunities = extGlobalSearchMapper.searchOpportunities(
                    orgId, ownerIds, keyword, SEARCH_LIMIT);
            response.setOpportunities(opportunities);
        } else {
            response.setOpportunities(Collections.emptyList());
        }

        // 搜索合同
        if (searchAll || types.contains("CONTRACT")) {
            List<GlobalSearchContractItem> contracts = extGlobalSearchMapper.searchContracts(
                    orgId, ownerIds, keyword, SEARCH_LIMIT);
            response.setContracts(contracts);
        } else {
            response.setContracts(Collections.emptyList());
        }

        // 搜索订单
        if (searchAll || types.contains("ORDER")) {
            List<GlobalSearchOrderItem> orders = extGlobalSearchMapper.searchOrders(
                    orgId, keyword, SEARCH_LIMIT);
            response.setOrders(orders);
        } else {
            response.setOrders(Collections.emptyList());
        }

        return response;
    }
}
