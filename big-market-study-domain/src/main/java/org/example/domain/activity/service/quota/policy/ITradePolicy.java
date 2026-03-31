package org.example.domain.activity.service.quota.policy;

import org.example.domain.activity.model.aggregate.CreateQuotaOrderAggregate;

public interface ITradePolicy {
    /**
     * 交易接口
     * @param createQuotaOrderAggregate
     */
    void trade(CreateQuotaOrderAggregate createQuotaOrderAggregate);
}
