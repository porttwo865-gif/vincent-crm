package cn.vincent.crm.follow.mapper;

import cn.vincent.crm.follow.domain.FollowRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 跟进记录自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtFollowRecordMapper {

    /**
     * 根据业务类型和业务对象 ID 查询跟进记录列表（时间倒序）
     *
     * @param bizType 业务类型
     * @param bizId   业务对象 ID
     * @return 跟进记录列表
     */
    List<FollowRecord> selectByBiz(@Param("bizType") String bizType, @Param("bizId") String bizId);
}
