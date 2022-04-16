
#建表语句
DROP TABLE IF EXISTS `t_sendredpacket`;

CREATE TABLE `t_sendredpacket` (
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
`sendRedPacketId` bigint(20)  NOT NULL,
`groupId` bigint(20)  NOT NULL,
`userId` int(11) NOT NULL COMMENT '用户Id',
`redType` varchar(20) NOT NULL COMMENT '红包类型',
`copies` varchar(20) DEFAULT NULL,
`money` int(11) NOT NULL COMMENT '金额',
`moneyBack` int(11) DEFAULT 0 NOT NULL COMMENT '最终退回金额',
`status` varchar(10) NOT NULL COMMENT '数据状态',
`userOrMerchant` varchar(10) NOT NULL COMMENT '用户还是商家',
`createDate` datetime DEFAULT NULL,
`months` int(11) DEFAULT NULL,
`days` int(11) DEFAULT NULL,
`finishDate` datetime DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `t_getredpacket`;

CREATE TABLE `t_getredpacket` (
`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
`sendRedPacketId` int(11) NOT NULL,
`groupId` bigint(20)  NOT NULL,
`userId` int(11) NOT NULL COMMENT '用户Id',
`money` int(11) NOT NULL COMMENT '金额',
`status` varchar(10) NOT NULL COMMENT '数据状态',
`createDate` datetime DEFAULT NULL,
`months` int(11) DEFAULT NULL,
`days` int(11) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


truncate table t_sendredpacket;
truncate table t_getredpacket;
