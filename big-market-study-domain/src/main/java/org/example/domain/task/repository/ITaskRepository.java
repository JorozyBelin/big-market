package org.example.domain.task.repository;

import org.example.domain.task.model.entity.TaskEntity;

import java.util.List;

public interface ITaskRepository {
    /**
     * 查询没有发送消息的抽奖任务
     * @return
     */
    List<TaskEntity> queryNoSendMessageTaskList();


    /**
     * 发送消息
     * @param taskEntity 抽奖任务
     */
    void sendMessage(TaskEntity taskEntity);

    /**
     * 更新任务发送消息完成

     */
    void updateTaskSendMessageComplete(String userId, String messageId);

    /**
     * 更新任务发送消息失败

     */
    void updateTaskSendMessageFail(String userId, String messageId);
}
