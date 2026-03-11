package org.example.trigger.http;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.api.IRaffleService;
import org.example.api.dto.RaffleAwardListDTO;
import org.example.api.dto.RaffleDTO;
import org.example.api.vo.RaffleAwardListVO;
import org.example.api.vo.RaffleVO;
import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.service.IRaffleAward;
import org.example.domain.strategy.service.IRaffleStrategy;
import org.example.domain.strategy.service.armory.IStrategyArmory;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.example.types.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle")
@Slf4j
public class RaffleController implements IRaffleService {
    @Autowired
    private IStrategyArmory strategyArmory;
    @Autowired
    private IRaffleAward raffleAward;
    @Autowired
    private IRaffleStrategy raffleStrategy;

    @Override
    @GetMapping("/strategy_armory")
    public Response<Boolean> strategyArmory(@RequestParam Long strategyId) {
        try {
            log.info("抽奖策略装配开始 strategyId：{}", strategyId);
            boolean armoryStatus = strategyArmory.assembleLotteryStrategy(strategyId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(armoryStatus)
                    .build();
            log.info("抽奖策略装配完成 strategyId：{} response: {}", strategyId, JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            log.info("抽奖策略装配失败，strategyId:{}", strategyId);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @Override
    @PostMapping("/query_raffle_award_list")
    public Response<List<RaffleAwardListVO>> queryRaffleAwardList(@RequestBody RaffleAwardListDTO raffleAwardListDTO) {
        try {
            log.info("查询奖品列表,raffleAwardListDto:{}", JSON.toJSONString(raffleAwardListDTO));
            List<StrategyAwardEntity> strategyAwardEntities = raffleAward.queryStrategyAwardList(raffleAwardListDTO.getStrategyId());
            ArrayList<RaffleAwardListVO> raffleAwardListVO = new ArrayList<>(strategyAwardEntities.size());
            for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
                raffleAwardListVO.add(RaffleAwardListVO.builder()
                        .awardId(strategyAwardEntity.getAwardId())
                        .awardTitle(strategyAwardEntity.getAwardTitle())
                        .awardSubtitle(strategyAwardEntity.getAwardSubtitle())
                        .sort(strategyAwardEntity.getSort())
                        .build());
            }
            log.info("查询奖品列表完成,raffleAwardListVO:{}", JSON.toJSONString(raffleAwardListVO));
            return Response.<List<RaffleAwardListVO>>builder().code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(raffleAwardListVO)
                    .build();
        } catch (Exception e) {
            log.error("查询奖品列表失败,raffleAwardListDto:{}", JSON.toJSONString(raffleAwardListDTO));
            return Response.<List<RaffleAwardListVO>>builder().code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @Override
    @PostMapping("/random_raffle")
    public Response<RaffleVO> randomRaffle(@RequestBody RaffleDTO raffleDTO) {
        try {
            log.info("随机抽奖开始 strategyId: {}", raffleDTO.getStrategyId());
            // 调用抽奖接口
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                    .userId("system")
                    .strategyId(raffleDTO.getStrategyId())
                    .build());
            // 封装返回结果
            Response<RaffleVO> response = Response.<RaffleVO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(RaffleVO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardIndex(raffleAwardEntity.getSort())
                            .build())
                    .build();
            log.info("随机抽奖完成 strategyId: {} response: {}", raffleDTO.getStrategyId(), JSON.toJSONString(response));
            return response;

        } catch (AppException e) {
            log.error("随机抽奖失败 strategyId：{} {}", raffleDTO.getStrategyId(), e.getInfo());
            return Response.<RaffleVO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("随机抽奖失败 strategyId：{}", raffleDTO.getStrategyId(), e);
            return Response.<RaffleVO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }
}
