package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.UserAwardRecord;

/**
 * 用户中奖记录表
 */
@Mapper
public interface IUserAwardRecordDao {
    /**
     * 插入用户中奖记录
     * @param userAwardRecord
     */
    void insert(UserAwardRecord userAwardRecord);
}
