# user-redpacket

# 一、项目介绍

红包系统，功能点均已实现
<spring.cloud.version>Greenwich.SR6</spring.cloud.version>
<spring.boot.version>2.1.17.RELEASE</spring.boot.version>

# 二、运行流程说明

1、发红包 com.jindidata.user.redpacket.provider.SendRedPacketProvider#sendRedPacket 2、抢红包
com.jindidata.user.redpacket.provider.GetRedPacketProvider#getRedPacket 3、对账 红包查询
//根据指定用户、指定群、指定时间段查询所有发出的红包记录（含退回金额明细信息） com.jindidata.user.redpacket.provider.QueryRedPacketProvider#findSendRedPackets
//根据指定用户、指定群、指定时间段查询所有收到的红包记录 com.jindidata.user.redpacket.provider.QueryRedPacketProvider#findGetRedPackets

# 三、运行效果录屏

略

# 四、开发思路

使用redis实现抢红包功能，借助redis list队列做moneyList方案，在发红包时提前将红包拆好存储，解决超发现象，借助策略模式提供两种算法解决红包金额平衡问题 、

# 五、遇到的问题

红包在指定时间后剩余金额进行退回

# 六、解决方案

借助redission延时队列RDelayedQueue实现

# 七、后续拓展

项目工程结构清晰，分层明确，后续可按需扩展新功能点及接口

# 输出功能

示例信息（DemoProvider）

- 示例接口1
- 示例接口2

# 资源依赖
| 服务      | 类           | 接口                                                    | 版本  |
| --------- | ------------ | ------------------------------------------------------- | ----- |
| 云产品 | 实例ID               | 实例配置           | 数据库 |
| ------ | -------------------- | ------------------ | ------ |
| RDS    | rr-2zej8c41bxy3w9y0j | 16核 64G 1.8万iops | prism1 |
| Redis  | r-2zed126d13b12034   | 4G集群版（8节点）  | 0      |

# 性能报告
- 测试ECS: 一台 4核 8G CentOS 6.3 千兆网卡
- 服务ECS: 两台 4核 8G CentOS 6.3 千兆网卡
- JVM参数: -Xms2g -Xmx2g

| 模块 | 接口     | 测试用例    | 线程数 | 压测时长 | Average | Max | 99%Line | Error% | TPS  | 瓶颈                |
| ---- | -------- | ----------- | ------ | -------- | ------- | --- | ------- | ------ | ---- | ------------------- |

# 使用限制

# 接入服务

# TODO List

# FAQ
