package org.example.domain.rebate.service;

import org.example.domain.rebate.model.entity.BehaviorEntity;

import java.util.List;

public interface IBehaviorRebateService {

    /**
     * 创建订单
     * @param behaviorEntity 行为实体对象
     * @return  订单编号
     */
    List<String> createOrder(BehaviorEntity behaviorEntity);
}
