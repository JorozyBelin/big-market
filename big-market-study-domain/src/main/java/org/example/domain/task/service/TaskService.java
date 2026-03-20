package org.example.domain.task.service;

import org.example.domain.task.repository.ITaskRepository;
import org.example.domain.task.model.entity.TaskEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TaskService implements ITaskService {
    @Resource
    private ITaskRepository taskRepository;
    @Override
    public List<TaskEntity> queryNoSendMessageTaskList() {
        return taskRepository.queryNoSendMessageTaskList();
    }

    @Override
    public void sendMessage(TaskEntity taskEntity) {
        taskRepository.sendMessage(taskEntity);
    }

    @Override
    public void updateTaskSendMessageComplete(String userId, String messageId) {
        taskRepository.updateTaskSendMessageComplete(userId, messageId);
    }

    @Override
    public void updateTaskSendMessageFail(String userId, String messageId) {
        taskRepository.updateTaskSendMessageFail(userId, messageId);
    }
}
