package org.example.domain.rebate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import org.example.domain.rebate.model.entity.TaskEntity;
import org.example.domain.rebate.event.SendRebateMessageEvent;
import org.example.domain.rebate.model.entity.BehaviorEntity;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.example.domain.rebate.model.vo.DailyBehaviorRebateVO;
import org.example.domain.rebate.model.vo.TaskStateVO;
import org.example.domain.rebate.repository.IBehaviorRebateRepository;
import org.example.types.common.Constants;
import org.example.types.event.BaseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BehaviorRebateService implements IBehaviorRebateService {
    @Autowired
    private IBehaviorRebateRepository behaviorRebateRepository;
    @Resource
    private SendRebateMessageEvent sendRebateMessageEvent;

    @Override
    public List<String> createOrder(BehaviorEntity behaviorEntity) {
        //1、查询返利配置
        List<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = behaviorRebateRepository.queryDailyBehaviorRebateConfig(behaviorEntity.getBehaviorTypeVO());
        if (dailyBehaviorRebateVOS == null || dailyBehaviorRebateVOS.isEmpty()) return new ArrayList<>();
        ArrayList<String> orderIds = new ArrayList<>();
        ArrayList<BehaviorRebateAggregate> behaviorRebateAggregates = new ArrayList<>();
        dailyBehaviorRebateVOS.forEach(dailyBehaviorRebateVO -> {
            String bizId = behaviorEntity.getUserId() + Constants.UNDERLINE + dailyBehaviorRebateVO.getRebateType() + Constants.UNDERLINE + behaviorEntity.getOutBusinessNo();
            //创建返利订单
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                    .behaviorType(dailyBehaviorRebateVO.getBehaviorType())
                    .orderId(RandomStringUtils.randomNumeric(12))
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .outBusinessNo(behaviorEntity.getOutBusinessNo())
                    .bizId(bizId)
                    .userId(behaviorEntity.getUserId())
                    .build();
            //保存返利订单
            orderIds.add(behaviorRebateOrderEntity.getOrderId());
            //创建返利消息
            SendRebateMessageEvent.RebateMessage rebateMessage = SendRebateMessageEvent.RebateMessage.builder()
                    .bizId(bizId)
                    .userId(behaviorEntity.getUserId())
                    .rebateConfig(dailyBehaviorRebateVO.getRebateConfig())
                    .rebateDesc(dailyBehaviorRebateVO.getRebateDesc())
                    .rebateType(dailyBehaviorRebateVO.getRebateType())
                    .build();
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> rebateMessageEventMessage = sendRebateMessageEvent.buildEventMessage(rebateMessage);
            //创建任务补偿
            TaskEntity taskEntity = TaskEntity.builder()
                    .topic(sendRebateMessageEvent.topic())
                    .state(TaskStateVO.create)
                    .messageId(rebateMessageEventMessage.getId())
                    .message(rebateMessageEventMessage)
                    .userId(behaviorEntity.getUserId())
                    .build();

            BehaviorRebateAggregate behaviorRebateAggregate = BehaviorRebateAggregate.builder()
                    .behaviorRebateOrderEntity(behaviorRebateOrderEntity)
                    .taskEntity(taskEntity)
                    .userId(behaviorEntity.getUserId())
                    .build();
            behaviorRebateAggregates.add(behaviorRebateAggregate);
        });
        //存储聚合对象订单信息
        behaviorRebateRepository.saveUserRebateRecord(behaviorEntity.getUserId(), behaviorRebateAggregates);

        return orderIds;

    }

    @Override
    public List<BehaviorRebateOrderEntity> queryBehaviorRebateOrder(String userId, String outBusinessNo) {
        return behaviorRebateRepository.queryUserBehaviorRebateOrderByOutBusinessNo(userId, outBusinessNo);
    }
}
