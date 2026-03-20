package org.example.domain.award.repository;

import org.example.domain.award.model.aggregate.UserAwardRecordAggregate;

public interface IAwardRepository {
    /**
     * 保存用户奖品记录
     * @param userAwardRecordAggregate 用户奖品记录和任务聚合
     */
    void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate);
}
