package cn.vincent.crm.system.service;

import cn.vincent.crm.system.dto.response.ModuleFormConfigDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 模块表单缓存服务 - 使用 Redis 缓存表单配置
 * <p>
 * 缓存键格式: module_form:{formKey}:{orgId}
 * TTL: 30 分钟
 */
@Service
@Slf4j
public class ModuleFormCacheService {

    /** 缓存键前缀 */
    private static final String CACHE_PREFIX = "module_form:";

    /** 缓存过期时间（30 分钟） */
    private static final long CACHE_TTL_MINUTES = 30;

    /** Redisson 客户端 */
    @Resource
    private RedissonClient redissonClient;

    /** 模块表单服务 */
    @Resource
    private ModuleFormService moduleFormService;

    /**
     * 获取业务表单配置（带缓存）
     * <p>
     * 优先从 Redis 缓存读取，缓存未命中时从数据库加载并写入缓存
     *
     * @param formKey 表单标识
     * @param orgId   组织 ID
     * @return 表单配置
     */
    public ModuleFormConfigDTO getBusinessFormConfig(String formKey, String orgId) {
        String cacheKey = buildCacheKey(formKey, orgId);
        RBucket<ModuleFormConfigDTO> bucket = redissonClient.getBucket(cacheKey);

        ModuleFormConfigDTO config = bucket.get();
        if (config != null) {
            return config;
        }

        // 缓存未命中，从数据库加载
        config = moduleFormService.getFormConfig(formKey, orgId);
        if (config != null) {
            bucket.set(config, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        }

        return config;
    }

    /**
     * 清除表单配置缓存
     *
     * @param formKey 表单标识
     * @param orgId   组织 ID
     */
    public void clearCache(String formKey, String orgId) {
        if (formKey == null) {
            return;
        }
        String cacheKey = buildCacheKey(formKey, orgId);
        redissonClient.getBucket(cacheKey).delete();
    }

    /**
     * 构建缓存键
     *
     * @param formKey 表单标识
     * @param orgId   组织 ID
     * @return 缓存键
     */
    private String buildCacheKey(String formKey, String orgId) {
        return CACHE_PREFIX + formKey + ":" + orgId;
    }
}
