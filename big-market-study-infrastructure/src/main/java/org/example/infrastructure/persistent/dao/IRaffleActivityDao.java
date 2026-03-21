package org.example.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivity;

@Mapper
public interface IRaffleActivityDao {
    /**
     * 获取活动信息
     *
     * @param activityId 活动ID
     * @return 活动信息
     */
    RaffleActivity queryRaffleActivityByActivityId(Long activityId);

    /**
     * 获取活动策略
     *
     * @param activityId 活动ID
     * @return 策略ID
     */
    Long queryStrategyIdByActivityId(Long activityId);
}
