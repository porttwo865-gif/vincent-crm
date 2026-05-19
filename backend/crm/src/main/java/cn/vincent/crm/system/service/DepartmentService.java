package cn.vincent.crm.system.service;

import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.system.domain.Department;
import cn.vincent.crm.system.domain.User;
import cn.vincent.crm.system.dto.response.DepartmentTreeResponse;
import cn.vincent.crm.system.dto.request.DepartmentAddRequest;
import cn.vincent.crm.system.dto.request.DepartmentUpdateRequest;
import cn.vincent.crm.system.mapper.DepartmentMapper;
import cn.vincent.crm.system.mapper.ExtDepartmentMapper;
import cn.vincent.common.exception.GenericException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门管理服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class DepartmentService {

    /** 部门通用 Mapper */
    @Resource
    private DepartmentMapper departmentMapper;

    /** 部门自定义 Mapper */
    @Resource
    private ExtDepartmentMapper extDepartmentMapper;

    /**
     * 新增部门
     *
     * @param request 新增请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 新增的部门实体
     */
    public Department add(DepartmentAddRequest request, String userId, String orgId) {
        Department department = new Department();
        department.setId(IDGenerator.nextStr());
        department.setName(request.getName());
        department.setParentId(StringUtils.isNotBlank(request.getParentId()) ? request.getParentId() : "0");
        department.setSort(request.getSort() != null ? request.getSort() : 0);
        department.setOrganizationId(orgId);
        department.setCreateUser(userId);
        department.setUpdateUser(userId);
        department.setCreateTime(System.currentTimeMillis());
        department.setUpdateTime(System.currentTimeMillis());
        departmentMapper.insert(department);
        return department;
    }

    /**
     * 更新部门
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 更新后的部门实体
     */
    public Department update(DepartmentUpdateRequest request, String userId, String orgId) {
        Department department = departmentMapper.selectByPrimaryKey(request.getId());
        if (department == null) {
            throw new GenericException(Translator.get("department.not.exist"));
        }
        if (request.getName() != null) {
            department.setName(request.getName());
        }
        if (request.getParentId() != null) {
            // 不允许将自己设置为父部门
            if (request.getParentId().equals(request.getId())) {
                throw new GenericException(Translator.get("department.parent.invalid"));
            }
            department.setParentId(request.getParentId());
        }
        if (request.getSort() != null) {
            department.setSort(request.getSort());
        }
        department.setUpdateUser(userId);
        department.setUpdateTime(System.currentTimeMillis());
        departmentMapper.update(department);
        return department;
    }

    /**
     * 删除部门（校验是否有子部门和关联用户）
     *
     * @param id 部门 ID
     */
    public void delete(String id) {
        Department department = departmentMapper.selectByPrimaryKey(id);
        if (department == null) {
            throw new GenericException(Translator.get("department.not.exist"));
        }

        // 校验是否有子部门
        int childCount = extDepartmentMapper.countByParentId(id);
        if (childCount > 0) {
            throw new GenericException(Translator.get("department.has.children"));
        }

        // 校验是否有关联用户
        int userCount = extDepartmentMapper.countUsersByDeptId(id);
        if (userCount > 0) {
            throw new GenericException(Translator.get("department.has.users"));
        }

        departmentMapper.deleteByIds(List.of(id));
    }

    /**
     * 获取部门树形结构
     *
     * @param orgId 组织 ID
     * @return 部门树形列表
     */
    public List<DepartmentTreeResponse> tree(String orgId) {
        List<Department> departments = extDepartmentMapper.selectByOrgId(orgId);
        if (departments == null || departments.isEmpty()) {
            return new ArrayList<>();
        }

        // 转换为树形响应
        List<DepartmentTreeResponse> responseList = departments.stream()
                .map(dept -> {
                    DepartmentTreeResponse response = new DepartmentTreeResponse();
                    response.setId(dept.getId());
                    response.setName(dept.getName());
                    response.setParentId(dept.getParentId());
                    response.setSort(dept.getSort());
                    return response;
                })
                .toList();

        // 构建树形结构
        Map<String, List<DepartmentTreeResponse>> parentMap = responseList.stream()
                .filter(r -> StringUtils.isNotBlank(r.getParentId()))
                .collect(Collectors.groupingBy(DepartmentTreeResponse::getParentId));

        responseList.forEach(r -> r.setChildren(parentMap.get(r.getId())));

        // 返回顶层节点（parentId 为 "0" 或空）
        return responseList.stream()
                .filter(r -> "0".equals(r.getParentId()) || StringUtils.isBlank(r.getParentId()))
                .toList();
    }
}
