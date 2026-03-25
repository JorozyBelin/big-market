package org.example.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.UserRaffleOrder;

/**
 * 用户抽奖订单表
 */
@Mapper
@DBRouterStrategy(splitTable = true)
public interface IUserRaffleOrderDao {
    /**
     * 查询未使用的抽奖订单
     * @param userRaffleOrderReq
     * @return
     */
    @DBRouter
    UserRaffleOrder queryNoUsedRaffleOrder(UserRaffleOrder userRaffleOrderReq);

    /**
     * 插入用户抽奖订单
     * @param build
     */
    void insert(UserRaffleOrder build);

    /**
     * 更新用户抽奖订单
     * @param userId
     * @param activityId
     */
    void updateUserRaffleOrderState(String userId, Long activityId);
}
