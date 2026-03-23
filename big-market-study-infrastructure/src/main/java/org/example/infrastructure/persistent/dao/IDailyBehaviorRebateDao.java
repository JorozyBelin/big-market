package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.DailyBehaviorRebate;

import java.util.List;

@Mapper
public interface IDailyBehaviorRebateDao {
    /**
     * 查询日常行为返利配置
     * @param behaviorType 行为类型
     * @return
     */
    List<DailyBehaviorRebate> queryDailyBehaviorRebateByBehaviorType(String behaviorType);
}
