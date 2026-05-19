package cn.vincent.crm.follow.service;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.follow.domain.FollowPlan;
import cn.vincent.crm.follow.dto.request.FollowPlanAddRequest;
import cn.vincent.crm.follow.dto.request.FollowPlanUpdateRequest;
import cn.vincent.crm.follow.dto.response.FollowPlanListResponse;
import cn.vincent.crm.follow.mapper.ExtFollowPlanMapper;
import cn.vincent.crm.follow.mapper.FollowPlanMapper;
import cn.vincent.crm.system.service.BaseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 跟进计划服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class FollowPlanService {

    /** 跟进计划通用 Mapper */
    @Resource
    private FollowPlanMapper followPlanMapper;

    /** 跟进计划自定义 Mapper */
    @Resource
    private ExtFollowPlanMapper extFollowPlanMapper;

    /** 通用基础服务 */
    @Resource
    private BaseService baseService;

    /**
     * 添加跟进计划
     *
     * @param request 添加请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 新增的跟进计划实体
     */
    public FollowPlan add(FollowPlanAddRequest request, String userId, String orgId) {
        FollowPlan plan = new FollowPlan();
        plan.setId(IDGenerator.nextStr());
        plan.setBizType(request.getBizType());
        plan.setBizId(request.getBizId());
        plan.setPlanTime(request.getPlanTime());
        plan.setContent(request.getContent());
        plan.setRemindBefore(request.getRemindBefore());
        plan.setStatus("pending");
        plan.setOwner(userId);
        plan.setOrganizationId(orgId);
        plan.setCreateUser(userId);
        plan.setUpdateUser(userId);
        plan.setCreateTime(System.currentTimeMillis());
        plan.setUpdateTime(System.currentTimeMillis());
        followPlanMapper.insert(plan);
        return plan;
    }

    /**
     * 更新跟进计划
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @return 更新后的跟进计划实体
     */
    public FollowPlan update(FollowPlanUpdateRequest request, String userId) {
        FollowPlan plan = followPlanMapper.selectByPrimaryKey(request.getId());
        if (plan == null) {
            throw new GenericException(Translator.get("follow.plan.not.exist"));
        }

        if (request.getPlanTime() != null) {
            plan.setPlanTime(request.getPlanTime());
        }
        if (request.getContent() != null) {
            plan.setContent(request.getContent());
        }
        if (request.getRemindBefore() != null) {
            plan.setRemindBefore(request.getRemindBefore());
        }
        plan.setUpdateUser(userId);
        plan.setUpdateTime(System.currentTimeMillis());
        followPlanMapper.update(plan);
        return plan;
    }

    /**
     * 删除跟进计划
     *
     * @param id 跟进计划 ID
     */
    public void delete(String id) {
        FollowPlan plan = followPlanMapper.selectByPrimaryKey(id);
        if (plan == null) {
            throw new GenericException(Translator.get("follow.plan.not.exist"));
        }
        followPlanMapper.deleteByIds(List.of(id));
    }

    /**
     * 标记跟进计划为完成
     *
     * @param id     跟进计划 ID
     * @param userId 当前用户 ID
     */
    public void done(String id, String userId) {
        FollowPlan plan = followPlanMapper.selectByPrimaryKey(id);
        if (plan == null) {
            throw new GenericException(Translator.get("follow.plan.not.exist"));
        }
        plan.setStatus("done");
        plan.setUpdateUser(userId);
        plan.setUpdateTime(System.currentTimeMillis());
        followPlanMapper.update(plan);
    }

    /**
     * 根据业务类型和业务对象 ID 查询跟进计划列表
     *
     * @param bizType 业务类型
     * @param bizId   业务对象 ID
     * @return 跟进计划列表响应
     */
    public List<FollowPlanListResponse> list(String bizType, String bizId) {
        List<FollowPlan> plans = extFollowPlanMapper.selectByBiz(bizType, bizId);
        if (plans == null || plans.isEmpty()) {
            return new ArrayList<>();
        }

        List<FollowPlanListResponse> responseList = new ArrayList<>();
        for (FollowPlan plan : plans) {
            FollowPlanListResponse response = BeanUtils.copyBean(new FollowPlanListResponse(), plan);
            responseList.add(response);
        }

        // 批量设置创建人/更新人/负责人姓名
        baseService.setCreateAndUpdateUserName(responseList);
        return responseList;
    }

    /**
     * 查询我的待办跟进计划（用于工作台）
     *
     * @param userId 当前用户 ID
     * @param orgId  当前组织 ID
     * @return 跟进计划列表响应
     */
    public List<FollowPlanListResponse> myPending(String userId, String orgId) {
        List<FollowPlan> plans = extFollowPlanMapper.selectMyPending(userId, orgId);
        if (plans == null || plans.isEmpty()) {
            return new ArrayList<>();
        }

        List<FollowPlanListResponse> responseList = new ArrayList<>();
        for (FollowPlan plan : plans) {
            FollowPlanListResponse response = BeanUtils.copyBean(new FollowPlanListResponse(), plan);
            responseList.add(response);
        }

        // 批量设置创建人/更新人/负责人姓名
        baseService.setCreateAndUpdateUserName(responseList);
        return responseList;
    }
}
