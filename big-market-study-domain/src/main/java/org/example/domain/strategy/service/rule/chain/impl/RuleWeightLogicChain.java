package org.example.domain.strategy.service.rule.chain.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.armory.StrategyArmoryDispatch;
import org.example.domain.strategy.service.rule.chain.AbstractLogicChain;
import org.example.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import org.example.types.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {
    
    private static Long userScore=4500L;
    @Autowired
    private IStrategyRepository strategyRepository;
    @Autowired
    private StrategyArmoryDispatch strategyArmoryDispatch;
    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        log.info("抽奖责任链-权重过滤：userId={},strategyId={}",userId,strategyId);
        String ruleValue = strategyRepository.queryStrategyRuleValue(strategyId, null, ruleModel());
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(ruleValue);
        if (null == analyticalValueGroup || analyticalValueGroup.isEmpty()) {
            return null;
        }
        ArrayList<Long> keys = new ArrayList<>(analyticalValueGroup.keySet());
        Collections.sort(keys);
        Long nextValue = keys.stream()
                .filter(key -> userScore >= key)
                .max(Long::compareTo)
                .orElse(null);
        if(null!=nextValue){
            Integer awardId = strategyArmoryDispatch.getRandomAwardId(strategyId, analyticalValueGroup.get(nextValue));
            log.info("抽奖责任链-权重接管：userId={},strategyId={},ruleModel={},awardId={}",userId,strategyId,ruleModel(),awardId);
            return DefaultChainFactory.StrategyAwardVO.builder()
                    .logicModel(DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode())
                    .awardId(awardId)
                    .build();
        }
        //过滤其他责任链
        log.info("抽奖责任链-权重放行：userId={},strategyId={}",userId,strategyId);
        return next().logic(userId,strategyId);
    }

    @Override
    public String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode();
    }

    private Map<Long, String> getAnalyticalValue(String ruleValue) {
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        Map<Long, String> ruleValueMap = new HashMap<>();
        for (String ruleValueKey : ruleValueGroups) {
            // 检查输入是否为空
            if (ruleValueKey == null || ruleValueKey.isEmpty()) {
                return ruleValueMap;
            }
            // 分割字符串以获取键和值
            String[] parts = ruleValueKey.split(Constants.COLON);
            if (parts.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueKey);
            }
            ruleValueMap.put(Long.parseLong(parts[0]), ruleValueKey);
        }
        return ruleValueMap;
    }
}
