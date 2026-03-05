package org.example.domain.strategy.service.armory;
/**
 * 策略仓库:负责初始化策略计算
 */

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.entity.StrategyEntity;
import org.example.domain.strategy.model.entity.StrategyRuleEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;


@Service
@Slf4j
public class StrategyArmoryDispatch implements IStrategyArmory, IStrategyDispatch {
    @Autowired
    private IStrategyRepository strategyRepository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        //1、查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities = strategyRepository.queryStrategyAwardList(strategyId);
        assembleLotteryStrategy(String.valueOf(strategyId),strategyAwardEntities);
        //2、权重策略配置
        StrategyEntity strategyEntity =strategyRepository.queryStrategyList(strategyId);
        String ruleWeight = strategyEntity.getRuleWeight();
        if(null==ruleWeight) return true;
        StrategyRuleEntity strategyRuleEntity=strategyRepository.queryStrategyRule(strategyId,ruleWeight);
        if (null == strategyRuleEntity) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }
        Map<String, List<Integer>> ruleWeightValues = strategyRuleEntity.getRuleWeightValues();
        Set<String> keys = ruleWeightValues.keySet();
        for (String key : keys) {
            List<Integer> values = ruleWeightValues.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesClone.removeIf(entity->!values.contains(entity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat("_").concat(key),strategyAwardEntitiesClone);
        }
        return true;
    }
    public boolean assembleLotteryStrategy(String key,List<StrategyAwardEntity> strategyAwardEntities){
        if(strategyAwardEntities==null||strategyAwardEntities.isEmpty()) return false;
        //1、获取策略奖品列表中奖品中奖概率最小值
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardCountRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        //2、获取概率值总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardCountRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("策略奖品列表中奖品中奖概率最小值为:{}",minAwardRate);
        log.info("策略奖品列表中奖品中奖概率总和为:{}",totalAwardRate);
        //3、获取概率范围分段
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);
        //4、生成策略奖品表
        List<Integer> strategyAwardTable = new ArrayList<>();
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardCountRate = strategyAwardEntity.getAwardCountRate();
            for(int i=0;i<rateRange.multiply(awardCountRate).setScale(0,RoundingMode.CEILING).intValue();i++){
                strategyAwardTable.add(awardId);
            }
        }
        //5、乱序排序
        Collections.shuffle(strategyAwardTable);
        // 6. 生成出Map集合，key值，对应的就是后续的概率值。通过概率来获得对应的奖品ID
        Map<Integer, Integer> shuffleStrategyAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardTable.size(); i++) {
            shuffleStrategyAwardSearchRateTable.put(i, strategyAwardTable.get(i));
        }
        //7、存放到redis中
        strategyRepository.storeStrategyAwardSearchRateTable(key,shuffleStrategyAwardSearchRateTable.size(),shuffleStrategyAwardSearchRateTable);
        log.info("策略奖品表:{}",shuffleStrategyAwardSearchRateTable);
        return true;
    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        int rateRange = strategyRepository.getRateRange(strategyId);
        return strategyRepository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeight) {
        String key=String.valueOf(strategyId).concat("_").concat(ruleWeight);
        int rateRange = strategyRepository.getRateRange(key);
        return strategyRepository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }
}
