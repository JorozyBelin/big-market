package org.example.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.apache.ibatis.annotations.Mapper;
import org.example.infrastructure.persistent.po.Task;

import java.util.List;

/**
 * 任务表，发送MQ
 */
@Mapper
public interface ITaskDao {
    /**
     * 插入任务
     * @param task
     */
    void insert(Task task);

    @DBRouter(key = "userId")
    void updateTaskSendMessageFail(Task task);

    @DBRouter(key = "userId")
    void updateTaskSendMessageComplete(Task task);

    List<Task> queryNoSendMessageTaskList();
}
