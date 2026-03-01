package org.example.domain.strategy.service.rule.chain;

public interface ILogicChainArmory {
    ILogicChain appendNext(ILogicChain logicChain);

    ILogicChain next();
}
