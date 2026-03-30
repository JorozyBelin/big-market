package org.example.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.award.model.aggregate.GiveOutPrizesAggregate;
import org.example.domain.award.model.aggregate.UserAwardRecordAggregate;
import org.example.domain.award.model.entity.TaskEntity;
import org.example.domain.award.model.entity.UserAwardRecordEntity;
import org.example.domain.award.model.entity.UserCreditAwardEntity;
import org.example.domain.award.model.vo.AccountStatusVO;
import org.example.domain.award.repository.IAwardRepository;
import org.example.infrastructure.persistent.dao.*;
import org.example.infrastructure.persistent.event.EventPublisher;
import org.example.infrastructure.persistent.po.Task;
import org.example.infrastructure.persistent.po.UserAwardRecord;
import org.example.infrastructure.persistent.po.UserCreditAccount;
import org.example.infrastructure.persistent.redis.IRedisService;
import org.example.types.common.Constants;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.redisson.api.RLock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class AwardRepository implements IAwardRepository {
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private IUserAwardRecordDao userAwardRecordDao;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private IUserRaffleOrderDao userRaffleOrderDao;
    @Resource
    private AwardDao awardDao;
    @Resource
    private IUserCreditAccountDao userCreditAccountDao;
    @Resource
    private IRedisService redisService;

    @Override
    public void saveUserAwardRecord(UserAwardRecordAggregate userAwardRecordAggregate) {
        UserAwardRecordEntity userAwardRecordEntity = userAwardRecordAggregate.getUserAwardRecordEntity();
        String userId = userAwardRecordEntity.getUserId();
        TaskEntity taskEntity = userAwardRecordAggregate.getTaskEntity();
        UserAwardRecord userAwardRecord = new UserAwardRecord();
        Long activityId = userAwardRecordEntity.getActivityId();
        Integer awardId = userAwardRecordEntity.getAwardId();
        Task task = new Task();

        userAwardRecord.setUserId(userId);
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());
        userAwardRecord.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        userAwardRecord.setAwardTime(userAwardRecordEntity.getAwardTime());
        userAwardRecord.setAwardId(awardId);
        userAwardRecord.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecord.setStrategyId(userAwardRecordEntity.getStrategyId());
        userAwardRecord.setActivityId(activityId);
        userAwardRecord.setAwardTitle(userAwardRecordEntity.getAwardTitle());

        task.setUserId(taskEntity.getUserId());
        task.setTopic(taskEntity.getTopic());
        task.setMessageId(taskEntity.getMessageId());
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setState(taskEntity.getState().getCode());

        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    // 写入记录
                    userAwardRecordDao.insert(userAwardRecord);
                    // 写入任务
                    taskDao.insert(task);
                    //更新用户订单状态
                    userRaffleOrderDao.updateUserRaffleOrderState(userId, activityId);
                    return 1;

                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入中奖记录，唯一索引冲突 userId: {} activityId: {} awardId: {}", userId, activityId, awardId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        } finally {
            dbRouter.clear();
        }
        try {
            //发送消息，在事务外执行，避免事务内数据回滚
            eventPublisher.publish(task.getTopic(), task.getMessage());
            //更新数据库,task任务表
            taskDao.updateTaskSendMessageComplete(task);
        } catch (Exception e) {
            log.error("写入中奖记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }

    }

    @Override
    public String queryAwardConfig(Integer awardId) {
        return awardDao.queryAwardConfigByAwardId(awardId);
    }

    @Override
    public void saveGiveOutPrizesAggregate(GiveOutPrizesAggregate giveOutPrizesAggregate) {
        String userId = giveOutPrizesAggregate.getUserId();
        UserCreditAwardEntity userCreditAwardEntity = giveOutPrizesAggregate.getUserCreditAwardEntity();
        UserAwardRecordEntity userAwardRecordEntity = giveOutPrizesAggregate.getUserAwardRecordEntity();
        //更新用户积分账户(首次创建账户)
        UserCreditAccount userCreditAccount = new UserCreditAccount();
        userCreditAccount.setUserId(userId);
        userCreditAccount.setTotalAmount(userCreditAwardEntity.getCreditAmount());
        userCreditAccount.setAvailableAmount(userCreditAwardEntity.getCreditAmount());
        userCreditAccount.setAccountStatus(AccountStatusVO.open.getCode());
        //更新用户中奖记录
        UserAwardRecord userAwardRecord = new UserAwardRecord();
        userAwardRecord.setUserId(userId);
        userAwardRecord.setOrderId(userAwardRecordEntity.getOrderId());
        userAwardRecord.setAwardState(userAwardRecordEntity.getAwardState().getCode());
        RLock lock = redisService.getLock(Constants.RedisKey.ACTIVITY_ACCOUNT_LOCK + userId);
        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    lock.lock(3, TimeUnit.SECONDS);
                    //1、更新用户积分账户，没有就创建账户
                    UserCreditAccount userCreditAccountRes = userCreditAccountDao.queryUserCreditAccount(userCreditAccount);
                    if (null == userCreditAccountRes) {
                        userCreditAccountDao.insert(userCreditAccount);
                    } else {
                        userCreditAccountDao.updateAddAmount(userCreditAccount);

                    }
                    //2、更新用户中奖记录
                    int updateUserAwardRecordCount = userAwardRecordDao.updateAwardRecordCompletedState(userAwardRecord);
                    if (updateUserAwardRecordCount == 0) {
                        log.warn("更新中奖记录，重复更新拦截 userId:{} giveOutPrizesAggregate:{}", userId, JSON.toJSONString(giveOutPrizesAggregate));
                        userAwardRecordDao.insert(userAwardRecord);
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("更新中奖记录，唯一索引冲突 userId: {} ", userId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        } finally {
            dbRouter.clear();
            lock.unlock();
        }

    }

    @Override
    public String queryAwardKey(Integer awardId) {
        return awardDao.queryAwardConfigByAwardId(awardId);
    }
}
