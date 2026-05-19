package cn.vincent.crm.approval.mapper;

import cn.vincent.crm.approval.domain.ApprovalInstance;
import cn.vincent.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审批实例通用 Mapper
 */
@Mapper
public interface ApprovalInstanceMapper extends BaseMapper<ApprovalInstance> {

}
