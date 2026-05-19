package cn.vincent.crm.contract.mapper;

import cn.vincent.crm.contract.domain.Contract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 合同自定义 Mapper
 */
@Mapper
public interface ExtContractMapper {

    /**
     * 合同分页列表查询
     *
     * @param organizationId 组织 ID
     * @param keyword        搜索关键词
     * @param status         状态
     * @param customerId     关联客户 ID
     * @return 合同列表
     */
    List<Contract> selectContractPage(@Param("organizationId") String organizationId,
                                      @Param("keyword") String keyword,
                                      @Param("status") String status,
                                      @Param("customerId") String customerId);
}
