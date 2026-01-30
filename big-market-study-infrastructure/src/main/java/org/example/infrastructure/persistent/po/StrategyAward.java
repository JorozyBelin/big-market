package org.example.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class StrategyAward {
    /**自增ID*/
    private Long id;
    /**策略ID*/
    private Long strategyId;
    /**抽奖奖品ID*/
    private Long awardId;
    /**抽奖奖品标题*/
    private String awardTitle;
    /**抽奖奖品副标题*/
    private String awardSubtitle;
    /**奖品库存总量*/
    private Long awardCount;
    /**奖品库存剩余*/
    private Long awardCountSurplus;
    /**奖品中奖概率*/
    private Double awardCountRate;
    /**排序*/
    private Long sort;
    /**创建时间*/
    private Date createTime;
    /**更新时间*/
    private Date updateTime;
}
