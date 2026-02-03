package org.example.domain.strategy.repository;

import org.example.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;
import java.util.Map;

/**
 * 策略仓库
 */
public interface IStrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTable(Long strategyId, int rateRange, Map<Integer, Integer> shuffleStrategyAwardSearchRateTable);

    int getRateRange(Long strategyId);

    Integer getStrategyAwardAssemble(Long strategyId, int rateKey);
}
