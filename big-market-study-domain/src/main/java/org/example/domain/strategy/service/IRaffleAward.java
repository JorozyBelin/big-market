package org.example.domain.strategy.service;

import org.example.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

public interface IRaffleAward {
    /**
     * 查询策略奖品列表
     * @param strategyId 策略ID
     * @return 策略奖品列表
     */
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    /**
     * 根据活动ID查询策略奖品列表
     * @param activityId 活动ID
     * @return 策略奖品列表
     */
    List<StrategyAwardEntity> queryStrategyAwardListByActivityId(Long activityId);
}
