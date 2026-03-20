package org.example.domain.award.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TaskStateVO {
    create("create", "创建"),
    completed("completed", "完成"),
    fail("fail", "失败");

    private final String code;
    private final String desc;
}
