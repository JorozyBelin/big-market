package org.example.infrastructure.persistent.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.example.infrastructure.persistent.po.Award;

import java.util.List;

@Mapper
public interface AwardDao {

    List<Award> queryAwardList();

    /**
     * 根据奖品ID查询奖品配置
     * @param awardId 奖品ID
     * @return 奖品配置
     */
    String queryAwardConfigByAwardId(Integer awardId);
}
