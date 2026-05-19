package cn.vincent.crm.contract.service;

import cn.vincent.common.constants.FormKey;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.ConditionFilterUtils;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.contract.domain.Contract;
import cn.vincent.crm.contract.dto.request.ContractAddRequest;
import cn.vincent.crm.contract.dto.request.ContractPageRequest;
import cn.vincent.crm.contract.dto.request.ContractStatusRequest;
import cn.vincent.crm.contract.dto.request.ContractUpdateRequest;
import cn.vincent.crm.contract.dto.response.ContractGetResponse;
import cn.vincent.crm.contract.dto.response.ContractListResponse;
import cn.vincent.crm.contract.mapper.ContractMapper;
import cn.vincent.crm.contract.mapper.ExtContractMapper;
import cn.vincent.crm.system.service.BaseService;
import cn.vincent.crm.system.service.ModuleFieldValueService;
import cn.vincent.mybatis.BaseMapper;
import cn.vincent.security.DataScopeService;
import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 合同服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ContractService {

    @Resource
    private ContractMapper contractMapper;

    @Resource
    private ExtContractMapper extContractMapper;

    @Resource
    private BaseService baseService;

    @Resource
    private ModuleFieldValueService moduleFieldValueService;

    @Resource
    private DataScopeService dataScopeService;

    /**
     * 新增合同
     *
     * @param request 新增请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 新增的合同实体
     */
    public Contract add(ContractAddRequest request, String userId, String orgId) {
        Contract contract = new Contract();
        contract.setId(cn.vincent.common.util.IDGenerator.nextStr());
        contract.setName(request.getName());
        contract.setCustomerId(request.getCustomerId());
        contract.setOpportunityId(request.getOpportunityId());
        contract.setOwner(StringUtils.isNotBlank(request.getOwner()) ? request.getOwner() : userId);
        contract.setAmount(request.getAmount());
        contract.setStartDate(request.getStartDate());
        contract.setEndDate(request.getEndDate());
        contract.setSignedDate(request.getSignedDate());
        contract.setStatus(StringUtils.isNotBlank(request.getStatus()) ? request.getStatus() : "draft");
        contract.setRemark(request.getRemark());
        contract.setOrganizationId(orgId);
        contract.setCreateUser(userId);
        contract.setUpdateUser(userId);
        contract.setCreateTime(System.currentTimeMillis());
        contract.setUpdateTime(System.currentTimeMillis());
        contractMapper.insert(contract);

        // 保存自定义字段值
        moduleFieldValueService.saveFieldValues(
                FormKey.CONTRACT.getKey(), contract.getId(), request.getModuleFields(), userId);

        return contract;
    }

    /**
     * 更新合同
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 更新后的合同实体
     */
    public Contract update(ContractUpdateRequest request, String userId, String orgId) {
        Contract contract = contractMapper.selectByPrimaryKey(request.getId());
        if (contract == null) {
            throw new GenericException(Translator.get("contract.not.exist"));
        }

        if (request.getName() != null) {
            contract.setName(request.getName());
        }
        if (request.getCustomerId() != null) {
            contract.setCustomerId(request.getCustomerId());
        }
        if (request.getOpportunityId() != null) {
            contract.setOpportunityId(request.getOpportunityId());
        }
        if (request.getOwner() != null) {
            contract.setOwner(request.getOwner());
        }
        if (request.getAmount() != null) {
            contract.setAmount(request.getAmount());
        }
        if (request.getStartDate() != null) {
            contract.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            contract.setEndDate(request.getEndDate());
        }
        if (request.getSignedDate() != null) {
            contract.setSignedDate(request.getSignedDate());
        }
        if (request.getStatus() != null) {
            contract.setStatus(request.getStatus());
        }
        if (request.getRemark() != null) {
            contract.setRemark(request.getRemark());
        }
        contract.setUpdateUser(userId);
        contract.setUpdateTime(System.currentTimeMillis());
        contractMapper.update(contract);

        // 保存自定义字段值
        moduleFieldValueService.saveFieldValues(
                FormKey.CONTRACT.getKey(), contract.getId(), request.getModuleFields(), userId);

        return contract;
    }

    /**
     * 删除合同
     *
     * @param id 合同 ID
     */
    public void delete(String id) {
        Contract contract = contractMapper.selectByPrimaryKey(id);
        if (contract == null) {
            throw new GenericException(Translator.get("contract.not.exist"));
        }

        // 删除自定义字段值
        moduleFieldValueService.deleteFieldValues(FormKey.CONTRACT.getKey(), id);

        // 删除合同
        contractMapper.deleteByIds(List.of(id));
    }

    /**
     * 合同列表（分页）
     *
     * @param request  分页请求
     * @param userId   当前用户 ID
     * @param orgId    当前组织 ID
     * @param ownerIds 数据权限范围内的负责人 ID 列表
     * @return 分页结果
     */
    public cn.vincent.common.response.PagerWithOption<List<ContractListResponse>> list(ContractPageRequest request,
                                                                                       String userId, String orgId,
                                                                                       List<String> ownerIds) {
        ConditionFilterUtils.parseCondition(request, FormKey.CONTRACT.getKey());

        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        List<Contract> contracts = extContractMapper.selectContractPage(
                orgId, request.getKeyword(), request.getStatus(), request.getCustomerId());
        PageInfo<Contract> pageInfo = new PageInfo<>(contracts);

        // 批量查询自定义字段值
        List<String> contractIds = contracts.stream().map(Contract::getId).toList();
        Map<String, List<ModuleFieldValueDTO>> fieldValuesMap = Collections.emptyMap();
        if (!contractIds.isEmpty()) {
            fieldValuesMap = moduleFieldValueService.batchGetFieldValues(
                    FormKey.CONTRACT.getKey(), contractIds);
        }

        Map<String, List<ModuleFieldValueDTO>> finalFieldValuesMap = fieldValuesMap;

        List<ContractListResponse> responseList = contracts.stream()
                .map(contract -> {
                    ContractListResponse response = BeanUtils.copyBean(new ContractListResponse(), contract);
                    response.setModuleFields(
                            finalFieldValuesMap.getOrDefault(contract.getId(), Collections.emptyList()));
                    return response;
                })
                .toList();

        // 批量设置创建人/负责人姓名
        responseList = baseService.setCreateAndUpdateUserName(responseList);
        for (ContractListResponse response : responseList) {
            if (StringUtils.isNotBlank(response.getOwner())) {
                // ownerName 由前端或 baseService 设置，这里简化处理
            }
        }

        return cn.vincent.common.response.PagerWithOption.of(responseList, pageInfo.getTotal(),
                request.getCurrent(), request.getPageSize());
    }

    /**
     * 合同详情（含数据权限校验）
     *
     * @param id     合同 ID
     * @param userId 当前用户 ID
     * @param orgId  当前组织 ID
     * @return 合同详情响应
     */
    public ContractGetResponse getWithDataPermissionCheck(String id, String userId, String orgId) {
        ContractGetResponse response = get(id);
        if (response == null) {
            throw new GenericException(Translator.get("contract.not.exist"));
        }
        dataScopeService.checkDataPermission(userId, orgId, List.of(response.getOwner()), PermissionConstants.CONTRACT_MANAGEMENT_READ);
        return response;
    }

    /**
     * 合同详情
     *
     * @param id 合同 ID
     * @return 合同详情响应
     */
    public ContractGetResponse get(String id) {
        Contract contract = contractMapper.selectByPrimaryKey(id);
        if (contract == null) {
            return null;
        }

        ContractGetResponse response = BeanUtils.copyBean(new ContractGetResponse(), contract);

        // 查询自定义字段值
        List<ModuleFieldValueDTO> fieldValues = moduleFieldValueService.getFieldValues(
                FormKey.CONTRACT.getKey(), id);
        response.setModuleFields(fieldValues);

        // 设置创建人/更新人/负责人姓名
        baseService.setCreateUpdateOwnerUserName(response);

        return response;
    }

    /**
     * 变更合同状态
     *
     * @param request 状态变更请求
     * @param userId  当前用户 ID
     * @return 更新后的合同实体
     */
    public Contract changeStatus(ContractStatusRequest request, String userId) {
        Contract contract = contractMapper.selectByPrimaryKey(request.getId());
        if (contract == null) {
            throw new GenericException(Translator.get("contract.not.exist"));
        }
        contract.setStatus(request.getStatus());
        contract.setUpdateUser(userId);
        contract.setUpdateTime(System.currentTimeMillis());
        contractMapper.update(contract);
        return contract;
    }
}
