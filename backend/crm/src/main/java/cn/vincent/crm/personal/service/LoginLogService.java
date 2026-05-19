package cn.vincent.crm.personal.service;

import cn.vincent.common.response.PagerWithOption;
import cn.vincent.common.util.BeanUtils;
import cn.vincent.crm.personal.domain.LoginLog;
import cn.vincent.crm.personal.dto.request.LoginLogPageRequest;
import cn.vincent.crm.personal.dto.response.LoginLogListResponse;
import cn.vincent.crm.personal.mapper.ExtLoginLogMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 登录日志服务 - 处理登录日志的写入与查询
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class LoginLogService {

    /** 登录日志自定义 Mapper */
    @Resource
    private ExtLoginLogMapper extLoginLogMapper;

    /**
     * 分页查询当前用户的登录日志
     *
     * @param request 分页请求
     * @param userId  当前用户 ID
     * @return 登录日志分页结果
     */
    public PagerWithOption<List<LoginLogListResponse>> page(LoginLogPageRequest request, String userId) {
        PageHelper.startPage(request.getCurrent(), request.getPageSize());
        List<LoginLog> logs = extLoginLogMapper.selectByUserId(userId);
        PageInfo<LoginLog> pageInfo = new PageInfo<>(logs);

        List<LoginLogListResponse> responseList = logs.stream()
                .map(log -> BeanUtils.copyBean(new LoginLogListResponse(), log))
                .toList();

        return PagerWithOption.of(responseList, pageInfo.getTotal(), request.getCurrent(), request.getPageSize());
    }
}
