package org.example.domain.rebate.repository;

import org.example.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import org.example.domain.rebate.model.vo.BehaviorTypeVO;
import org.example.domain.rebate.model.vo.DailyBehaviorRebateVO;

import java.util.ArrayList;
import java.util.List;

public interface IBehaviorRebateRepository {
    /**
     * 查询日常行为返利配置
     * @param behaviorTypeVO
     * @return
     */
    List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO);

    /**
     * 保存用户返利记录
     * @param userId
     * @param behaviorRebateAggregates
     */
    void saveUserRebateRecord(String userId, ArrayList<BehaviorRebateAggregate> behaviorRebateAggregates);
}
