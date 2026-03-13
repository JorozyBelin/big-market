package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.RaffleActivityAccount;
import org.example.infrastructure.persistent.po.RaffleActivityCount;

@Mapper
public interface IRaffleActivityCountDao {

    void insert(RaffleActivityAccount raffleActivityAccount);


    RaffleActivityCount queryRaffleActivityCountByActivityCountId(Long activityCountId);
}
