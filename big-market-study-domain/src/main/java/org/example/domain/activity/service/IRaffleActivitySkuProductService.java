package org.example.domain.activity.service;

import org.example.domain.activity.model.entity.SkuProductEntity;

import java.util.List;

public interface IRaffleActivitySkuProductService {
    /**
     * 查询活动商品列表
     * @param activityId 活动ID
     * @return 活动商品列表
     */
    List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId);
}
