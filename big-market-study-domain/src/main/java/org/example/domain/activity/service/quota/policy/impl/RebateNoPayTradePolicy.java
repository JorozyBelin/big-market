package org.example.domain.activity.service.quota.policy.impl;

import org.example.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import org.example.domain.activity.model.vo.OrderStateVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.quota.policy.ITradePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("rebate_no_pay_trade")
public class RebateNoPayTradePolicy implements ITradePolicy {
    @Autowired
    private IActivityRepository activityRepository;

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        // 不需要支付则修改订单金额为0，状态为完成，直接给用户账户充值
        createQuotaOrderAggregate.setOrderState(OrderStateVO.completed);
        createQuotaOrderAggregate.getActivityOrderEntity().setPayAmount(BigDecimal.ZERO);
        activityRepository.doSaveOrder(createQuotaOrderAggregate);
    }
}
