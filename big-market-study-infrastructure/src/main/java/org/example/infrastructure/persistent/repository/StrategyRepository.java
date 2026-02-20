package org.example.infrastructure.persistent.repository;
/**
 * 策略仓储实现
 */

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.infrastructure.persistent.dao.StrategyAwardDao;
import org.example.infrastructure.persistent.dao.StrategyDao;
import org.example.infrastructure.persistent.dao.StrategyRuleDao;
import org.example.infrastructure.persistent.po.Strategy;
import org.example.infrastructure.persistent.po.StrategyAward;
import org.example.infrastructure.persistent.po.StrategyRule;
import org.example.infrastructure.persistent.redis.IRedisService;
import org.example.types.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class StrategyRepository implements IStrategyRepository {
    @Autowired
    private IRedisService redisService;
    @Autowired
    private StrategyAwardDao strategyAwardDao;
    @Autowired
    private StrategyDao strategyDao;
    @Autowired
    private StrategyRuleDao strategyRuleDao;
    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        String cacheKey= Constants.RedisKey.STRATEGY_AWARD_KEY+strategyId;
        //1、从缓存中获取策略奖品列表
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
        if(strategyAwardEntities!=null&&strategyAwardEntities.size()>0){
            return strategyAwardEntities;
        }
        //2、缓存中没有，则从数据库中获取
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        ArrayList<StrategyAwardEntity> strategyAwardEntityList = new ArrayList<>();
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = StrategyAwardEntity.builder()
                    .strategyId(strategyAward.getStrategyId())
                    .awardId(strategyAward.getAwardId())
                    .awardCount(strategyAward.getAwardCount())
                    .awardCountSurplus(strategyAward.getAwardCountSurplus())
                    .awardCountRate(strategyAward.getAwardCountRate())
                    .build();
                    strategyAwardEntityList.add(strategyAwardEntity);
        }
        //3、缓存策略奖品列表
        redisService.setValue(cacheKey,strategyAwardEntityList);
        return strategyAwardEntityList;
    }

    @Override
    public void storeStrategyAwardSearchRateTable(String strategyId, int rateRange, Map<Integer, Integer> shuffleStrategyAwardSearchRateTable) {
        //1、存储抽奖策略范围值
       redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY+strategyId,rateRange);
       //2、存储抽奖策略概率表
        Map<Integer, Integer> map = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId);
        map.putAll(shuffleStrategyAwardSearchRateTable);

    }

    @Override
    public int getRateRange(Long strategyId) {
        return getRateRange(strategyId.toString());
    }

    @Override
    public int getRateRange(String key) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY+key);
    }

    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, int rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY+strategyId,rateKey);
    }

    @Override
    public Integer getStrategyAwardAssemble(String key, int rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY+key,rateKey);
    }

    @Override
    public StrategyEntity queryStrategyList(Long strategyId) {
        //1、从缓存中获取策略
        String key=Constants.RedisKey.STRATEGY_KEY+strategyId;
        StrategyEntity strategyEntity = redisService.getValue(key);
        if(strategyEntity!=null) return strategyEntity;
        //2、缓存中没有，则从数据库中获取
        Strategy strategy=strategyDao.queryStrategyByStrategyId(strategyId);
        strategyEntity = StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModel(strategy.getRuleModel())
                .build();
                redisService.setValue(key,strategyEntity);
                return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleWeight) {
        StrategyRule strategyRule =strategyRuleDao.queryStrategyRuleByStrategyIdAndRuleWeight(strategyId,ruleWeight);
        log.info("strategyRule:{}",strategyRule);
        return StrategyRuleEntity.builder()
                .strategyId(strategyRule.getStrategyId())
                .awardId(strategyRule.getAwardId())
                .ruleType(strategyRule.getRuleType())
                .ruleModel(strategyRule.getRuleModel())
                .awardValue(strategyRule.getAwardValue())
                .ruleDesc(strategyRule.getRuleDesc())
                .build();
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {
        StrategyRule strategyRule= strategyRuleDao.queryStrategyRuleValue(strategyId, awardId, ruleModel);
        return strategyRule.getAwardValue();
    }

    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setStrategyId(strategyId);
        strategyAward.setAwardId(awardId);
        StrategyAward strategyAwardEntity =strategyAwardDao.queryStrategyAwardRuleModels(strategyAward);
        String ruleModels = strategyAwardEntity.getRuleModels();
        return StrategyAwardRuleModelVO.builder().ruleModels(ruleModels).build();
    }
}
