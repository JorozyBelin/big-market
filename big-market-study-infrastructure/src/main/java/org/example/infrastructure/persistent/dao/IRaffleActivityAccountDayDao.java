package org.example.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivityAccountDay;

/**
 * 抽奖活动账户表-日次数
 */
@Mapper
public interface IRaffleActivityAccountDayDao {
    /**
     * 减去抽奖活动账户表-日次数
     * @param build
     * @return
     */
    int updateActivityAccountDaySubtractionQuota(RaffleActivityAccountDay build);

    /**
     * 添加抽奖活动账户表-日次数
     * @param build
     */
    void insertActivityAccountDay(RaffleActivityAccountDay build);

    /**
     * 查询抽奖活动账户表-日次数
     * @param raffleActivityAccountDayReq
     * @return
     */
    @DBRouter
    RaffleActivityAccountDay queryActivityAccountDayByUserId(RaffleActivityAccountDay raffleActivityAccountDayReq);

    /**
     * 查询活动ID
     * @param strategyId
     * @return
     */
    @DBRouter
    Long queryActivityIdByStrategyId(Long strategyId);

    /**
     * 查询用户参与活动次数
     * @param raffleActivityAccountDay
     * @return
     */
    @DBRouter
    Integer queryActivityAccountDayPartakeCount(RaffleActivityAccountDay raffleActivityAccountDay);

    /**
     * 添加用户参与活动次数
     * @param raffleActivityAccountDay
     */
    void addAccountQuota(RaffleActivityAccountDay raffleActivityAccountDay);
}
