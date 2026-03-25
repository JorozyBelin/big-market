package org.example.api.dto;

import lombok.Data;

/**
 * 用户活动账户请求对象
 */
@Data
public class UserActivityAccountRequestDTO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

}
