package cn.vincent.crm.invoice.service;

import cn.vincent.common.constants.FormKey;
import cn.vincent.common.constants.PermissionConstants;
import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.invoice.domain.Invoice;
import cn.vincent.crm.invoice.dto.request.InvoiceAddRequest;
import cn.vincent.crm.invoice.dto.request.InvoicePageRequest;
import cn.vincent.crm.invoice.dto.request.InvoiceStatusRequest;
import cn.vincent.crm.invoice.dto.request.InvoiceUpdateRequest;
import cn.vincent.crm.invoice.dto.response.InvoiceGetResponse;
import cn.vincent.crm.invoice.dto.response.InvoiceListResponse;
import cn.vincent.crm.invoice.mapper.ExtInvoiceMapper;
import cn.vincent.crm.invoice.mapper.InvoiceMapper;
import cn.vincent.crm.system.service.BaseService;
import cn.vincent.crm.system.service.ModuleFieldValueService;
import cn.vincent.common.response.PagerWithOption;
import cn.vincent.security.DataScopeService;
import cn.vincent.crm.system.dto.response.ModuleFieldValueDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 发票服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class InvoiceService {

    @Resource
    private InvoiceMapper invoiceMapper;

    @Resource
    private ExtInvoiceMapper extInvoiceMapper;

    @Resource
    private BaseService baseService;

    @Resource
    private ModuleFieldValueService moduleFieldValueService;

    @Resource
    private DataScopeService dataScopeService;

    /**
     * 新增发票
     *
     * @param request 新增请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 新增的发票实体
     */
    public Invoice add(InvoiceAddRequest request, String userId, String orgId) {
        Invoice invoice = new Invoice();
        invoice.setId(IDGenerator.nextStr());
        invoice.setContractId(request.getContractId());
        invoice.setCustomerId(request.getCustomerId());
        invoice.setInvoiceNo(request.getInvoiceNo());
        invoice.setAmount(request.getAmount());
        invoice.setInvoiceDate(request.getInvoiceDate());
        invoice.setInvoiceType(request.getInvoiceType());
        invoice.setStatus(request.getStatus());
        invoice.setRemark(request.getRemark());
        invoice.setOrganizationId(orgId);
        invoice.setCreateUser(userId);
        invoice.setUpdateUser(userId);
        invoice.setCreateTime(System.currentTimeMillis());
        invoice.setUpdateTime(System.currentTimeMillis());
        invoiceMapper.insert(invoice);

        // 保存自定义字段值
        moduleFieldValueService.saveFieldValues(
                FormKey.INVOICE.getKey(), invoice.getId(), request.getModuleFields(), userId);

        return invoice;
    }

    /**
     * 更新发票
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 更新后的发票实体
     */
    public Invoice update(InvoiceUpdateRequest request, String userId, String orgId) {
        Invoice invoice = invoiceMapper.selectByPrimaryKey(request.getId());
        if (invoice == null) {
            throw new GenericException(Translator.get("invoice.not.exist"));
        }

        if (request.getContractId() != null) {
            invoice.setContractId(request.getContractId());
        }
        if (request.getCustomerId() != null) {
            invoice.setCustomerId(request.getCustomerId());
        }
        if (request.getInvoiceNo() != null) {
            invoice.setInvoiceNo(request.getInvoiceNo());
        }
        if (request.getAmount() != null) {
            invoice.setAmount(request.getAmount());
        }
        if (request.getInvoiceDate() != null) {
            invoice.setInvoiceDate(request.getInvoiceDate());
        }
        if (request.getInvoiceType() != null) {
            invoice.setInvoiceType(request.getInvoiceType());
        }
        if (request.getStatus() != null) {
            invoice.setStatus(request.getStatus());
        }
        if (request.getRemark() != null) {
            invoice.setRemark(request.getRemark());
        }
        invoice.setUpdateUser(userId);
        invoice.setUpdateTime(System.currentTimeMillis());
        invoiceMapper.update(invoice);

        // 保存自定义字段值
        moduleFieldValueService.saveFieldValues(
                FormKey.INVOICE.getKey(), invoice.getId(), request.getModuleFields(), userId);

        return invoice;
    }

    /**
     * 删除发票
     *
     * @param id 发票 ID
     */
    public void delete(String id) {
        Invoice invoice = invoiceMapper.selectByPrimaryKey(id);
        if (invoice == null) {
            throw new GenericException(Translator.get("invoice.not.exist"));
        }

        // 删除自定义字段值
        moduleFieldValueService.deleteFieldValues(FormKey.INVOICE.getKey(), id);

        // 删除发票
        invoiceMapper.deleteByIds(List.of(id));
    }

    /**
     * 发票列表（分页）
     *
     * @param request 分页请求
     * @param orgId   当前组织 ID
     * @return 分页结果
     */
    public PagerWithOption<List<InvoiceListResponse>> list(InvoicePageRequest request, String orgId) {
        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        List<Invoice> invoices = extInvoiceMapper.selectInvoicePage(
                orgId, request.getKeyword(), request.getStatus(), request.getCustomerId());
        PageInfo<Invoice> pageInfo = new PageInfo<>(invoices);

        // 批量查询自定义字段值
        List<String> invoiceIds = invoices.stream().map(Invoice::getId).toList();
        Map<String, List<ModuleFieldValueDTO>> fieldValuesMap = Collections.emptyMap();
        if (!invoiceIds.isEmpty()) {
            fieldValuesMap = moduleFieldValueService.batchGetFieldValues(
                    FormKey.INVOICE.getKey(), invoiceIds);
        }

        Map<String, List<ModuleFieldValueDTO>> finalFieldValuesMap = fieldValuesMap;

        List<InvoiceListResponse> responseList = invoices.stream()
                .map(invoice -> {
                    InvoiceListResponse response = BeanUtils.copyBean(new InvoiceListResponse(), invoice);
                    response.setModuleFields(
                            finalFieldValuesMap.getOrDefault(invoice.getId(), Collections.emptyList()));
                    return response;
                })
                .toList();

        // 批量设置创建人姓名
        responseList = baseService.setCreateAndUpdateUserName(responseList);

        return PagerWithOption.of(responseList, pageInfo.getTotal(),
                request.getCurrent(), request.getPageSize());
    }

    /**
     * 发票详情（含数据权限校验）
     *
     * @param id     发票 ID
     * @param userId 当前用户 ID
     * @param orgId  当前组织 ID
     * @return 发票详情响应
     */
    public InvoiceGetResponse getWithDataPermissionCheck(String id, String userId, String orgId) {
        InvoiceGetResponse response = get(id);
        if (response == null) {
            throw new GenericException(Translator.get("invoice.not.exist"));
        }
        return response;
    }

    /**
     * 发票详情
     *
     * @param id 发票 ID
     * @return 发票详情响应
     */
    public InvoiceGetResponse get(String id) {
        Invoice invoice = invoiceMapper.selectByPrimaryKey(id);
        if (invoice == null) {
            return null;
        }

        InvoiceGetResponse response = BeanUtils.copyBean(new InvoiceGetResponse(), invoice);

        // 查询自定义字段值
        List<ModuleFieldValueDTO> fieldValues = moduleFieldValueService.getFieldValues(
                FormKey.INVOICE.getKey(), id);
        response.setModuleFields(fieldValues);

        // 设置创建人/更新人姓名
        baseService.setCreateUpdateOwnerUserName(response);

        return response;
    }

    /**
     * 变更发票状态
     *
     * @param request 状态变更请求
     * @param userId  当前用户 ID
     * @return 更新后的发票实体
     */
    public Invoice changeStatus(InvoiceStatusRequest request, String userId) {
        Invoice invoice = invoiceMapper.selectByPrimaryKey(request.getId());
        if (invoice == null) {
            throw new GenericException(Translator.get("invoice.not.exist"));
        }
        invoice.setStatus(request.getStatus());
        invoice.setUpdateUser(userId);
        invoice.setUpdateTime(System.currentTimeMillis());
        invoiceMapper.update(invoice);
        return invoice;
    }
}
