-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`
(
    `admin_id` int(11)                NOT NULL AUTO_INCREMENT,
    `username` varchar(20) DEFAULT '' not null,
    `password` varchar(20) DEFAULT '' not null,
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    primary key (admin_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

insert into admin(username,password)
values ('admin', 'admin');

-- ----------------------------
-- Table structure for user 用户
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `user_id`  int(11)                                   NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `username` varchar(50) default ''                    NOT NULL COMMENT '用户名',
    `password` varchar(50) default ''                    NOT NULL COMMENT '密码',
    `nickname` varchar(50) DEFAULT '' COMMENT '昵称',
    `face`     varchar(50) DEFAULT '' COMMENT '头像',
    `sex`      int(11)     DEFAULT 0                     not null COMMENT '性别(0男，1女)',
    `bir`      datetime    DEFAULT '0001-01-01 00:00:00' not null COMMENT '出生日期',
    weibo_count int(11) default 0 not null comment '微博数',
    follow_count int(11) default 0 not null comment '关注数',
    fans_count int(11) default 0 not null comment '粉丝数',
    useful int(11) default 1 not null comment '是否有效（0：无效，1有效）',
    admin_able int(11) default 0 not null comment '是否管理（0：否，1是）',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    PRIMARY KEY (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for relation 用户关系
-- ----------------------------
DROP TABLE IF EXISTS `relation`;
CREATE TABLE `relation` (
    `relation_id` int(11) NOT NULL AUTO_INCREMENT comment '关系id',
    `user_id` int(11) DEFAULT 0 not NULL comment '用户id',
    `follow_user_id` int(11) default 0 NOT NULL comment '从属用户id',
    `state` int(11) default 0 NOT NULL comment '关系（0:陌生，1:关注，2:拉黑）',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    PRIMARY KEY (`relation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for message 私信消息
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
    `message_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息id',
    `from_id` int(11) DEFAULT 0 not null COMMENT '发送用户id',
    `to_id` int(11) DEFAULT 0 not NULL COMMENT '接收用户id',
    `message_content` varchar(500) default '' NOT NULL COMMENT '消息内容',
    read_state int(11) default 0 not null comment '是否已读（0：未读，1：已读）',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for weibo 微博
-- ----------------------------
DROP TABLE IF EXISTS `weibo`;
CREATE TABLE `weibo` (
    `weibo_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '微博id',
    `user_id` int(11) DEFAULT 0 not null COMMENT '用户id',
    `post_time` datetime DEFAULT CURRENT_TIMESTAMP not null COMMENT '发送时间',
    `content` varchar(1024) default '' NOT NULL COMMENT '文字内容',
    `pic` varchar(500) DEFAULT '' not null comment '附图集合;分隔',
    topic varchar(100) default '' not null comment '提及话题;分隔',
    `original` int(11) DEFAULT 1 not null comment '是否原创（1是，0否）',
    `repost_id` int(11) DEFAULT 0 not null comment '转发来源微博Id',
    useful int(11) default 1 not null comment '是否有效（0：无效，1有效）',
    comment_count int(11) default 0 not null comment '评论数',
    likes_count int(11) default 0 not null comment '点赞数',
    collect_count int(11) default 0 not null comment '收藏数',
    repost_count int(11) default 0 not null comment '转发数',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    PRIMARY KEY (`weibo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for comment 评论微博
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `comment_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论id',
    `user_id` int(11) DEFAULT 0 not null COMMENT '评论用户id',
    `weibo_id` int(11) DEFAULT 0 not NULL COMMENT '微博id',
    `comment_content` varchar(500) default '' NOT NULL COMMENT '评论内容',
    likes_count int(11) default 0 not null comment '评论点赞数',
    useful int(11) default 1 not null comment '是否有效（0：无效，1有效）',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for reply 回复评论
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply` (
    `reply_id` int(11) NOT NULL AUTO_INCREMENT comment '回复id',
    `comment_id` int(11) DEFAULT 0 not null comment '评论id',
    `from_id` int(11) DEFAULT 0 not null comment '编写回复的用户id',
    `to_id` int(11) DEFAULT 0 not null comment '接收回复的用户id',
    `reply_content` varchar(500) DEFAULT '' not null comment '回复内容',
    origin_reply_id int(11) default 0 not null comment '回复的上级id',
    likes_count int(11) default 0 not null comment '回复点赞数',
    useful int(11) default 1 not null comment '是否有效（0：无效，1有效）',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    PRIMARY KEY (`reply_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for likes 微博点赞
-- ----------------------------
DROP TABLE IF EXISTS `likes`;
CREATE TABLE `likes` (
                         `likes_id` int(11) NOT NULL AUTO_INCREMENT comment '点赞id',
                         `user_id` int(11) DEFAULT 0 not null comment '用户id',
                         `weibo_id` int(11) DEFAULT 0 not null comment '微博id',
                         create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
                         update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
                         PRIMARY KEY (`likes_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for collect 收藏微博
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
    `collect_id` int(11) NOT NULL AUTO_INCREMENT comment '收藏Id',
    `user_id` int(11) DEFAULT 0 not null comment '用户id',
    `weibo_id` int(11) DEFAULT 0 not null comment '微博id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    PRIMARY KEY (`collect_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;





