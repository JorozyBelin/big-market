package org.example.domain.award.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.award.event.SendAwardMessageEvent;
import org.example.domain.award.model.vo.TaskStateVO;
import org.example.types.event.BaseEvent;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskEntity {

    /** 用户ID */
    private String userId;
    /** 消息ID */
    private String messageId;
    /** 消息主题 */
    private String topic;
    /** 消息主体 */
    private BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> message;
    /** 任务状态；createOrder-创建、completed-完成、fail-失败 */
    private TaskStateVO state;
}
