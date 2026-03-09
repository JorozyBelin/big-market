package org.example.domain.strategy.repository;

import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.model.vo.RuleTreeVO;
import org.example.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import org.example.domain.strategy.model.vo.StrategyAwardStockKeyVO;

import java.util.List;
import java.util.Map;

/**
 * 策略仓库
 */
public interface IStrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTable(String strategyId, int rateRange, Map<Integer, Integer> shuffleStrategyAwardSearchRateTable);

    int getRateRange(Long strategyId);

    int getRateRange(String key);

    Integer getStrategyAwardAssemble(Long strategyId, int rateKey);

    Integer getStrategyAwardAssemble(String strategyId, int rateKey);

    StrategyEntity queryStrategyList(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleWeight);

    String queryStrategyRuleModel(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);

    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);

    /**
     * 缓存奖品库存
     *
     * @param key redis key
     * @param awardCount 奖品数量
     */
    void cacheStrategyAwardCount(String key, Integer awardCount);

    /**
     * 扣减奖品库存
     *
     * @param cacheKey redis key
     * @return 扣减结果
     */
    boolean subtractionAwardStock(String cacheKey);

    /**
     * 奖品库存消费队列
     *
     * @param strategyAwardStockKeyVO 对象值对象
     */
    void awardStockConsumeSendQueue(StrategyAwardStockKeyVO strategyAwardStockKeyVO);

    /**
     * 获取奖品库存消费队列
     *
     * @return 奖品库存消费队列
     */
    StrategyAwardStockKeyVO takeQueueValue();

    /**
     * 更新奖品库存
     *
     * @param strategyId 策略ID
     * @param awardId 奖品ID
     */
    void updateStrategyAwardStock(Long strategyId, Integer awardId);
}
