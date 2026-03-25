package org.example.infrastructure.persistent.repository;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.rebate.model.aggregate.BehaviorRebateAggregate;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.example.domain.rebate.model.entity.TaskEntity;
import org.example.domain.rebate.model.vo.BehaviorTypeVO;
import org.example.domain.rebate.model.vo.DailyBehaviorRebateVO;
import org.example.domain.rebate.repository.IBehaviorRebateRepository;
import org.example.infrastructure.persistent.dao.IDailyBehaviorRebateDao;
import org.example.infrastructure.persistent.dao.ITaskDao;
import org.example.infrastructure.persistent.dao.IUserBehaviorRebateOrderDao;
import org.example.infrastructure.persistent.event.EventPublisher;
import org.example.infrastructure.persistent.po.DailyBehaviorRebate;
import org.example.infrastructure.persistent.po.Task;
import org.example.infrastructure.persistent.po.UserBehaviorRebateOrder;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class BehaviorRebateRepository implements IBehaviorRebateRepository {
    @Autowired
    private IDailyBehaviorRebateDao dailyBehaviorRebateDao;
    @Autowired
    private IUserBehaviorRebateOrderDao userBehaviorRebateOrderDao;
    @Resource
    private IDBRouterStrategy dbRouter;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private ITaskDao taskDao;
    @Autowired
    private EventPublisher eventPublisher;

    @Override
    public List<DailyBehaviorRebateVO> queryDailyBehaviorRebateConfig(BehaviorTypeVO behaviorTypeVO) {
        List<DailyBehaviorRebate> dailyBehaviorRebates = dailyBehaviorRebateDao.queryDailyBehaviorRebateByBehaviorType(behaviorTypeVO.getCode());
        ArrayList<DailyBehaviorRebateVO> dailyBehaviorRebateVOS = new ArrayList<>();
        for (DailyBehaviorRebate dailyBehaviorRebate : dailyBehaviorRebates) {
            DailyBehaviorRebateVO dailyBehaviorRebateVO = DailyBehaviorRebateVO.builder()
                    .behaviorType(dailyBehaviorRebate.getBehaviorType())
                    .rebateDesc(dailyBehaviorRebate.getRebateDesc())
                    .rebateType(dailyBehaviorRebate.getRebateType())
                    .rebateConfig(dailyBehaviorRebate.getRebateConfig())
                    .build();
            dailyBehaviorRebateVOS.add(dailyBehaviorRebateVO);
        }
        return dailyBehaviorRebateVOS;
    }

    @Override
    public void saveUserRebateRecord(String userId, ArrayList<BehaviorRebateAggregate> behaviorRebateAggregates) {
        try {
            dbRouter.doRouter(userId);
            transactionTemplate.execute(status -> {
                try {
                    for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
                        BehaviorRebateOrderEntity behaviorRebateOrderEntity = behaviorRebateAggregate.getBehaviorRebateOrderEntity();
                        UserBehaviorRebateOrder userBehaviorRebateOrder = new UserBehaviorRebateOrder();
                        userBehaviorRebateOrder.setBehaviorType(behaviorRebateOrderEntity.getBehaviorType());
                        userBehaviorRebateOrder.setRebateConfig(behaviorRebateOrderEntity.getRebateConfig());
                        userBehaviorRebateOrder.setRebateDesc(behaviorRebateOrderEntity.getRebateDesc());
                        userBehaviorRebateOrder.setRebateType(behaviorRebateOrderEntity.getRebateType());
                        userBehaviorRebateOrder.setUserId(userId);
                        userBehaviorRebateOrder.setOrderId(behaviorRebateOrderEntity.getOrderId());
                        userBehaviorRebateOrder.setBizId(behaviorRebateOrderEntity.getBizId());
                        userBehaviorRebateOrder.setOutBusinessNo(behaviorRebateOrderEntity.getOutBusinessNo());
                        userBehaviorRebateOrderDao.insert(userBehaviorRebateOrder);
                        //任务对象
                        TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
                        Task task = new Task();
                        task.setState(taskEntity.getState().getCode());
                        task.setUserId(userId);
                        task.setTopic(taskEntity.getTopic());
                        task.setMessage(JSON.toJSONString(taskEntity.getMessage()));
                        task.setMessageId(taskEntity.getMessageId());
                        taskDao.insert(task);
                    }
                    return 1;
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("写入返利记录，唯一索引冲突 userId:{}", userId, e);
                    throw new AppException(ResponseCode.INDEX_DUP.getCode(), e);
                }
            });
        } finally {
            dbRouter.clear();
        }
        //发送MQ消息
        for (BehaviorRebateAggregate behaviorRebateAggregate : behaviorRebateAggregates) {
            TaskEntity taskEntity = behaviorRebateAggregate.getTaskEntity();
            Task task = new Task();
            task.setUserId(userId);
            task.setMessageId(taskEntity.getMessageId());
            try{
                eventPublisher.publish(taskEntity.getTopic(),taskEntity.getMessage());
                taskDao.updateTaskSendMessageComplete(task);
            }catch(Exception e){
                taskDao.updateTaskSendMessageFail(task);
                log.error("发送MQ消息失败 userId:{} topic:{} message:{}", userId, taskEntity.getTopic(), taskEntity.getMessage(), e);
                throw new AppException(ResponseCode.UN_ERROR.getCode(), e);
            }
        }
    }

    @Override
    public List<BehaviorRebateOrderEntity> queryUserBehaviorRebateOrderByOutBusinessNo(String userId, String outBusinessNo) {
        UserBehaviorRebateOrder userBehaviorRebateOrderReq = new UserBehaviorRebateOrder();
        userBehaviorRebateOrderReq.setOutBusinessNo(outBusinessNo);
        userBehaviorRebateOrderReq.setUserId(userId);
        List<BehaviorRebateOrderEntity> behaviorRebateOrderEntities = new ArrayList<>();
        List<UserBehaviorRebateOrder> userBehaviorRebateOrders = userBehaviorRebateOrderDao.queryUserBehaviorRebateOrderByOutBusinessNo(userBehaviorRebateOrderReq);
        for (UserBehaviorRebateOrder userBehaviorRebateOrder : userBehaviorRebateOrders) {
            BehaviorRebateOrderEntity behaviorRebateOrderEntity = BehaviorRebateOrderEntity.builder()
                    .behaviorType(userBehaviorRebateOrder.getBehaviorType())
                    .bizId(userBehaviorRebateOrder.getBizId())
                    .orderId(userBehaviorRebateOrder.getOrderId())
                    .outBusinessNo(userBehaviorRebateOrder.getOutBusinessNo())
                    .rebateConfig(userBehaviorRebateOrder.getRebateConfig())
                    .rebateDesc(userBehaviorRebateOrder.getRebateDesc())
                    .rebateType(userBehaviorRebateOrder.getRebateType())
                    .userId(userBehaviorRebateOrder.getUserId())
                    .build();
                    behaviorRebateOrderEntities.add(behaviorRebateOrderEntity);
        }
        return behaviorRebateOrderEntities;
    }
}
