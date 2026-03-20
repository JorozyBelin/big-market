package org.example.domain.award.service;

import org.example.domain.award.model.entity.UserAwardRecordEntity;

/**
 * 奖品服务接口
 */
public interface IAwardService {
    /**
     * 保存用户奖品记录
     * @param userAwardRecordEntity 用户奖品记录
     */
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity);
}
