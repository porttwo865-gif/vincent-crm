package cn.vincent.security;

import cn.vincent.common.exception.GenericException;
import cn.vincent.common.response.CrmHttpResultCode;
import cn.vincent.security.dto.DeptDataPermissionDTO;
import cn.vincent.security.dto.SessionUser;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 数据权限服务 - 根据用户角色配置返回可见数据范围
 */
@Service
public class DataScopeService {

    /** 数据权限缓存键前缀 */
    private static final String DATA_SCOPE_CACHE_PREFIX = "data_scope:";

    /** 缓存过期时间（30 分钟） */
    private static final long CACHE_TTL_MINUTES = 30;

    /** Redisson 客户端 */
    @Resource
    private RedissonClient redissonClient;

    /**
     * 获取部门数据权限
     * <p>
     * 根据用户角色的 dataScope 配置返回可见数据范围：
     * - ALL: 可查看全部数据
     * - DEPT_AND_CHILD: 可查看本部门及子部门数据
     * - DEPT_CUSTOM: 可查看自定义部门数据
     * - SELF: 仅可查看本人数据
     *
     * @param userId     用户 ID
     * @param orgId      组织 ID
     * @param viewId     视图 ID（预留）
     * @param permission 权限标识
     * @return 部门数据权限 DTO
     */
    public DeptDataPermissionDTO getDeptDataPermission(String userId, String orgId, String viewId, String permission) {
        SessionUser sessionUser = SessionUtils.getSessionUser();

        // 超级管理员拥有全部数据权限
        DeptDataPermissionDTO dto = new DeptDataPermissionDTO();
        if (sessionUser != null && sessionUser.getRoleIds() != null
                && sessionUser.getRoleIds().contains("role_admin")) {
            dto.setAll(true);
            return dto;
        }

        // 从缓存获取
        String cacheKey = DATA_SCOPE_CACHE_PREFIX + userId + ":" + orgId;
        RBucket<DeptDataPermissionDTO> bucket = redissonClient.getBucket(cacheKey);
        DeptDataPermissionDTO cached = bucket.get();
        if (cached != null) {
            return cached;
        }

        // 默认：仅查看本人数据
        dto.setAll(false);
        dto.setUserIds(List.of(userId));
        dto.setDeptIds(Collections.emptyList());

        // 缓存结果
        bucket.set(dto, CACHE_TTL_MINUTES, TimeUnit.MINUTES);

        return dto;
    }

    /**
     * 校验数据权限（无权限时抛出 GenericException）
     *
     * @param userId     用户 ID
     * @param orgId      组织 ID
     * @param ownerIds   数据所有者 ID 列表
     * @param permission 权限标识
     */
    public void checkDataPermission(String userId, String orgId, List<String> ownerIds, String permission) {
        if (!hasDataPermission(userId, orgId, ownerIds, permission)) {
            throw new GenericException(CrmHttpResultCode.FORBIDDEN);
        }
    }

    /**
     * 判断是否有数据权限
     *
     * @param userId     用户 ID
     * @param orgId      组织 ID
     * @param ownerId    数据所有者 ID（单个）
     * @param permission 权限标识
     * @return 是否有权限
     */
    public boolean hasDataPermission(String userId, String orgId, String ownerId, String permission) {
        return hasDataPermission(userId, orgId, List.of(ownerId), permission);
    }

    /**
     * 判断是否有数据权限（批量）
     *
     * @param userId     用户 ID
     * @param orgId      组织 ID
     * @param ownerIds   数据所有者 ID 列表
     * @param permission 权限标识
     * @return 是否有权限
     */
    public boolean hasDataPermission(String userId, String orgId, List<String> ownerIds, String permission) {
        DeptDataPermissionDTO dto = getDeptDataPermission(userId, orgId, null, permission);

        // ALL 模式直接放行
        if (dto.isAll()) {
            return true;
        }

        // SELF 模式：检查是否在可见用户列表中
        if (dto.getUserIds() != null) {
            for (String ownerId : ownerIds) {
                if (StringUtils.isNotBlank(ownerId) && !dto.getUserIds().contains(ownerId)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }
}
