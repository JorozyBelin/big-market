package org.example.domain.strategy.service.rule.chain;

public interface ILogicChainArmory {
    /**
     * 责任链接口：添加责任链
     *
     * @param logicChain 责任链
     * @return 返回添加的责任链
     */
    ILogicChain appendNext(ILogicChain logicChain);

    /**
     * 责任链接口：获取责任链
     *
     * @return 返回下一个责任链
     */
    ILogicChain next();
}
