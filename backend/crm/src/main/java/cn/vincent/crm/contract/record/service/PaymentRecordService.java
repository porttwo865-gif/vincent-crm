package cn.vincent.crm.contract.record.service;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.contract.record.domain.PaymentRecord;
import cn.vincent.crm.contract.record.dto.request.PaymentRecordAddRequest;
import cn.vincent.crm.contract.record.dto.request.PaymentRecordUpdateRequest;
import cn.vincent.crm.contract.record.dto.response.PaymentRecordListResponse;
import cn.vincent.crm.contract.record.mapper.ExtPaymentRecordMapper;
import cn.vincent.crm.contract.record.mapper.PaymentRecordMapper;
import cn.vincent.crm.system.service.BaseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 回款记录服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PaymentRecordService {

    @Resource
    private PaymentRecordMapper paymentRecordMapper;

    @Resource
    private ExtPaymentRecordMapper extPaymentRecordMapper;

    @Resource
    private BaseService baseService;

    /**
     * 新增回款记录
     *
     * @param request 新增请求
     * @param userId  当前用户 ID
     * @return 新增的回款记录实体
     */
    public PaymentRecord add(PaymentRecordAddRequest request, String userId) {
        PaymentRecord record = new PaymentRecord();
        record.setId(IDGenerator.nextStr());
        record.setContractId(request.getContractId());
        record.setPlanId(request.getPlanId());
        record.setAmount(request.getAmount());
        record.setPaymentDate(request.getPaymentDate());
        record.setPaymentMethod(request.getPaymentMethod());
        record.setRemark(request.getRemark());
        record.setCreateUser(userId);
        record.setUpdateUser(userId);
        record.setCreateTime(System.currentTimeMillis());
        record.setUpdateTime(System.currentTimeMillis());
        paymentRecordMapper.insert(record);
        return record;
    }

    /**
     * 更新回款记录
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @return 更新后的回款记录实体
     */
    public PaymentRecord update(PaymentRecordUpdateRequest request, String userId) {
        PaymentRecord record = paymentRecordMapper.selectByPrimaryKey(request.getId());
        if (record == null) {
            throw new GenericException(Translator.get("payment.record.not.exist"));
        }

        if (request.getPlanId() != null) {
            record.setPlanId(request.getPlanId());
        }
        if (request.getAmount() != null) {
            record.setAmount(request.getAmount());
        }
        if (request.getPaymentDate() != null) {
            record.setPaymentDate(request.getPaymentDate());
        }
        if (request.getPaymentMethod() != null) {
            record.setPaymentMethod(request.getPaymentMethod());
        }
        if (request.getRemark() != null) {
            record.setRemark(request.getRemark());
        }
        record.setUpdateUser(userId);
        record.setUpdateTime(System.currentTimeMillis());
        paymentRecordMapper.update(record);
        return record;
    }

    /**
     * 删除回款记录
     *
     * @param id 回款记录 ID
     */
    public void delete(String id) {
        PaymentRecord record = paymentRecordMapper.selectByPrimaryKey(id);
        if (record == null) {
            throw new GenericException(Translator.get("payment.record.not.exist"));
        }
        paymentRecordMapper.deleteByIds(List.of(id));
    }

    /**
     * 根据合同 ID 查询回款记录列表
     *
     * @param contractId 合同 ID
     * @return 回款记录列表
     */
    public List<PaymentRecordListResponse> listByContractId(String contractId) {
        List<PaymentRecord> records = extPaymentRecordMapper.selectByContractId(contractId);
        List<PaymentRecordListResponse> responseList = records.stream()
                .map(record -> BeanUtils.copyBean(new PaymentRecordListResponse(), record))
                .toList();
        return baseService.setCreateAndUpdateUserName(responseList);
    }
}
