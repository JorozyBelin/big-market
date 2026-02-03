package org.example.domain.strategy.service.armory;
/**
 * 策略仓库:负责初始化策略计算
 */

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.repository.IStrategyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;


@Service
@Slf4j
public class StrategyArmory implements IStrategyArmory {
    @Autowired
    private IStrategyRepository strategyRepository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        //1、查询策略配置
        List<StrategyAwardEntity> strategyAwardEntities = strategyRepository.queryStrategyAwardList(strategyId);
        if(strategyAwardEntities==null||strategyAwardEntities.isEmpty()) return false;
        //2、获取策略奖品列表中奖品中奖概率最小值
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardCountRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        //3、获取概率值总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardCountRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("策略奖品列表中奖品中奖概率最小值为:{}",minAwardRate);
        log.info("策略奖品列表中奖品中奖概率总和为:{}",totalAwardRate);
        //4、获取概率范围分段
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);
        //5、生成策略奖品表
        List<Integer> strategyAwardTable = new ArrayList<>();
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            BigDecimal awardCountRate = strategyAwardEntity.getAwardCountRate();
            for(int i=0;i<rateRange.multiply(awardCountRate).setScale(0,RoundingMode.CEILING).intValue();i++){
                strategyAwardTable.add(awardId);
            }
        }
        //6、乱序排序
        Collections.shuffle(strategyAwardTable);
        // 7. 生成出Map集合，key值，对应的就是后续的概率值。通过概率来获得对应的奖品ID
        Map<Integer, Integer> shuffleStrategyAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardTable.size(); i++) {
            shuffleStrategyAwardSearchRateTable.put(i, strategyAwardTable.get(i));
        }
        //8、存放到redis中
        strategyRepository.storeStrategyAwardSearchRateTable(strategyId,shuffleStrategyAwardSearchRateTable.size(),shuffleStrategyAwardSearchRateTable);
        log.info("策略奖品表:{}",shuffleStrategyAwardSearchRateTable);
        return true;
    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        int rateRange = strategyRepository.getRateRange(strategyId);
        return strategyRepository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }
}
