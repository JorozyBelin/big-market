package org.example.domain.award.repository;

import org.example.domain.award.model.aggregate.GiveOutPrizesAggregate;
import org.example.domain.award.model.aggregate.UserAwardRecordAggregate;

public interface IAwardRepository {
    /**
     * 保存用户奖品记录
     * @param userAwardRecordAggregate 用户奖品记录和任务聚合
     */
    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);

    /**
     * 根据奖品ID查询奖品配置
     * @param awardId 奖品ID
     * @return 奖品配置
     */
    String queryAwardConfig(Integer awardId);

    /**
     * 保存发放奖品聚合
     * @param giveOutPrizesAggregate
     */
    void saveGiveOutPrizesAggregate(GiveOutPrizesAggregate giveOutPrizesAggregate);

    /**
     * 根据奖品ID查询奖品Key
     * @param awardId 奖品ID
     * @return 奖品Key
     */
    String queryAwardKey(Integer awardId);
}
