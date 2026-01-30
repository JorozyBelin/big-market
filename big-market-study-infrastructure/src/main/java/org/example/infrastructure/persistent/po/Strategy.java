package org.example.infrastructure.persistent.po;
/**
 * 策略表
 */

import lombok.Data;

import java.util.Date;

/**
 * 策略表
 */
@Data
public class Strategy {
    /**自增id */
    private Long id;
    /**抽奖策略id */
    private Long strategyId;
    /**抽奖策略描述*/
    private String strategyDesc;
    /**策略模型*/
    private String ruleModel;
    /**创建时间 */
    private Date createTime;
    /**更新时间 */
    private Date updateTime;
}