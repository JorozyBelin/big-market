package org.example.domain.activity.service.rule;

import org.example.domain.activity.model.entity.ActivityCountEntity;
import org.example.domain.activity.model.entity.ActivityEntity;
import org.example.domain.activity.model.entity.ActivitySkuEntity;

public interface IActionChain extends IActionChainArmory{
    /**
     * 执行动作
     * @param actionSkuEntity 动作sku
     * @param activityEntity 活动信息
     * @param activityCountEntity 活动次数信息
     * @return 是否执行成功
     */
    Boolean doAction(ActivitySkuEntity actionSkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);
}
