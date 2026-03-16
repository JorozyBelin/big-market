package org.example.domain.activity.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.activity.model.aggregate.CreateOrderAggregate;
import org.example.domain.activity.model.entity.*;
import org.example.domain.activity.repository.IActivityRepository;
import org.example.domain.activity.service.rule.IActionChain;
import org.example.domain.activity.service.rule.factory.DefaultActivityChainFactory;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;

@Slf4j
public abstract class AbstractRaffleActivity extends RaffleActivitySupport implements IRaffleOrder {

    public AbstractRaffleActivity(IActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
        super(activityRepository, defaultActivityChainFactory);
    }

    @Override
    public String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity) {
        //1、参数校验
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if (null == sku || StringUtils.isBlank(userId) || StringUtils.isBlank(outBusinessNo)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        //2、查询信息
        ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
        ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        ActivityCountEntity activityCountEntity = queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        //3、活动信息校验
        IActionChain activityChain = defaultActivityChainFactory.openActivityChain();
        activityChain.doAction(activitySkuEntity, activityEntity, activityCountEntity);

        // 4. 构建订单聚合对象
        CreateOrderAggregate createOrderAggregate = buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityEntity, activityCountEntity);

        // 5. 保存订单
        doSaveOrder(createOrderAggregate);

        // 6. 返回单号
        return createOrderAggregate.getActivityOrderEntity().getOrderId();

    }
    protected abstract CreateOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);

    protected abstract void doSaveOrder(CreateOrderAggregate createOrderAggregate);


}
