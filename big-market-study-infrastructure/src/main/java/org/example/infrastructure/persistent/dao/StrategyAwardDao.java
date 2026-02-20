package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.StrategyAward;

import java.util.List;

@Mapper
public interface StrategyAwardDao {
    List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);

    StrategyAward queryStrategyAwardRuleModels(StrategyAward strategyAward);
}
