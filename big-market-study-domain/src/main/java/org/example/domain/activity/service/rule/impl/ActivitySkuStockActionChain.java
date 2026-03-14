package org.example.domain.activity.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.model.entity.ActivityCountEntity;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;
import org.example.domain.activity.service.rule.AbstractActionChain;
import org.springframework.stereotype.Component;

@Component("activity_sku_stock_action")
@Slf4j
public class ActivitySkuStockActionChain extends AbstractActionChain {
    @Override
    public Boolean doAction(ActivitySkuEntity actionSkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始。");
        return true;
    }
}
