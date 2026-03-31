package org.example.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.credit.model.aggregate.TradeAggregate;
import org.example.domain.credit.model.entity.CreditAccountEntity;
import org.example.domain.credit.model.entity.CreditOrderEntity;
import org.example.domain.credit.model.entity.TaskEntity;
import org.example.domain.credit.repository.ICreditRepository;
import org.example.infrastructure.persistent.dao.ITaskDao;
import org.example.infrastructure.persistent.dao.IUserCreditAccountDao;
import org.example.infrastructure.persistent.dao.IUserCreditOrderDao;
import org.example.infrastructure.persistent.event.EventPublisher;
import org.example.infrastructure.persistent.po.Task;
import org.example.infrastructure.persistent.po.UserCreditAccount;
import org.example.infrastructure.persistent.po.UserCreditOrder;
import org.example.infrastructure.persistent.redis.IRedisService;
import org.example.types.common.Constants;
import org.redisson.api.RLock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class CreditRepository implements ICreditRepository {
    @Resource
    private IDBRouterStrategy dbRouter;
    @Resource
    private IRedisService redisService;
    @Resource
    private IUserCreditAccountDao userCreditAccountDao;
    @Resource
    private IUserCreditOrderDao userCreditOrderDao;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private ITaskDao taskDao;
    @Resource
    private EventPublisher eventPublisher;
    @Override
    public void saveUserCreditTradeOrder(TradeAggregate tradeAggregate) {
        String userId = tradeAggregate.getUserId();
        CreditAccountEntity creditAccountEntity = tradeAggregate.getCreditAccountEntity();
        CreditOrderEntity creditOrderEntity = tradeAggregate.getCreditOrderEntity();
        TaskEntity taskEntity = tradeAggregate.getTaskEntity();

        //积分账户
        UserCreditAccount userCreditAccountReq = new UserCreditAccount();
        userCreditAccountReq.setUserId(userId);
        userCreditAccountReq.setTotalAmount(creditAccountEntity.getAdjustAmount());
        userCreditAccountReq.setAvailableAmount(creditAccountEntity.getAdjustAmount());

        // 积分订单
        UserCreditOrder userCreditOrderReq = new UserCreditOrder();
        userCreditOrderReq.setUserId(creditOrderEntity.getUserId());
        userCreditOrderReq.setOrderId(creditOrderEntity.getOrderId());
        userCreditOrderReq.setTradeName(creditOrderEntity.getTradeName().getName());
        userCreditOrderReq.setTradeType(creditOrderEntity.getTradeType().getCode());
        userCreditOrderReq.setTradeAmount(creditOrderEntity.getTradeAmount());
        userCreditOrderReq.setOutBusinessNo(creditOrderEntity.getOutBusinessNo());

        //任务
        Task task = new Task();
        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
        task.setMessageId(taskEntity.getMessageId());
        task.setTopic(taskEntity.getTopic());
        task.setUserId(taskEntity.getUserId());
        task.setState(taskEntity.getState().getCode());



        RLock lock = redisService.getLock(Constants.RedisKey.USER_CREDIT_ACCOUNT_LOCK + userId + Constants.UNDERLINE + creditOrderEntity.getOutBusinessNo());
        lock.lock();
        try{
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status->{
                try{
                    // 1. 保存账户积分
                    UserCreditAccount userCreditAccount = userCreditAccountDao.queryUserCreditAccount(userCreditAccountReq);
                    if (null == userCreditAccount) {
                        userCreditAccountDao.insert(userCreditAccountReq);
                    } else {
                        userCreditAccountDao.updateAddAmount(userCreditAccountReq);
                    }
                    // 2. 保存账户订单
                    userCreditOrderDao.insert(userCreditOrderReq);
                    //3.写入任务
                    taskDao.insert(task);
            }catch(DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("调整账户积分额度异常，唯一索引冲突 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                }
                catch(Exception e){
                    status.setRollbackOnly();
                    log.error("调整账户积分额度失败 userId:{} orderId:{}", userId, creditOrderEntity.getOrderId(), e);
                }
                return 1;
        });
        }finally{
            dbRouter.clear();
            lock.unlock();
        }
        try{
            // 发送消息【在事务外执行，如果失败还有任务补偿】
            eventPublisher.publish(task.getTopic(),task.getMessage());
            // 更新数据库记录，task 任务表
            taskDao.updateTaskSendMessageComplete(task);
            log.info("调整账户积分记录，发送MQ消息完成 userId: {} orderId:{} topic: {}", userId, creditOrderEntity.getOrderId(), task.getTopic());
        }catch(Exception e){
            log.error("调整账户积分记录，发送MQ消息失败 userId: {} topic: {}", userId, task.getTopic());
            taskDao.updateTaskSendMessageFail(task);
        }
    }
}
