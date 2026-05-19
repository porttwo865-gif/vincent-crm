package cn.vincent.crm.contract.mapper;

import cn.vincent.crm.contract.domain.Contract;
import cn.vincent.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 合同通用 Mapper
 */
@Mapper
public interface ContractMapper extends BaseMapper<Contract> {
}
