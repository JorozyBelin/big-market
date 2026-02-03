package org.example.infrastructure.persistent.po;
/**
 * 奖品表
 */
import lombok.Data;

import java.util.Date;

@Data
public class Award {
    /**自增id */
    private Long id;
    /**抽奖奖品ID*/
    private Integer awardId;
    /**奖品对接标识 */
    private String awardKey;
    /**奖品配置信息 */
    private String awardConfig;
    /**奖品内容描述 */
    private String awardDesc;
    /**创建时间 */
    private Date createTime;
    /**更新时间 */
    private Date updateTime;
}
