package org.example.domain.strategy.service.armory;

/**
 * 策略仓库:负责初始化策略计算
 *
 */
public interface IStrategyArmory {
    /**
     * 装配抽奖策略配置
     *
     * @param strategyId 策略ID
     * @return
     */
    boolean assembleLotteryStrategy(Long strategyId);


}
