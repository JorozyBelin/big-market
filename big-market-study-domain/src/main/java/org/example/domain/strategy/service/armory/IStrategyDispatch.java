package org.example.domain.strategy.service.armory;
/**
 * 策略抽奖调度调度
 */
public interface IStrategyDispatch {
    /**
     * 抽取策略装配的随机结果
     * @param strategyId 策略ID
     * @return
     */
    Integer getRandomAwardId(Long strategyId);

    /**
     * 抽取策略权重的随机结果
     * @param strategyId
     * @param ruleWeight
     * @return
     */
    Integer getRandomAwardId(Long strategyId,String ruleWeight);
}
