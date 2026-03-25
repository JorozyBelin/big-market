package org.example.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.UserBehaviorRebateOrder;

import java.util.List;

@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserBehaviorRebateOrderDao {
    /**
     * 插入返利订单
     * @param userBehaviorRebateOrder 返利订单
     */
    void insert(UserBehaviorRebateOrder userBehaviorRebateOrder);

    /**
     * 根据业务单号查询返利订单
     * @param userBehaviorRebateOrderReq 业务单号
     * @return 返利订单
     */
    @DBRouter
    List<UserBehaviorRebateOrder> queryUserBehaviorRebateOrderByOutBusinessNo(UserBehaviorRebateOrder userBehaviorRebateOrderReq);
}
