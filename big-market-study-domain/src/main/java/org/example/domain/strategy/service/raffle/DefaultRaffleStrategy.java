package org.example.domain.strategy.service.raffle;
/**
 * 默认的抽奖策略实现
 */

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.RaffleFactoryEntity;
import org.example.domain.strategy.model.entity.RuleActionEntity;
import org.example.domain.strategy.model.entity.RuleMatterEntity;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.domain.strategy.service.armory.IStrategyDispatch;
import org.example.domain.strategy.service.rule.ILogicFilter;
import org.example.domain.strategy.service.rule.factory.DefaultLogicFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DefaultRaffleStrategy extends AbstractRaffleStrategy{

    @Autowired
    private IStrategyDispatch strategyDispatch;
    @Autowired
    private IStrategyRepository strategyRepository;
    @Autowired
    private DefaultLogicFactory logicFactory;

    public DefaultRaffleStrategy(IStrategyRepository strategyRepository, IStrategyDispatch strategyDispatch) {
        super(strategyRepository, strategyDispatch);
    }

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckBeforeRaffleLogic(RaffleFactoryEntity raffleFactoryEntity, String... logics) {
        Map<String, ILogicFilter<RuleActionEntity.RaffleBeforeEntity>> logicFilterGroup = logicFactory.openLogicFilter();
        String ruleBlackList = Arrays.stream(logics)
                .filter(logic -> logic.contains(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .findFirst()
                .orElse(null);
        if(ruleBlackList != null){
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> logicFilter = logicFilterGroup.get(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());
            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
            ruleMatterEntity.setStrategyId(raffleFactoryEntity.getStrategyId());
            ruleMatterEntity.setRuleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());
            ruleMatterEntity.setUserId(raffleFactoryEntity.getUserId());
            ruleMatterEntity.setAwardId(ruleMatterEntity.getAwardId());
            RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleBeforeActionEntity = logicFilter.filter(ruleMatterEntity);
            if(!ruleBeforeActionEntity.getCode().equals(RuleLogicCheckTypeVO.ALLOW.getCode())){
                return ruleBeforeActionEntity;
            }
        }
        List<String> ruleList = Arrays.stream(logics)
                .filter(logic -> !logic.contains(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .collect(Collectors.toList());

        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity = null;
        for (String ruleModel : ruleList) {
            ILogicFilter<RuleActionEntity.RaffleBeforeEntity> logicFilter = logicFilterGroup.get(ruleModel);
            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
            ruleMatterEntity.setUserId(raffleFactoryEntity.getUserId());
            ruleMatterEntity.setAwardId(ruleMatterEntity.getAwardId());
            ruleMatterEntity.setStrategyId(raffleFactoryEntity.getStrategyId());
            ruleMatterEntity.setRuleModel(ruleModel);
            ruleActionEntity = logicFilter.filter(ruleMatterEntity);
            // 非放行结果则顺序过滤
            log.info("抽奖前规则过滤 userId: {} ruleModel: {} code: {} info: {}", raffleFactoryEntity.getUserId(), ruleModel, ruleActionEntity.getCode(), ruleActionEntity.getInfo());
            if (!RuleLogicCheckTypeVO.ALLOW.getCode().equals(ruleActionEntity.getCode())) return ruleActionEntity;
        }

        return ruleActionEntity;

    }
}
