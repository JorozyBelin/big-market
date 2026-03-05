package org.example.domain.strategy.service.rule.chain;

import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * 抽奖责任链接装配接口
 */
public interface ILogicChain extends ILogicChainArmory{

    /**
     * 责任链接口：逻辑处理
     *
     * @param userId 用户Id
     * @return 奖品Id
     * @strategyId 策略Id
     */
    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);


}
