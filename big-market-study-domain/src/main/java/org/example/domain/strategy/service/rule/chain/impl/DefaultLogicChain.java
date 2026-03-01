package org.example.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.service.armory.StrategyArmoryDispatch;
import org.example.domain.strategy.service.rule.chain.AbstractLogicChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("default")
public class DefaultLogicChain extends AbstractLogicChain {
    @Autowired
    private StrategyArmoryDispatch strategyArmoryDispatch;
    @Override
    public Integer logic(String userId, Long strategyId) {
        return strategyArmoryDispatch.getRandomAwardId(strategyId);
    }

    @Override
    public String ruleModel() {
        return "default";
    }
}
