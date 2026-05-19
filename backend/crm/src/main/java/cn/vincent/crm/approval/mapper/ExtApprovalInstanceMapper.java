package cn.vincent.crm.approval.mapper;

import cn.vincent.crm.approval.domain.ApprovalInstance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 审批实例自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtApprovalInstanceMapper {

    /**
     * 我发起的审批实例分页查询
     *
     * @param applicant      申请人 ID
     * @param organizationId 组织 ID
     * @param bizType        业务类型
     * @param status         状态
     * @param keyword        搜索关键词
     * @return 审批实例列表
     */
    List<ApprovalInstance> selectMinePage(@Param("applicant") String applicant,
                                           @Param("organizationId") String organizationId,
                                           @Param("bizType") String bizType,
                                           @Param("status") String status,
                                           @Param("keyword") String keyword);

    /**
     * 待我审批的实例分页查询（通过子查询关联节点记录表）
     *
     * @param approverId     审批人 ID
     * @param organizationId 组织 ID
     * @param bizType        业务类型
     * @param keyword        搜索关键词
     * @return 审批实例列表
     */
    List<ApprovalInstance> selectPendingPage(@Param("approverId") String approverId,
                                              @Param("organizationId") String organizationId,
                                              @Param("bizType") String bizType,
                                              @Param("keyword") String keyword);

    /**
     * 全部审批实例分页查询
     *
     * @param organizationId 组织 ID
     * @param bizType        业务类型
     * @param status         状态
     * @param keyword        搜索关键词
     * @return 审批实例列表
     */
    List<ApprovalInstance> selectAllPage(@Param("organizationId") String organizationId,
                                          @Param("bizType") String bizType,
                                          @Param("status") String status,
                                          @Param("keyword") String keyword);
}
