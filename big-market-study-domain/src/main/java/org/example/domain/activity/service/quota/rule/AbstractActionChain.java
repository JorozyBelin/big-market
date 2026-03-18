package org.example.domain.activity.service.quota.rule;

public abstract class AbstractActionChain implements IActionChain{

    private IActionChain next;

    public IActionChain appendNext(IActionChain next) {
        return this.next = next;
    }

    @Override
    public IActionChain next() {
        return next;
    }
}
