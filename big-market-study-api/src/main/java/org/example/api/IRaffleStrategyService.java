package org.example.api;

import org.example.api.dto.RaffleAwardListDTO;
import org.example.api.dto.RaffleStrategyDTO;
import org.example.api.vo.RaffleAwardListVO;
import org.example.api.vo.RaffleStrategyVO;
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
     * @param raffleAwardListDTO 查询参数
     * @return 策略奖品列表
     */
    Response<List<RaffleAwardListVO>>queryRaffleAwardList(RaffleAwardListDTO raffleAwardListDTO);

    /**
     * 随机抽奖
     * @param raffleStrategyDTO 抽奖参数
     * @return 抽奖结果
     */
    Response<RaffleStrategyVO> randomRaffle(RaffleStrategyDTO raffleStrategyDTO);
}
