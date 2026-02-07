package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.Strategy;

@Mapper
public interface StrategyDao {

    Strategy queryStrategyByStrategyId(Long strategyId);
}
