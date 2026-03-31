package org.example.domain.activity.service.quota.policy.impl;

import org.example.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import org.example.domain.activity.model.vo.OrderStateVO;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.quota.policy.ITradePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("credit_pay_trade")
public class CreditPayTradePolicy implements ITradePolicy {
    @Autowired
    private IActivityRepository activityRepository;

    @Override
    public void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        createQuotaOrderAggregate.setOrderState(OrderStateVO.wait_pay);
        activityRepository.doSaveCreditPayOrder(createQuotaOrderAggregate);
    }
}
