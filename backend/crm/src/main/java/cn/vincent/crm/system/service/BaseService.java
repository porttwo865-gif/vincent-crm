package cn.vincent.crm.system.service;

import cn.vincent.common.util.BeanUtils;
import cn.vincent.crm.system.mapper.UserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 通用基础服务 - 提供创建人/更新人姓名设置等公共方法
 */
@Service
@Slf4j
public class BaseService {

    /** 用户 Mapper */
    @Resource
    private UserMapper userMapper;

    /**
     * 设置创建人和更新人姓名
     * <p>
     * 通过反射获取 createUser/updateUser/owner 字段，
     * 查询用户表获取姓名并设置到对应 Name 字段
     *
     * @param response 响应对象
     * @param <T>      响应类型
     * @return 设置姓名后的响应对象
     */
    public <T> T setCreateUpdateOwnerUserName(T response) {
        if (response == null) {
            return null;
        }
        try {
            Set<String> userIds = new HashSet<>();
            // 收集需要查询的用户 ID
            String createUser = getFieldValue(response, "createUser");
            String updateUser = getFieldValue(response, "updateUser");
            String owner = getFieldValue(response, "owner");

            if (StringUtils.isNotBlank(createUser)) {
                userIds.add(createUser);
            }
            if (StringUtils.isNotBlank(updateUser)) {
                userIds.add(updateUser);
            }
            if (StringUtils.isNotBlank(owner)) {
                userIds.add(owner);
            }

            if (userIds.isEmpty()) {
                return response;
            }

            // 批量查询用户姓名
            Map<String, String> userNameMap = getUserNameMap(userIds);

            // 设置姓名
            if (StringUtils.isNotBlank(createUser) && userNameMap.containsKey(createUser)) {
                setFieldValue(response, "createUserName", userNameMap.get(createUser));
            }
            if (StringUtils.isNotBlank(updateUser) && userNameMap.containsKey(updateUser)) {
                setFieldValue(response, "updateUserName", userNameMap.get(updateUser));
            }
            if (StringUtils.isNotBlank(owner) && userNameMap.containsKey(owner)) {
                setFieldValue(response, "ownerName", userNameMap.get(owner));
            }
        } catch (Exception e) {
            log.error("设置创建人/更新人姓名失败: {}", e.getMessage(), e);
        }
        return response;
    }

    /**
     * 批量设置创建人和更新人姓名
     *
     * @param list 响应列表
     * @param <T>  响应类型
     * @return 设置姓名后的列表
     */
    public <T> List<T> setCreateAndUpdateUserName(List<T> list) {
        if (list == null || list.isEmpty()) {
            return list;
        }
        try {
            // 收集所有用户 ID
            Set<String> userIds = new HashSet<>();
            for (T item : list) {
                String createUser = getFieldValue(item, "createUser");
                String updateUser = getFieldValue(item, "updateUser");
                if (StringUtils.isNotBlank(createUser)) {
                    userIds.add(createUser);
                }
                if (StringUtils.isNotBlank(updateUser)) {
                    userIds.add(updateUser);
                }
            }

            if (userIds.isEmpty()) {
                return list;
            }

            // 批量查询用户姓名
            Map<String, String> userNameMap = getUserNameMap(userIds);

            // 设置姓名
            for (T item : list) {
                String createUser = getFieldValue(item, "createUser");
                String updateUser = getFieldValue(item, "updateUser");
                if (StringUtils.isNotBlank(createUser) && userNameMap.containsKey(createUser)) {
                    setFieldValue(item, "createUserName", userNameMap.get(createUser));
                }
                if (StringUtils.isNotBlank(updateUser) && userNameMap.containsKey(updateUser)) {
                    setFieldValue(item, "updateUserName", userNameMap.get(updateUser));
                }
            }
        } catch (Exception e) {
            log.error("批量设置创建人/更新人姓名失败: {}", e.getMessage(), e);
        }
        return list;
    }

    /**
     * 根据用户 ID 集合批量查询用户姓名映射
     *
     * @param userIds 用户 ID 集合
     * @return 用户 ID -> 姓名 映射
     */
    private Map<String, String> getUserNameMap(Set<String> userIds) {
        if (userIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return userMapper.selectByIds(new ArrayList<>(userIds)).stream()
                .filter(user -> StringUtils.isNotBlank(user.getName()))
                .collect(Collectors.toMap(
                        user -> user.getId(),
                        user -> user.getName(),
                        (existing, replacement) -> existing
                ));
    }

    /**
     * 通过反射获取对象字段值
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @return 字段值，获取失败返回 null
     */
    private String getFieldValue(Object obj, String fieldName) {
        try {
            Method getter = obj.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            Object value = getter.invoke(obj);
            return value != null ? value.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过反射设置对象字段值
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @param value     字段值
     */
    private void setFieldValue(Object obj, String fieldName, String value) {
        try {
            Method setter = obj.getClass().getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), String.class);
            setter.invoke(obj, value);
        } catch (Exception e) {
            // 字段不存在则忽略
        }
    }
}
