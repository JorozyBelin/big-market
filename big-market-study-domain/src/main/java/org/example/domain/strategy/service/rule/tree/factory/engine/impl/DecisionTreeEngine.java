package org.example.domain.strategy.service.rule.tree.factory.engine.impl;

import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import org.example.domain.strategy.model.vo.RuleTreeNodeLineVO;
import org.example.domain.strategy.model.vo.RuleTreeNodeVO;
import org.example.domain.strategy.model.vo.RuleTreeVO;
import org.example.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.example.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;

import java.util.Map;

@Slf4j
public class DecisionTreeEngine implements IDecisionTreeEngine {

    private final Map<String, ILogicTreeNode> treeNodeGroup;

    private final RuleTreeVO ruleTreeVO;

    public DecisionTreeEngine(Map<String, ILogicTreeNode> treeNodeMap, RuleTreeVO ruleTreeVO) {
        this.treeNodeGroup = treeNodeMap;
        this.ruleTreeVO = ruleTreeVO;
    }

    /**
     * 获取决策树结果
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @return
     */
    @Override
    public DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId, Date endDatetime) {
        DefaultTreeFactory.StrategyAwardVO strategyAwardData = null;

        //1. 获取根节点、节点信息
        String nextNode = ruleTreeVO.getTreeRootRuleNode();
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();
        //2、获取起始节点
        RuleTreeNodeVO ruleTreeNodeVO = treeNodeMap.get(nextNode);
        while (ruleTreeNodeVO != null) {
            //获取决策树节点
            ILogicTreeNode logicTreeNode = treeNodeGroup.get(ruleTreeNodeVO.getRuleKey());
            String ruleValue = ruleTreeNodeVO.getRuleValue();
            //计算决策树节点
            DefaultTreeFactory.TreeActionEntity treeActionEntity = logicTreeNode.logic(userId, strategyId, awardId, ruleValue, endDatetime);
            RuleLogicCheckTypeVO ruleLogicCheckType = treeActionEntity.getRuleLogicCheckType();
            strategyAwardData = treeActionEntity.getStrategyAwardVO();
            log.info("决策树引擎【{}】treeId:{} node:{} code:{}", ruleTreeVO.getTreeName(), ruleTreeVO.getTreeId(), nextNode, ruleLogicCheckType.getCode());
            //获取下一个节点
            nextNode = nextNode(ruleLogicCheckType.getCode(), ruleTreeNodeVO.getTreeNodeLineVOList());
            ruleTreeNodeVO = treeNodeMap.get(nextNode);
        }
        return strategyAwardData;
    }
    /**
     * 找出下一个节点
     *
     * @param matterValue        状态参数，判断当前是否被接管或是否放行
     * @param treeNodeLineVOList 节点连线
     * @return 下一个节点
     */
    public String nextNode(String matterValue, List<RuleTreeNodeLineVO> treeNodeLineVOList) {
        if (treeNodeLineVOList == null || treeNodeLineVOList.isEmpty()) return null;
        for (RuleTreeNodeLineVO ruleTreeNodeLineVO : treeNodeLineVOList) {
            if (decisionLogic(matterValue, ruleTreeNodeLineVO)) {
                return ruleTreeNodeLineVO.getRuleNodeTo();
            }
        }
        //throw new RuntimeException("决策树引擎，nextNode 计算失败，未找到可执行节点！");
        return null;
    }

    /**
     * 决策逻辑
     *
     * @param matterValue        状态参数，判断当前是否被接管或是否放行
     * @param ruleTreeNodeLineVO 节点连线
     * @return 是否通过
     */
    private boolean decisionLogic(String matterValue, RuleTreeNodeLineVO ruleTreeNodeLineVO) {
        switch (ruleTreeNodeLineVO.getRuleLimitType()) {
            case EQUAL:
                return matterValue.equals(ruleTreeNodeLineVO.getRuleLimitValue().getCode());
            // 以下规则暂时不需要实现
            case GT:
            case LT:
            case GE:
            case LE:
            default:
                return false;
        }

    }
}
