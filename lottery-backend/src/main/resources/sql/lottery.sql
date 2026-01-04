CREATE TABLE da_le_tou
(
    id        varchar(100) DEFAULT NULL COMMENT '大乐透期数',
    draw_time datetime     DEFAULT NULL COMMENT '开奖时间',
    red_one   varchar(100) DEFAULT NULL COMMENT '红色球1号',
    red_two   varchar(100) DEFAULT NULL COMMENT '红色球2号',
    red_three varchar(100) DEFAULT NULL COMMENT '红色球3号',
    red_four  varchar(100) DEFAULT NULL COMMENT '红色球4号',
    red_five  varchar(100) DEFAULT NULL COMMENT '红色球5号',
    blue_one  varchar(100) DEFAULT NULL COMMENT '蓝色球1号',
    blue_two  varchar(100) DEFAULT NULL COMMENT '蓝色球2号'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3 COMMENT ='大乐透';

CREATE INDEX da_le_tou_id_IDX USING BTREE ON da_le_tou (id);

CREATE TABLE chat_history
(
    session_id varchar(100) NULL COMMENT '会话ID',
    message    TEXT         NULL COMMENT '聊天内容',
    send_time  DATETIME     NULL COMMENT '聊天发送时间'
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci
    COMMENT ='聊天历史';
CREATE INDEX chat_history_session_id_IDX USING BTREE ON chat_history (session_id);
