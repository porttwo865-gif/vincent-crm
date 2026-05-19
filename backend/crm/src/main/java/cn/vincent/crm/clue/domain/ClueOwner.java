package cn.vincent.crm.clue.domain;

import cn.vincent.mybatis.BaseModel;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 线索负责人变更历史实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "clue_owner")
public class ClueOwner extends BaseModel {

    /** 线索 ID */
    private String clueId;

    /** 原负责人 */
    private String fromOwner;

    /** 新负责人 */
    private String toOwner;

    /** 操作类型（TRANSFER/CLAIM/ASSIGN/MOVE_POOL） */
    private String operationType;

    /** 操作人 */
    private String operatorId;

    /** 操作时间 */
    private Long operateTime;

    /** 组织 ID */
    private String organizationId;
}
