package cn.vincent.crm.follow.mapper;

import cn.vincent.crm.follow.domain.FollowPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 跟进计划自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtFollowPlanMapper {

    /**
     * 根据业务类型和业务对象 ID 查询跟进计划列表
     *
     * @param bizType 业务类型
     * @param bizId   业务对象 ID
     * @return 跟进计划列表
     */
    List<FollowPlan> selectByBiz(@Param("bizType") String bizType, @Param("bizId") String bizId);

    /**
     * 查询指定用户的待办跟进计划（用于工作台）
     *
     * @param owner           负责人 ID
     * @param organizationId  组织 ID
     * @return 跟进计划列表
     */
    List<FollowPlan> selectMyPending(@Param("owner") String owner, @Param("organizationId") String organizationId);
}
