create database big_market;
use big_market;
create table if not exists `strategy` (
                                          `id` int(16) unsigned not null AUTO_INCREMENT COMMENT 'id',
    `strategy_id` int(16) not null COMMENT '抽奖策略ID',
    `strategy_desc` varchar(128) not null COMMENT '抽奖策略描述',
    `rule_model` varchar(256) not null COMMENT '策略模型',
    `create_time` datetime not null DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime not null DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `strategy_id` (`strategy_id`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '抽奖策略表';
insert into strategy (strategy_id,strategy_desc,rule_model,create_time,update_time)
values ('10001','抽奖策略A','rule_random','2025/07/27','2025/07/27');
create table if not exists strategy_award (
                                              `id` int(16) unsigned not null AUTO_INCREMENT COMMENT 'id',
    `strategy_id` int(16) not null COMMENT '抽奖策略ID',
    `award_id` int(16) not null COMMENT '抽奖奖品ID',
    `award_title` varchar(128) not null COMMENT '抽奖奖品标题',
    `award_subtitle` varchar(128) COMMENT '抽奖奖品副标题',
    `award_count` int(8) not null COMMENT '奖品库存总量',
    `award_count_surplus` int(8) not null COMMENT '奖品库存剩余',
    `award_count_rate` decimal(6,4) not null COMMENT '奖品中奖概率',
    `sort` int(8) not null DEFAULT 0 COMMENT '排序',
    `create_time` datetime not null DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime not null DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `award_id` (`award_id`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '奖品表';
insert into strategy_award (strategy_id,award_id,award_title,award_subtitle,award_count,award_count_surplus,award_count_rate,sort,create_time,update_time)
values ('10001','101','随机积分',null,80000,80000,80,0,'2025/07/27','2025/07/27'),
       ('10001','102','5次使用',null,10000,10000,10,1,'2025/07/27','2025/07/27'),
       ('10001','103','10次使用',null,5000,5000,5,2,'2025/07/27','2025/07/27'),
       ('10001','104','20次使用',null,4000,4000,4,3,'2025/07/27','2025/07/27'),
       ('10001','105','增加gpt-4对话模型',null,600,600,0.6,4,'2025/07/27','2025/07/27'),
       ('10001','106','增加dall-e-2对话模型','抽奖1次后解锁',200,200,0.2,5,'2025/07/27','2025/07/27'),
       ('10001','107','增加dall-e-3对话模型','抽奖2次后解锁',199,199,0.1999,6,'2025/07/27','2025/07/27'),
       ('10001','108','解锁全部模型','抽奖6次后解锁',1,1,0.0001,7,'2025/07/27','2025/07/27');
create table if not exists strategy_rule (
                                             `id` int(16) unsigned not null AUTO_INCREMENT COMMENT 'id',
    `strategy_id` int(16) not null COMMENT '抽奖策略ID',
    `award_id` int(16) COMMENT '抽奖奖品ID',
    `rule_type` int(8) not null COMMENT '抽奖规则类型【1-策略规则 2-奖品规则】',
    `rule_model` varchar(16) not null COMMENT '抽奖规则类型【rule_luck】',
    `award_value` varchar(128) not null COMMENT '抽奖规则比值',
    `rule_desc` varchar(128) not null COMMENT '抽奖规则描述',
    `create_time` datetime not null DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime not null DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='奖品策略规则表';
insert into strategy_rule (strategy_id,award_id,rule_type,rule_model,award_value,rule_desc,create_time,update_time)
values ('10001','101','2','rule_random','1,1000','随机积分策略','2025/07/27','2025/07/27'),
       ('10001','107','2','rule_lock','1','抽奖1次后解锁','2025/07/27','2025/07/27'),
       ('10001','108','2','rule_lock','2','抽奖2次后解锁','2025/07/27','2025/07/27'),
       ('10001','109','2','rule_lock','3','抽奖6次后解锁','2025/07/27','2025/07/27'),
       ('10001','101','2','rule_lock_award','1,1000','随机积分兜底','2025/07/27','2025/07/27'),
       ('10001',null,'1','rule_weight','6000:102,103,104,105,106,107,108,109','幸运值满了必中奖品','2025/07/27','2025/07/27'),
       ('10001',null,'1','rule_backlist','1','黑名单用户，1积分兜底','2025/07/27','2025/07/27');
CREATE TABLE  `award`(
                         `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                         `award_id` int(8) NOT NULL COMMENT '抽奖奖品ID，内部流转使用',
                         `award_key` varchar(32) NOT NULL  COMMENT '奖品对接标识 - 每一个都是一个对应的发奖策略',
                         `award_config` varchar(32)  NOT NULL  COMMENT '奖品配置信息',
                         `award_desc` varchar(128)  NOT NULL  COMMENT '奖品内容描述',
                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT INTO `award` VALUES (1, 101, 'user_credit_random', '1100', '用户积分【优先透彻规则范围，如果没有则走配置】', '2023-12-09 11:07:06', '2023-12-09 11:21:31');
INSERT INTO `award` VALUES (2, 102, 'openai_use_count', '5', 'OpenAI增加使用次数', '2023-12-09 11:07:06', '2023-12-09 11:12:59');
INSERT INTO `award` VALUES (3, 103, 'openai_use_conut', '10', 'OpenAI增加使用次数', '2023-12-09 11:07:06', '2023-12-09 11:12:59');
INSERT INTO `award` VALUES (4, 104, 'openai_use_conut', '20', 'OpenAI增加使用次数', '2023-12-09 11:07:06', '2023-12-09 11:12:58');
INSERT INTO `award` VALUES (5, 105, 'openai_model', 'gpt-4', 'OpenAI增加模型', '2023-12-09 11:07:06', '2023-12-09 11:12:01');
INSERT INTO `award` VALUES (6, 106, 'openai_model', 'dall-e-2', 'OpenAI增加模型', '2023-12-09 11:07:06', '2023-12-09 11:12:08');
INSERT INTO `award` VALUES (7, 107, 'openai_model', 'dall-e-3', 'OpenAI增加模型', '2023-12-09 11:07:06', '2023-12-09 11:12:10');
INSERT INTO `award` VALUES (8, 108, 'openai_use_conut', '100', 'OpenAI增加使用次数', '2023-12-09 11:07:06', '2023-12-09 11:12:55');
INSERT INTO `award` VALUES (9, 109, 'openai_model', 'gpt-4,dall-e-2,dall-e-3', 'OpenAI增加模型', '2023-12-09 11:07:06', '2023-12-09 11:12:39');
