package org.example.domain.strategy.service;

import org.example.domain.strategy.model.vo.RuleWeightVO;

import java.util.List;
import java.util.Map;

public interface IRaffleRule {
    /**
     * 查询奖品规则模型锁数量
     *
     * @param treeIds 规则模型ID
     * @return 奖品规则模型锁数量
     */
    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);

    /**
     * 查询奖品权重配置
     *
     * @param strategyId 策略ID
     * @return 权重规则
     */
    List<RuleWeightVO> queryAwardRuleWeight(Long strategyId);


    /**
     * 查询奖品权重配置
     *
     * @param activityId 活动ID
     * @return 权重规则
     */
    List<RuleWeightVO> queryAwardRuleWeightByActivityId(Long activityId);

}
