package org.example.domain.strategy.service.armory;

/**
 * 策略仓库:负责初始化策略计算
 *
 */
public interface IStrategyArmory {
    public boolean assembleLotteryStrategy(Long StrategyId);

    public Integer getRandomAwardId(Long strategyId);

}
