package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RuleTreeNode;

import java.util.List;

@Mapper
public interface IRuleTreeNodeDao {

    List<RuleTreeNode> queryRuleTreeNodeListByTreeId(String treeId);

    /**
     * 查询锁住的规则树节点
     * @param treeIds
     * @return
     */
    List<RuleTreeNode> queryAwardRuleLockCount(String[] treeIds);
}
