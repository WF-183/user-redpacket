package com.jindidata.user.redpacket.handler;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * 二倍均值法
 */
@Service
public class AvgDivHandler implements BaseHandler {

    private final RedisTemplate redisTemplate;

    public AvgDivHandler(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void divRedPacket(Integer amount, Integer num, Long redId) {
        //TODO 后续可使用批处理优化redis插入性能
        Random random = new Random();
        //剩余红包金额为M，剩余人数为N，每次抢到的金额 = 随机区间 (0, M/N *2)
        for (; num > 1; num--) {
            //将区间控制在(0, M/N *2) ,不能出现为0和最大的情况
            int randInt = random.nextInt(amount / num * 2 - 1) + 1;
            amount -= randInt;
            redisTemplate.opsForList().leftPush("redId:" + redId, (double)randInt / 100);
        }
        //最后一个将剩余所有金额拿走
        redisTemplate.opsForList().leftPush("redId:" + redId, (double)amount / 100);
    }
}
