package org.example.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.types.common.Constants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StrategyEntity {
    /**抽奖策略id */
    private Long strategyId;
    /**抽奖策略描述*/
    private String strategyDesc;
    /**策略模型*/
    private String ruleModel;

    public String[] ruleModel() {
        if (StringUtils.isBlank(ruleModel)) return null;
        return ruleModel.split(Constants.SPLIT);
    }

    public String getRuleWeight() {
        String[] ruleModels = this.ruleModel();
        for (String ruleModel : ruleModels) {
            if ("rule_weight".equals(ruleModel)) return ruleModel;
        }
        return null;
    }

}
