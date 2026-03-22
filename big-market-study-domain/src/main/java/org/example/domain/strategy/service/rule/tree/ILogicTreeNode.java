package org.example.domain.strategy.service.rule.tree;

import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

import java.util.Date;

public interface ILogicTreeNode {
    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue, Date endDatetime);
}
