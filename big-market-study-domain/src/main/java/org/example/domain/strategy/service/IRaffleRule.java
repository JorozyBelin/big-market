package org.example.domain.strategy.service;

import java.util.Map;

public interface IRaffleRule {
    /**
     * 查询奖品规则模型锁数量
     * @param treeIds 规则模型ID
     * @return 奖品规则模型锁数量
     */
    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);
}
