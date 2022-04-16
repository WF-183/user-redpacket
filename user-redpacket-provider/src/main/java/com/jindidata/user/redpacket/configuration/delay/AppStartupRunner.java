package com.jindidata.user.redpacket.configuration.delay;

import com.alibaba.fastjson.JSON;
import com.jindidata.user.redpacket.constant.Global;
import com.jindidata.user.redpacket.mapper.userfollow.SendRedPacketMapper;
import com.jindidata.user.redpacket.pojo.dto.RedPacketDto;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author： <a href="mailto:wangfei@tianyancha.com">wangfei</a>
 * @date: 2022/4/16
 * @version: 1.0.0
 */
@Slf4j
@Component
public class AppStartupRunner implements CommandLineRunner {

    private final RedissonClient redissonClient;
    private final RedisTemplate redisTemplate;
    private final SendRedPacketMapper sendRedPacketMapper;

    public AppStartupRunner(RedissonClient redissonClient, RedisTemplate redisTemplate, SendRedPacketMapper sendRedPacketMapper) {
        this.redissonClient = redissonClient;
        this.redisTemplate = redisTemplate;
        this.sendRedPacketMapper = sendRedPacketMapper;
    }

    @Override
    public void run(String... args) {
        new Thread(() -> {
            RBlockingQueue<RedPacketDto> blockingFairQueue = redissonClient.getBlockingQueue("delay_queue");
            RDelayedQueue<RedPacketDto> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
            //应用启动时往队列里面放一个空值,防止重启应用可能导致队列已有的数据消费不及时
            delayedQueue.offer(null, 1, TimeUnit.SECONDS);
            while (true) {
                RedPacketDto dto = null;
                try {
                    dto = blockingFairQueue.take();
                    log.info("delay receive dto:{}", JSON.toJSONString(dto));
                } catch (Exception e) {
                    continue;
                }
                if (Objects.isNull(dto)) {
                    continue;
                }

                //dosomething 处理相关业务逻辑
                //指定延时后拿到红包分布式ID，check红包是否被领光，剩余未抢完红包超时自动退回
                Double stockMoney = 0.0;
                while (true) {
                    Double obj = (Double)redisTemplate.opsForList().leftPop("redId:" + dto.getRedId());
                    if (obj == null) {
                        break;
                    }
                    stockMoney += obj;
                }
                //有剩余
                if (Math.abs(stockMoney) > 0.000001) {
                    Map paramIn = new HashMap();
                    Integer moneyBack = new BigDecimal(stockMoney * 100 + "").setScale(0, BigDecimal.ROUND_HALF_EVEN).intValue();
                    paramIn.put("moneyBack", moneyBack);
                    paramIn.put("sendRedPacketId", dto.getRedId());
                    sendRedPacketMapper.updateMoneyBack(paramIn);
                    log.info("还有" + stockMoney + "元未领取，返回给用户");
                }

                //处理红包状态
                //清空redis缓存记录
                redisTemplate.delete("redId:" + dto.getRedId());
                redisTemplate.delete("redInfo:" + dto.getRedId());
                //数据库状态设为终结态
                Map paramIn = new HashMap();
                paramIn.put("status", Global.DEAL_C);
                paramIn.put("sendRedPacketId", dto.getRedId());
                sendRedPacketMapper.updateSendPacketStatus(paramIn);
            }
        }).start();
    }

}