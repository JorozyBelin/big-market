package org.example.domain.strategy.service.rule.chain.factory;

import lombok.*;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DefaultChainFactory {
    private final Map<String, ILogicChain> logicChainMap;

    private final IStrategyRepository strategyRepository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChainMap, IStrategyRepository strategyRepository) {
        this.logicChainMap = logicChainMap;
        this.strategyRepository = strategyRepository;
    }

    public ILogicChain openLogicChain(Long strategyId) {
        StrategyEntity strategyEntity = strategyRepository.queryStrategyList(strategyId);
        String[] ruleModel = strategyEntity.ruleModel();
        //如果没有规则模型，则使用默认规则
        if (ruleModel == null || ruleModel.length == 0) {
            return logicChainMap.get(LogicModel.RULE_DEFAULT.getCode());
        }
        // 按照配置顺序装填用户配置的责任链
        ILogicChain logicChain = logicChainMap.get(ruleModel[0]);
        ILogicChain current = logicChain;
        for (int i = 1; i < ruleModel.length; i++) {
            current = current.appendNext(logicChainMap.get(ruleModel[i]));
        }
        // 将默认规则挂载到责任链的末尾
        current.appendNext(logicChainMap.get(LogicModel.RULE_DEFAULT.getCode()));
        return logicChain;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO {
        /**
         * 抽奖奖品ID - 内部流转使用
         */
        private Integer awardId;
        /**
         *规则模型
         */
        private String logicModel;
        /**
         * 抽奖奖品规则值
         */
        private String awardRuleValue;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_DEFAULT("rule_default", "默认抽奖"),
        RULE_BLACKLIST("rule_blacklist", "黑名单抽奖"),
        RULE_WEIGHT("rule_weight", "权重规则"),
        ;

        private final String code;
        private final String info;

    }

}
