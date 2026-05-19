package cn.vincent.crm.approval.mapper;

import cn.vincent.crm.approval.domain.ApprovalNodeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 审批节点记录自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtApprovalNodeRecordMapper {

    /**
     * 根据实例 ID 查询节点记录列表（按序号排序）
     *
     * @param instanceId 实例 ID
     * @return 节点记录列表
     */
    List<ApprovalNodeRecord> selectByInstanceId(@Param("instanceId") String instanceId);

    /**
     * 根据实例 ID 和节点序号查询待审批的节点记录
     *
     * @param instanceId 实例 ID
     * @param nodeSeq    节点序号
     * @return 节点记录列表
     */
    List<ApprovalNodeRecord> selectPendingByInstanceAndSeq(@Param("instanceId") String instanceId,
                                                            @Param("nodeSeq") Integer nodeSeq);
}
