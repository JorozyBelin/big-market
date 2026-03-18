package org.example.domain.activity.service.quota.rule;

public interface IActionChainArmory {

    /**
     * 获取下一个节点
     * @return 下一个节点
     */
    IActionChain next();

    /**
     * 添加下一个节点
     * @param next 下一个节点
     * @return 当前节点
     */
    IActionChain appendNext(IActionChain next);
}
