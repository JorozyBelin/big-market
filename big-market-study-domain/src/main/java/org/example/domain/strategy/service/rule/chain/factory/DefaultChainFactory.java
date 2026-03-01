package org.example.domain.strategy.service.rule.chain.factory;

import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.rule.chain.ILogicChain;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultChainFactory {
    private final Map<String , ILogicChain> logicChainMap;

    private final IStrategyRepository strategyRepository;

    public DefaultChainFactory(Map<String , ILogicChain> logicChainMap, IStrategyRepository strategyRepository){
        this.logicChainMap = logicChainMap;
        this.strategyRepository = strategyRepository;
    }

    public ILogicChain openLogicChain(Long strategyId){
        StrategyEntity strategyEntity = strategyRepository.queryStrategyList(strategyId);
        String[] ruleModel = strategyEntity.ruleModel();
        ILogicChain logicChain = logicChainMap.get(ruleModel[0]);
        ILogicChain current=logicChain;
        for (int i = 1; i < ruleModel.length; i++) {
            current = current.appendNext(logicChainMap.get(ruleModel[i]));
        }
        current.appendNext(logicChainMap.get("default"));
        return logicChain;
    }
}
