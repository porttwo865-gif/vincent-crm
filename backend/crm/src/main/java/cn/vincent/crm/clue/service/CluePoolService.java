package cn.vincent.crm.clue.service;

import cn.vincent.common.constants.FormKey;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.ConditionFilterUtils;
import cn.vincent.crm.clue.domain.Clue;
import cn.vincent.crm.clue.dto.request.CluePageRequest;
import cn.vincent.crm.clue.dto.request.CluePoolAssignRequest;
import cn.vincent.crm.clue.dto.request.CluePoolClaimRequest;
import cn.vincent.crm.clue.dto.response.ClueListResponse;
import cn.vincent.crm.clue.mapper.ExtClueMapper;
import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import cn.vincent.crm.system.service.BaseService;
import cn.vincent.crm.system.service.ModuleFieldValueService;
import cn.vincent.security.DataScopeService;
import cn.vincent.security.dto.DeptDataPermissionDTO;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 线索池服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CluePoolService {

    /** 线索自定义 Mapper */
    @Resource
    private ExtClueMapper extClueMapper;

    /** 线索服务 */
    @Resource
    private ClueService clueService;

    /** 数据权限服务 */
    @Resource
    private DataScopeService dataScopeService;

    /** 通用基础服务 */
    @Resource
    private BaseService baseService;

    /** 自定义字段值服务 */
    @Resource
    private ModuleFieldValueService moduleFieldValueService;

    /**
     * 线索池分页列表
     *
     * @param request            分页请求
     * @param orgId              当前组织 ID
     * @param deptDataPermission 部门数据权限
     * @return 分页响应
     */
    public PagerWithOption<List<ClueListResponse>> list(CluePageRequest request, String orgId,
                                                         DeptDataPermissionDTO deptDataPermission) {
        // 解析条件筛选
        ConditionFilterUtils.parseCondition(request, FormKey.CLUE.getKey());

        // 线索池固定查询 inSharedPool=true
        List<String> ownerIds = null;
        if (deptDataPermission != null && !deptDataPermission.isAll()) {
            ownerIds = deptDataPermission.getUserIds();
        }

        // PageHelper 分页
        int current = request.getCurrent() != null ? request.getCurrent() : 1;
        int pageSize = request.getPageSize() != null ? request.getPageSize() : 20;
        PageHelper.startPage(current, pageSize);

        List<Clue> clues = extClueMapper.selectByCondition(
                orgId, true, ownerIds, request.getKeyword());

        // 转换为分页结果
        com.github.pagehelper.Page<Clue> page = (com.github.pagehelper.Page<Clue>) clues;
        List<ClueListResponse> responseList = convertToListResponse(clues);

        return PagerWithOption.of(responseList, page.getTotal(), current, pageSize);
    }

    /**
     * 从线索池领取线索
     *
     * @param request 领取请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     */
    public void claim(CluePoolClaimRequest request, String userId, String orgId) {
        clueService.claimFromPool(request.getClueId(), userId, orgId);
    }

    /**
     * 从线索池分配线索
     *
     * @param request 分配请求
     * @param userId  当前用户 ID（操作人）
     * @param orgId   当前组织 ID
     */
    public void assign(CluePoolAssignRequest request, String userId, String orgId) {
        clueService.assignFromPool(request.getClueId(), request.getToOwner(), userId, orgId);
    }

    // ========== 私有方法 ==========

    /**
     * 将列表转换为 ClueListResponse
     *
     * @param clues 线索列表
     * @return 响应列表
     */
    private List<ClueListResponse> convertToListResponse(List<Clue> clues) {
        if (clues == null || clues.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> clueIds = clues.stream().map(Clue::getId).toList();

        // 批量获取自定义字段值
        Map<String, List<ModuleFieldValueDTO>> fieldValuesMap =
                moduleFieldValueService.batchGetFieldValues(FormKey.CLUE.getKey(), clueIds);

        List<ClueListResponse> responseList = new ArrayList<>();
        for (Clue clue : clues) {
            ClueListResponse response = BeanUtils.copyBean(new ClueListResponse(), clue);
            response.setModuleFields(fieldValuesMap.getOrDefault(clue.getId(), new ArrayList<>()));
            responseList.add(response);
        }

        // 批量设置创建人/更新人姓名
        baseService.setCreateAndUpdateUserName(responseList);

        return responseList;
    }
}
