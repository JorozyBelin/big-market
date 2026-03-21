package org.example.domain.activity.service.armory;

/**
 * 活动配置接口
 */
public interface IActivityArmory {

    /**
     * 组装活动
     *
     * @param sku
     * @return
     */
   boolean assembleActivity(Long sku);

   /**
     * 批量组装活动
     *
     * @param activityId
     */
    boolean assembleActivitySkuByActivityId(Long activityId);
}
