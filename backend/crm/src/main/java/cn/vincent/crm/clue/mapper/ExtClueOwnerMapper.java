package cn.vincent.crm.clue.mapper;

import cn.vincent.crm.clue.domain.ClueOwner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 线索负责人变更历史自定义 Mapper - 包含非通用查询
 */
@Mapper
public interface ExtClueOwnerMapper {

    /**
     * 根据线索 ID 查询负责人变更历史列表
     *
     * @param clueId 线索 ID
     * @return 变更历史列表
     */
    List<ClueOwner> selectByClueId(@Param("clueId") String clueId);
}