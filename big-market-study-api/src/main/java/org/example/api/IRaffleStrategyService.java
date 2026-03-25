package org.example.api;

import org.example.api.dto.*;
import org.example.types.model.Response;

import java.util.List;

public interface IRaffleStrategyService {
    /**
     * 抽奖策略装配
     * @param strategyId 策略ID
     * @return 策略是否装配成功
     */
    Response<Boolean> strategyArmory(Long strategyId);

    /**
     * 查询策略奖品列表
     * @param raffleAwardListRequestDTO 查询参数
     * @return 策略奖品列表
     */
    Response<List<RaffleAwardListResponseDTO>>queryRaffleAwardList(RaffleAwardListRequestDTO raffleAwardListRequestDTO);

    /**
     * 查询策略抽奖规则权重
     * @param raffleStrategyRuleWeightRequestDTO 抽奖参数
     * @return 抽奖结果
     */
    Response<List<RaffleStrategyRuleWeightResponseDTO>> queryRaffleStrategyRuleWeight(RaffleStrategyRuleWeightRequestDTO raffleStrategyRuleWeightRequestDTO);
    /**
     * 随机抽奖
     * @param raffleStrategyRequestDTO 抽奖参数
     * @return 抽奖结果
     */
    Response<RaffleStrategyResponseDTO> randomRaffle(RaffleStrategyRequestDTO raffleStrategyRequestDTO);
}
