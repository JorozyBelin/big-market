package org.example.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivityAccountMonth;

/**
 * 抽奖活动账户表-月次数
 */
@Mapper
public interface IRaffleActivityAccountMonthDao {
    /**
     * 减去抽奖活动账户表-月次数
     *
     * @param build
     * @return
     */
    int updateActivityAccountMonthSubtractionQuota(RaffleActivityAccountMonth build);

    /**
     * 添加抽奖活动账户表-月次数
     *
     * @param build
     * @return
     */
    void insertActivityAccountMonth(RaffleActivityAccountMonth build);

    /**
     * 查询抽奖活动账户表-月次数
     *
     * @param raffleActivityAccountMonthReq
     * @return
     */
    @DBRouter
    RaffleActivityAccountMonth queryActivityAccountMonthByUserId(RaffleActivityAccountMonth raffleActivityAccountMonthReq);

    /**
     * 添加抽奖活动账户表-月次数
     *
     * @param raffleActivityAccountMonth
     * @return
     */
    void addAccountQuota(RaffleActivityAccountMonth raffleActivityAccountMonth);
}
