package org.example.api.dto;

import lombok.Data;

@Data
public class RaffleStrategyRequestDTO {
    // 抽奖策略ID
    private Long strategyId;
    // 用户ID
    private String userId;

}
