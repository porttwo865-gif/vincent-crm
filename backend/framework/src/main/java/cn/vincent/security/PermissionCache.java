package cn.vincent.security;

import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 权限缓存 - 使用 Redis 缓存用户权限列表
 * <p>
 * 缓存键格式: permission_cache:{userId}:{orgId}
 * TTL: 30 分钟
 */
@Component
public class PermissionCache {

    /** 缓存键前缀 */
    private static final String CACHE_PREFIX = "permission_cache:";

    /** 缓存过期时间（30 分钟） */
    private static final long CACHE_TTL_MINUTES = 30;

    /** Redisson 客户端 */
    @Resource
    private RedissonClient redissonClient;

    /**
     * 获取用户权限 ID 列表（优先从缓存读取，缓存未命中时从数据库加载）
     * <p>
     * 注意：此方法需要由 Mapper 查询实现，当前版本返回空列表，
     * 待 system 模块 Mapper 完善后替换为实际数据库查询逻辑。
     *
     * @param userId 用户 ID
     * @param orgId  组织 ID
     * @return 权限 ID 列表
     */
    @SuppressWarnings("unchecked")
    public List<String> getPermissionIds(String userId, String orgId) {
        String cacheKey = buildCacheKey(userId, orgId);
        RBucket<List<String>> bucket = redissonClient.getBucket(cacheKey);

        List<String> permissions = bucket.get();
        if (permissions != null) {
            return permissions;
        }

        // 从数据库加载权限（通过 ExtRolePermissionMapper）
        // 此处由 UserLoginService 在构建 SessionUser 时一并查询并写入缓存
        // 如果缓存未命中，返回空列表
        return Collections.emptyList();
    }

    /**
     * 设置用户权限缓存
     *
     * @param userId      用户 ID
     * @param orgId       组织 ID
     * @param permissions 权限 ID 列表
     */
    public void setPermissionIds(String userId, String orgId, List<String> permissions) {
        String cacheKey = buildCacheKey(userId, orgId);
        RBucket<List<String>> bucket = redissonClient.getBucket(cacheKey);
        bucket.set(permissions, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * 清除用户权限缓存
     *
     * @param userId 用户 ID
     * @param orgId  组织 ID
     */
    public void clearCache(String userId, String orgId) {
        String cacheKey = buildCacheKey(userId, orgId);
        redissonClient.getBucket(cacheKey).delete();
    }

    /**
     * 构建缓存键
     *
     * @param userId 用户 ID
     * @param orgId  组织 ID
     * @return 缓存键
     */
    private String buildCacheKey(String userId, String orgId) {
        return CACHE_PREFIX + userId + ":" + orgId;
    }
}
