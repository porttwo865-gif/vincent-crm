package cn.vincent.crm.approval.mapper;

import cn.vincent.crm.approval.domain.ApprovalTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 审批模板自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtApprovalTemplateMapper {

    /**
     * 审批模板分页列表查询
     *
     * @param organizationId 组织 ID
     * @param keyword        搜索关键词
     * @param bizType        业务类型
     * @param enabled        是否启用
     * @return 审批模板列表
     */
    List<ApprovalTemplate> selectTemplatePage(@Param("organizationId") String organizationId,
                                               @Param("keyword") String keyword,
                                               @Param("bizType") String bizType,
                                               @Param("enabled") Boolean enabled);

    /**
     * 根据业务类型查询启用的审批模板
     *
     * @param bizType        业务类型
     * @param organizationId 组织 ID
     * @return 启用的审批模板列表
     */
    List<ApprovalTemplate> selectEnabledByBizType(@Param("bizType") String bizType,
                                                   @Param("organizationId") String organizationId);
}
