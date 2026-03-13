package org.example.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivity;

@Mapper
@DBRouterStrategy(splitTable = true)
public interface IRaffleActivityDao {
    /**
     * 获取活动信息
     *
     * @param activityId 活动ID
     * @return 活动信息
     */
    @DBRouter(key = "userId")
    RaffleActivity queryRaffleActivityByActivityId(Long activityId);


}
