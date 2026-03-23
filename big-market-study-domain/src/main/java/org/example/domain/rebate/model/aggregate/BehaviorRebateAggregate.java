package org.example.domain.rebate.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.rebate.model.entity.TaskEntity;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;

/**
 * 行为返利聚合对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorRebateAggregate {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 行为返利订单实体对象
     */
    private BehaviorRebateOrderEntity behaviorRebateOrderEntity;
    /**
     * 任务实体对象
     */
    private TaskEntity taskEntity;

}

