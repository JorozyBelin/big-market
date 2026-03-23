package org.example.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.UserBehaviorRebateOrder;

@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserBehaviorRebateOrderDao {
    /**
     * 插入返利订单
     * @param userBehaviorRebateOrder 返利订单
     */
    void insert(UserBehaviorRebateOrder userBehaviorRebateOrder);
}
