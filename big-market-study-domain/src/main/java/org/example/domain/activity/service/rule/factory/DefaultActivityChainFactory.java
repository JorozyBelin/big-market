package org.example.domain.activity.service.rule.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.activity.service.rule.IActionChain;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class DefaultActivityChainFactory {

    private final IActionChain activityChain;

    public DefaultActivityChainFactory(Map<String, IActionChain> activityChainMap) {
        activityChain = activityChainMap.get("activity_base_action");
        activityChain.appendNext(activityChainMap.get("activity_sku_stock_action"));
    }
     public IActionChain openActivityChain(){
        return activityChain;
     }

    @Getter
    @AllArgsConstructor
    public enum ActionModel {

        activity_base_action("activity_base_action", "活动的库存、时间校验"),
        activity_sku_stock_action("activity_sku_stock_action", "活动sku库存"),
        ;

        private final String code;
        private final String info;

    }

}
