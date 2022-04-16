package com.jindidata.user.redpacket.provider.impl;

import com.alibaba.fastjson.JSON;
import com.jindidata.user.redpacket.pojo.dto.RedPacketDto;
import com.jindidata.user.redpacket.provider.DemoProvider;
import com.jindidata.user.redpacket.service.DemoService;
import com.jindidata.common.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.jindidata.common.pojo.ResultFactory.ok;

@Slf4j
@RestController
@RequestMapping(DemoProvider.PATH_PREFIX)
public class DemoProviderImpl implements DemoProvider {


    private final DemoService demoService;
    private final RedissonClient redissonClient;
    private final Redisson redisson;

    public DemoProviderImpl(DemoService demoService, RedissonClient redissonClient, Redisson redisson) {
        this.demoService = demoService;
        this.redissonClient = redissonClient;
        this.redisson = redisson;
    }

    @Override
    public void test() {
        RBlockingQueue<RedPacketDto> blockingFairQueue = redissonClient.getBlockingQueue("delay_queue");
        //应用启动时往队列里面放一个空值,防止重启应用可能导致队列已有的数据消费不及时
        RDelayedQueue<RedPacketDto> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        delayedQueue.offer(null, 1, TimeUnit.SECONDS);

        // 下面是真正的业务
        log.info("放入时间点");
        delayedQueue.offer(new RedPacketDto(5L), 5, TimeUnit.SECONDS);
        delayedQueue.offer(new RedPacketDto(10L), 10, TimeUnit.SECONDS);
        delayedQueue.offer(new RedPacketDto(30L), 30, TimeUnit.SECONDS);
        delayedQueue.offer(new RedPacketDto(50L), 50, TimeUnit.SECONDS);
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RedPacketDto dto = null;
            try {
                dto = blockingFairQueue.take();
                log.info("receive1=======dto:{}",JSON.toJSONString(dto));
            } catch (Exception e) {
                continue;
            }
            if (Objects.isNull(dto)) {
                continue;
            }
        }



    }

    @Override
    public void test2() {
        RBlockingQueue<RedPacketDto> blockingFairQueue = redissonClient.getBlockingQueue("delay_queue");
        RDelayedQueue<RedPacketDto> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        log.info("后续 放入时间点");
        delayedQueue.offer(new RedPacketDto(10L), 10, TimeUnit.SECONDS);
    }

    @GetMapping("realCgid")
    @Override
    public Result<Long> findRealCgid(@NotNull @RequestParam("cgid") Long cgid){
        return ok(demoService.findRealCgid(cgid));
    }

    @GetMapping("realCgid/forInnerRpc")
    @Override
    public Long findRealCgidForInnerRpc(@NotNull @RequestParam("cgid") Long cgid){
        return demoService.findRealCgid(cgid);
    }
}
