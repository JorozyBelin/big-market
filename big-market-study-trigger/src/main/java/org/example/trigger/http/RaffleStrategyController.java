package org.example.trigger.http;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.example.api.IRaffleStrategyService;
import org.example.api.dto.*;
import org.example.domain.activity.service.IRaffleActivityPartakeService;
import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.model.vo.RuleWeightVO;
import org.example.domain.strategy.service.IRaffleAward;
import org.example.domain.strategy.service.IRaffleRule;
import org.example.domain.strategy.service.IRaffleStrategy;
import org.example.domain.strategy.service.armory.IStrategyArmory;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.example.types.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle")
@Slf4j
public class RaffleStrategyController implements IRaffleStrategyService {
    @Autowired
    private IStrategyArmory strategyArmory;
    @Autowired
    private IRaffleAward raffleAward;
    @Autowired
    private IRaffleStrategy raffleStrategy;
    @Autowired
    private IRaffleRule raffleRule;
    @Autowired
    private IRaffleActivityPartakeService raffleActivityPartakeService;

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
    public Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(@RequestBody RaffleAwardListRequestDTO raffleAwardListRequestDTO) {
        try {
            log.info("查询奖品列表,raffleAwardListDto:{}", JSON.toJSONString(raffleAwardListRequestDTO));
            //1、参数校验
            if(null== raffleAwardListRequestDTO.getActivityId()||StringUtils.isBlank(raffleAwardListRequestDTO.getUserId())){
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            //2、查询奖品配置
            List<StrategyAwardEntity> strategyAwardEntities = raffleAward.queryStrategyAwardListByActivityId(raffleAwardListRequestDTO.getActivityId());
            //3、获取规则配置
            String[] treeIds = strategyAwardEntities.stream()
                    .map(StrategyAwardEntity::getRuleModels)
                    .filter(ruleModels -> !ruleModels.isEmpty() && ruleModels != null)
                    .toArray(String[]::new);
            //4、查询规则配置-获取奖品解锁次数
            Map<String, Integer> awardRuleLockCount = raffleRule.queryAwardRuleLockCount(treeIds);
            //5、查询用户参与次数
            Integer dayPartakeCount = raffleActivityPartakeService.queryRaffleActivityAccountDayPartakeCount(raffleAwardListRequestDTO.getUserId(), raffleAwardListRequestDTO.getActivityId());
            ArrayList<RaffleAwardListResponseDTO> raffleAwardListResponseDTO = new ArrayList<>(strategyAwardEntities.size());
            for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
                Integer awardRaffleLockCount = awardRuleLockCount.get(strategyAwardEntity.getRuleModels());
                raffleAwardListResponseDTO.add(RaffleAwardListResponseDTO.builder()
                        .awardId(strategyAwardEntity.getAwardId())
                        .awardTitle(strategyAwardEntity.getAwardTitle())
                        .awardSubtitle(strategyAwardEntity.getAwardSubtitle())
                        .sort(strategyAwardEntity.getSort())
                        .awardRuleLockCount(awardRaffleLockCount)
                        .isAwardUnlock(awardRaffleLockCount == null || dayPartakeCount >= awardRaffleLockCount)
                        .waitUnlockCount(awardRaffleLockCount == null || dayPartakeCount >= awardRaffleLockCount ? 0 : dayPartakeCount - awardRaffleLockCount)
                        .build());
            }
            Response<List<RaffleAwardListResponseDTO>> response = Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(raffleAwardListResponseDTO)
                    .build();
            log.info("查询抽奖奖品列表配置完成 userId:{} activityId：{} response: {}", raffleAwardListRequestDTO.getUserId(), raffleAwardListRequestDTO.getActivityId(), JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            log.error("查询奖品列表失败,raffleAwardListDto:{}", JSON.toJSONString(raffleAwardListRequestDTO));
            return Response.<List<RaffleAwardListResponseDTO>>builder().code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @Override
    public Response<List<RaffleStrategyRuleWeightResponseDTO>> queryRaffleStrategyRuleWeight(@RequestBody RaffleStrategyRuleWeightRequestDTO raffleStrategyRuleWeightRequestDTO) {
        try {
            log.info("查询抽奖策略权重规则配置开始 userId:{} activityId：{}", raffleStrategyRuleWeightRequestDTO.getUserId(), raffleStrategyRuleWeightRequestDTO.getActivityId());
            // 1. 参数校验
            if (StringUtils.isBlank(raffleStrategyRuleWeightRequestDTO.getUserId()) || null == raffleStrategyRuleWeightRequestDTO.getActivityId()) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            // 2. 查询用户抽奖总次数
            Integer userActivityAccountTotalUseCount = raffleActivityPartakeService.queryRaffleActivityAccountDayPartakeCount(raffleStrategyRuleWeightRequestDTO.getUserId(),raffleStrategyRuleWeightRequestDTO.getActivityId());
            // 3. 查询规则
            List<RaffleStrategyRuleWeightResponseDTO> raffleStrategyRuleWeightList = new ArrayList<>();
            List<RuleWeightVO> ruleWeightVOList = raffleRule.queryAwardRuleWeightByActivityId(raffleStrategyRuleWeightRequestDTO.getActivityId());
            for (RuleWeightVO ruleWeightVO : ruleWeightVOList) {
                // 转换对象
                List<RaffleStrategyRuleWeightResponseDTO.StrategyAward> strategyAwards = new ArrayList<>();
                List<RuleWeightVO.Award> awardList = ruleWeightVO.getAwardList();
                for (RuleWeightVO.Award award : awardList) {
                    RaffleStrategyRuleWeightResponseDTO.StrategyAward strategyAward = new RaffleStrategyRuleWeightResponseDTO.StrategyAward();
                    strategyAward.setAwardId(award.getAwardId());
                    strategyAward.setAwardTitle(award.getAwardTitle());
                    strategyAwards.add(strategyAward);
                }
                // 封装对象
                RaffleStrategyRuleWeightResponseDTO raffleStrategyRuleWeightResponseDTO = new RaffleStrategyRuleWeightResponseDTO();
                raffleStrategyRuleWeightResponseDTO.setRuleWeightCount(ruleWeightVO.getWeight());
                raffleStrategyRuleWeightResponseDTO.setStrategyAwards(strategyAwards);
                raffleStrategyRuleWeightResponseDTO.setUserActivityAccountTotalUseCount(userActivityAccountTotalUseCount);

                raffleStrategyRuleWeightList.add(raffleStrategyRuleWeightResponseDTO);
            }
            Response<List<RaffleStrategyRuleWeightResponseDTO>> response = Response.<List<RaffleStrategyRuleWeightResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(raffleStrategyRuleWeightList)
                    .build();
            log.info("查询抽奖策略权重规则配置完成 userId:{} activityId：{} response: {}", raffleStrategyRuleWeightRequestDTO.getUserId(), raffleStrategyRuleWeightRequestDTO.getActivityId(), JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            log.error("查询抽奖策略权重规则配置失败 userId:{} activityId：{}", raffleStrategyRuleWeightRequestDTO.getUserId(), raffleStrategyRuleWeightRequestDTO.getActivityId(), e);
            return Response.<List<RaffleStrategyRuleWeightResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }

    @Override
    @PostMapping("/random_raffle")
    public Response<RaffleStrategyResponseDTO> randomRaffle(@RequestBody RaffleStrategyRequestDTO raffleStrategyRequestDTO) {
        try {
            log.info("随机抽奖开始 strategyId: {}", raffleStrategyRequestDTO.getStrategyId());
            // 调用抽奖接口
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                    .userId(raffleStrategyRequestDTO.getUserId())
                    .strategyId(raffleStrategyRequestDTO.getStrategyId())
                    .build());
            // 封装返回结果
            Response<RaffleStrategyResponseDTO> response = Response.<RaffleStrategyResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(RaffleStrategyResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardIndex(raffleAwardEntity.getSort())
                            .build())
                    .build();
            log.info("随机抽奖完成 strategyId: {} response: {}", raffleStrategyRequestDTO.getStrategyId(), JSON.toJSONString(response));
            return response;

        } catch (AppException e) {
            log.error("随机抽奖失败 strategyId：{} {}", raffleStrategyRequestDTO.getStrategyId(), e.getInfo());
            return Response.<RaffleStrategyResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("随机抽奖失败 strategyId：{}", raffleStrategyRequestDTO.getStrategyId(), e);
            return Response.<RaffleStrategyResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }
}
