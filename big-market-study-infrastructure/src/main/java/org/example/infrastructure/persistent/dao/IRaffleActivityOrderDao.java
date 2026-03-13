package org.example.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivityOrder;

import java.util.List;

@Mapper
@DBRouterStrategy(splitTable = true)
public interface IRaffleActivityOrderDao {
    /**
     * 插入活动领取记录
     *
     * @param order 领取活动信息
     * @return      领取结果
     */
    @DBRouter(key = "userId")
    int insertActivityOrder(RaffleActivityOrder order);

    /**
     * 根据用户ID查询活动领取记录
     *
     * @param userId 用户ID
     * @return       领取结果
     */
    @DBRouter(key="userId")
    List<RaffleActivityOrder> queryRaffleActivityOrderByUserId(String userId);

}
