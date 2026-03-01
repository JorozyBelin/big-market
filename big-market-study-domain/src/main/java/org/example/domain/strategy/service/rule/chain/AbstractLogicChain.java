package org.example.domain.strategy.service.rule.chain;

public abstract class AbstractLogicChain implements ILogicChain{

    private ILogicChain next;
    @Override
    public ILogicChain appendNext(ILogicChain logicChain) {
        this.next=logicChain;
        return next;
    }

    @Override
    public ILogicChain next() {
        return next;
    }
    public abstract String ruleModel();
}
