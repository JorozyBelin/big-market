package org.example.api;

import org.example.api.dto.*;
import org.example.types.model.Response;

import java.math.BigDecimal;
import java.util.List;

public interface IRaffleActivityService {

    /**
     * 活动装配，数据预热缓存
     *
     * @param activityId 活动ID
     * @return 装配结果
     */
    Response<Boolean> armory(Long activityId);

    /**
     * 活动抽奖接口
     *
     * @param request 请求对象
     * @return 返回结果
     */
    Response<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);

    /**
     * 签到返利
     *
     * @param userId 用户ID
     * @return 返回结果
     */
    Response<Boolean> calendarSignRebate(String userId);

    /**
     * 是否日历签到返利成功
     *
     * @param userId 用户ID
     * @return 签到返利结果
     */
    Response<Boolean> isCalendarSignRebate(String userId);

    /**
     * 查询用户活动账户信息
     *
     * @param request 请求对象
     * @return 账户信息
     */
    Response<UserActivityAccountResponseDTO> queryUserActivityAccount(UserActivityAccountRequestDTO request);

    /**
     * 查询活动商品列表
     *
     * @param activityId 活动ID
     * @return 商品列表
     */
    Response<List<SkuProductResponseDTO>> querySkuProductListByActivityId(Long activityId);

    /**
     *  查询用户积分账户
     * @param userId 用户ID
     * @return 积分
     */
    Response<BigDecimal> queryUserCreditAccount(String userId);

    /**
     * 积分支付兑换商品
     *
     * @param request 购物车请求对象
     * @return 兑换结果
     */
    Response<Boolean> creditPayExchangeSku(SkuProductShopCartRequestDTO  request);
}