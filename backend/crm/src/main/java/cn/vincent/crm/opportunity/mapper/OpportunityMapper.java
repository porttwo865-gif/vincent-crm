package cn.vincent.crm.opportunity.mapper;

import cn.vincent.crm.opportunity.domain.Opportunity;
import cn.vincent.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商机通用 Mapper
 */
@Mapper
public interface OpportunityMapper extends BaseMapper<Opportunity> {

}
