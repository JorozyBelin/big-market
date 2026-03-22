package org.example.api.dto;


import lombok.Data;

/**
 * 抽奖奖品列表
 */
@Data
    public class RaffleAwardListDTO {

        // 抽奖策略ID
        private Long strategyId;

        private Long activityId;
        private String userId;
    }


