package org.example.domain.activity.service.armory;

import java.util.Date;

public interface IActivityDispatch {
    /**
     * 扣减活动商品库存
     *
     * @param sku         商品SKU
     * @param endDateTime 活动结束时间
     * @return 扣减结果
     */
    boolean subtractionActivitySkuStock(Long sku, Date endDateTime);
}
