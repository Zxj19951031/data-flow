-- auto-generated definition
create table tb_cron
(
    id          int auto_increment
        primary key,
    name        varchar(16)                        not null comment '调度名称',
    expression  varchar(32)                        not null comment '调度表达式',
    metadata    text                               null comment '前端元数据',
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    status      tinyint  default 0                 not null
)
    comment '调度规则表' charset = utf8;

