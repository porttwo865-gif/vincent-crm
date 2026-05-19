package cn.vincent.crm.customer.service;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.customer.domain.CustomerContact;
import cn.vincent.crm.customer.dto.request.ContactAddRequest;
import cn.vincent.crm.customer.dto.request.ContactUpdateRequest;
import cn.vincent.crm.customer.dto.response.ContactListResponse;
import cn.vincent.crm.customer.mapper.CustomerContactMapper;
import cn.vincent.crm.customer.mapper.ExtCustomerContactMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 联系人管理服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ContactService {

    /** 客户联系人 Mapper */
    @Resource
    private CustomerContactMapper customerContactMapper;

    /** 客户联系人自定义 Mapper */
    @Resource
    private ExtCustomerContactMapper extCustomerContactMapper;

    /**
     * 根据客户 ID 查询联系人列表
     *
     * @param customerId 客户 ID
     * @return 联系人列表
     */
    public List<ContactListResponse> listByCustomerId(String customerId) {
        List<CustomerContact> contacts = extCustomerContactMapper.selectByCustomerId(customerId);
        return contacts.stream()
                .map(contact -> BeanUtils.copyBean(new ContactListResponse(), contact))
                .toList();
    }

    /**
     * 新增联系人
     *
     * @param request 新增请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 新增的联系人实体
     */
    public CustomerContact add(ContactAddRequest request, String userId, String orgId) {
        CustomerContact contact = new CustomerContact();
        contact.setId(IDGenerator.nextStr());
        contact.setCustomerId(request.getCustomerId());
        contact.setName(request.getName());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        contact.setPosition(request.getPosition());
        contact.setDepartment(request.getDepartment());
        contact.setIsPrimary(request.getIsPrimary() != null ? request.getIsPrimary() : false);
        contact.setRemark(request.getRemark());
        contact.setOrganizationId(orgId);
        contact.setCreateUser(userId);
        contact.setUpdateUser(userId);
        contact.setCreateTime(System.currentTimeMillis());
        contact.setUpdateTime(System.currentTimeMillis());
        customerContactMapper.insert(contact);
        return contact;
    }

    /**
     * 更新联系人
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 更新后的联系人实体
     */
    public CustomerContact update(ContactUpdateRequest request, String userId, String orgId) {
        CustomerContact contact = customerContactMapper.selectByPrimaryKey(request.getId());
        if (contact == null) {
            throw new GenericException(Translator.get("contact.not.exist"));
        }

        if (request.getName() != null) {
            contact.setName(request.getName());
        }
        if (request.getPhone() != null) {
            contact.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            contact.setEmail(request.getEmail());
        }
        if (request.getPosition() != null) {
            contact.setPosition(request.getPosition());
        }
        if (request.getDepartment() != null) {
            contact.setDepartment(request.getDepartment());
        }
        if (request.getIsPrimary() != null) {
            contact.setIsPrimary(request.getIsPrimary());
        }
        if (request.getRemark() != null) {
            contact.setRemark(request.getRemark());
        }
        contact.setUpdateUser(userId);
        contact.setUpdateTime(System.currentTimeMillis());
        customerContactMapper.update(contact);
        return contact;
    }

    /**
     * 删除联系人
     *
     * @param id 联系人 ID
     */
    public void delete(String id) {
        CustomerContact contact = customerContactMapper.selectByPrimaryKey(id);
        if (contact == null) {
            throw new GenericException(Translator.get("contact.not.exist"));
        }
        customerContactMapper.deleteByIds(List.of(id));
    }
}
