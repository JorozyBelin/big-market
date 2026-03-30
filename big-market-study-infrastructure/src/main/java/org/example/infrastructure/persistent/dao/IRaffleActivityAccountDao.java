package org.example.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivityAccount;

@Mapper
public interface IRaffleActivityAccountDao {

    /**
     * 创建活动账户
     *
     * @param raffleActivityAccount
     */
    void insert(RaffleActivityAccount raffleActivityAccount);

    /**
     * 更新活动账户的图片额度
     *
     * @param raffleActivityAccount
     * @return
     */
    int updateAccountQuota(RaffleActivityAccount raffleActivityAccount);

    /**
     * 减去活动账户的图片额度
     *
     * @param raffleActivityAccount
     * @return
     */
    int updateActivityAccountSubtractionQuota(RaffleActivityAccount raffleActivityAccount);

    /**
     * 更新活动账户的月剩余图片额度
     *
     * @param build
     */
    void updateActivityAccountMonthSurplusImageQuota(RaffleActivityAccount build);

    /**
     * 减去活动账户的月剩余图片额度
     *
     * @param build
     */
    void updateActivityAccountDaySurplusImageQuota(RaffleActivityAccount build);

    /**
     * 根据用户ID查询活动账户
     *
     * @param raffleActivityAccountReq
     * @return
     */
    @DBRouter
    RaffleActivityAccount queryActivityAccountByUserId(RaffleActivityAccount raffleActivityAccountReq);

    /**
     * 根据活动ID查询活动账户
     *
     * @param raffleActivityAccountReq
     * @return
     */
    @DBRouter
    RaffleActivityAccount queryActivityAccount(RaffleActivityAccount raffleActivityAccountReq);

    RaffleActivityAccount queryAccountByUserId(RaffleActivityAccount raffleActivityAccount);
}

