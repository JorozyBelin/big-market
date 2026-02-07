package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.StrategyRule;

@Mapper
public interface StrategyRuleDao {
    StrategyRule queryStrategyRuleByStrategyIdAndRuleWeight(Long strategyId, String ruleWeight);

}
