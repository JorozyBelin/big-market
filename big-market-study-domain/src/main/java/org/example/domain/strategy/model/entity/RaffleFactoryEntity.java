package org.example.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaffleFactoryEntity {
    /** 用户ID */
    private String userId;
    /** 策略ID */
    private Long strategyId;
}
