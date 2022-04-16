package com.jindidata.user.redpacket.handler;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 线段切割法
 */
@Service
public class LineDivHandler implements BaseHandler {

    private final RedisTemplate redisTemplate;

    public LineDivHandler(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void divRedPacket(Integer amount, Integer num, Long redId) {
        Random random = new Random();
        int m = 0, n = amount;
        Set<Integer> sets = new TreeSet<>();
        //(m,n)区间
        for (int i = 0; i < (num - 1); i++) {
            //将区间控制在(0,max) ,不能出现为0和最大的情况
            int randInt = random.nextInt(n - m - 1) + (m + 1);
            sets.add(randInt);
        }
        while (sets.size() < (num - 1)) {
            int randInt = random.nextInt(n - m - 1) + (m + 1);
            sets.add(randInt);
        }
        //做为当前set循环中的上一参数
        int cur = 0;
        for (Integer i : sets) {
            redisTemplate.opsForList().leftPush("redId:" + redId, (double)(i - cur) / 100);
            cur = i;
        }
        redisTemplate.opsForList().leftPush("redId:" + redId, (double)(amount - cur) / 100);
    }
}
