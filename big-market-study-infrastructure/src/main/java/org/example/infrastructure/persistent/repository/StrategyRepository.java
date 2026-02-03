package org.example.infrastructure.persistent.repository;
/**
 * 策略仓储实现
 */

import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.infrastructure.persistent.dao.StrategyAwardDao;
import org.example.infrastructure.persistent.po.StrategyAward;
import org.example.infrastructure.persistent.redis.IRedisService;
import org.example.types.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class StrategyRepository implements IStrategyRepository {
    @Autowired
    private IRedisService redisService;
    @Autowired
    private StrategyAwardDao strategyAwardDao;
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
    public void storeStrategyAwardSearchRateTable(Long strategyId, int rateRange, Map<Integer, Integer> shuffleStrategyAwardSearchRateTable) {
        //1、存储抽奖策略范围值
       redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY+strategyId,rateRange);
       //2、存储抽奖策略概率表
        Map<Integer, Integer> map = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId);
        map.putAll(shuffleStrategyAwardSearchRateTable);

    }

    @Override
    public int getRateRange(Long strategyId) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY+strategyId);
    }

    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, int rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY+strategyId,rateKey);
    }
}
