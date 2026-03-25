package org.example.domain.rebate.service;

import org.example.domain.rebate.model.entity.BehaviorEntity;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;

import java.util.List;

/**
 * 行为返利接口：用户参加活动行为，创建返利订单
 */
public interface IBehaviorRebateService {

    /**
     * 创建订单
     * @param behaviorEntity 行为实体对象
     * @return  订单编号
     */
    List<String> createOrder(BehaviorEntity behaviorEntity);

    /**
     * 查询返利订单
     * @param userId 用户ID
     * @param outBusinessNo 业务编号
     * @return  返利订单列表
     */
    List<BehaviorRebateOrderEntity> queryBehaviorRebateOrder(String userId,String outBusinessNo);
}
