package cn.vincent.crm.follow.service;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.common.util.IDGenerator;
import cn.vincent.common.util.Translator;
import cn.vincent.crm.follow.domain.FollowRecord;
import cn.vincent.crm.follow.dto.request.FollowRecordAddRequest;
import cn.vincent.crm.follow.dto.request.FollowRecordUpdateRequest;
import cn.vincent.crm.follow.dto.response.FollowRecordListResponse;
import cn.vincent.crm.follow.mapper.ExtFollowRecordMapper;
import cn.vincent.crm.follow.mapper.FollowRecordMapper;
import cn.vincent.crm.system.service.BaseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 跟进记录服务
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class FollowRecordService {

    /** 跟进记录通用 Mapper */
    @Resource
    private FollowRecordMapper followRecordMapper;

    /** 跟进记录自定义 Mapper */
    @Resource
    private ExtFollowRecordMapper extFollowRecordMapper;

    /** 通用基础服务 */
    @Resource
    private BaseService baseService;

    /**
     * 添加跟进记录
     *
     * @param request 添加请求
     * @param userId  当前用户 ID
     * @param orgId   当前组织 ID
     * @return 新增的跟进记录实体
     */
    public FollowRecord add(FollowRecordAddRequest request, String userId, String orgId) {
        FollowRecord record = new FollowRecord();
        record.setId(IDGenerator.nextStr());
        record.setBizType(request.getBizType());
        record.setBizId(request.getBizId());
        record.setContent(request.getContent());
        record.setFollowType(request.getFollowType());
        record.setNextFollowTime(request.getNextFollowTime());
        record.setAttachments(request.getAttachments());
        record.setOwner(userId);
        record.setOrganizationId(orgId);
        record.setCreateUser(userId);
        record.setUpdateUser(userId);
        record.setCreateTime(System.currentTimeMillis());
        record.setUpdateTime(System.currentTimeMillis());
        followRecordMapper.insert(record);
        return record;
    }

    /**
     * 更新跟进记录
     *
     * @param request 更新请求
     * @param userId  当前用户 ID
     * @return 更新后的跟进记录实体
     */
    public FollowRecord update(FollowRecordUpdateRequest request, String userId) {
        FollowRecord record = followRecordMapper.selectByPrimaryKey(request.getId());
        if (record == null) {
            throw new GenericException(Translator.get("follow.record.not.exist"));
        }

        if (request.getContent() != null) {
            record.setContent(request.getContent());
        }
        if (request.getFollowType() != null) {
            record.setFollowType(request.getFollowType());
        }
        if (request.getNextFollowTime() != null) {
            record.setNextFollowTime(request.getNextFollowTime());
        }
        if (request.getAttachments() != null) {
            record.setAttachments(request.getAttachments());
        }
        record.setUpdateUser(userId);
        record.setUpdateTime(System.currentTimeMillis());
        followRecordMapper.update(record);
        return record;
    }

    /**
     * 删除跟进记录
     *
     * @param id 跟进记录 ID
     */
    public void delete(String id) {
        FollowRecord record = followRecordMapper.selectByPrimaryKey(id);
        if (record == null) {
            throw new GenericException(Translator.get("follow.record.not.exist"));
        }
        followRecordMapper.deleteByIds(List.of(id));
    }

    /**
     * 根据业务类型和业务对象 ID 查询跟进记录列表
     *
     * @param bizType 业务类型
     * @param bizId   业务对象 ID
     * @return 跟进记录列表响应
     */
    public List<FollowRecordListResponse> list(String bizType, String bizId) {
        List<FollowRecord> records = extFollowRecordMapper.selectByBiz(bizType, bizId);
        if (records == null || records.isEmpty()) {
            return new ArrayList<>();
        }

        List<FollowRecordListResponse> responseList = new ArrayList<>();
        for (FollowRecord record : records) {
            FollowRecordListResponse response = BeanUtils.copyBean(new FollowRecordListResponse(), record);
            responseList.add(response);
        }

        // 批量设置创建人/更新人/负责人姓名
        baseService.setCreateAndUpdateUserName(responseList);
        return responseList;
    }
}
