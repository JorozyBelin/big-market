package org.example.api;

import org.example.api.dto.ActivityDrawRequestDTO;
import org.example.api.dto.ActivityDrawResponseDTO;
import org.example.api.dto.UserActivityAccountRequestDTO;
import org.example.api.dto.UserActivityAccountResponseDTO;
import org.example.types.model.Response;

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
}