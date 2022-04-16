package com.jindidata.user.redpacket.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.jindidata.common.pojo.Result;
import com.jindidata.common.pojo.ResultCode;
import com.jindidata.common.user.UserRequestHandler;
import com.jindidata.user.redpacket.configuration.FlexProperties;
import com.jindidata.user.redpacket.configuration.strategy.StrategyDispatcher;
import com.jindidata.user.redpacket.constant.Global;
import com.jindidata.user.redpacket.mapper.userfollow.SendRedPacketMapper;
import com.jindidata.user.redpacket.pojo.dto.RedPacketDto;
import com.jindidata.user.redpacket.service.SendRedPacketService;
import com.jindidata.user.redpacket.utils.Amount;
import com.jindidata.user.redpacket.utils.ThreadPoolUtils;
import com.jindidata.user.redpacket.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.jindidata.user.redpacket.utils.ThreadPoolUtils.SEND_THREAD_POOL;

/**
 *
 * @author： <a href="mailto:wangfei@tianyancha.com">wangfei</a>
 * @date: 2022/4/16
 * @version: 1.0.0
 */
@Slf4j
@Service
public class SendRedPacketServiceImpl implements SendRedPacketService {

    private final RedissonClient redissonClient;
    private final FlexProperties flexProperties;
    private final StrategyDispatcher strategyDispatcher;
    private final SendRedPacketMapper sendRedPacketMapper;

    public SendRedPacketServiceImpl(RedissonClient redissonClient, FlexProperties flexProperties, StrategyDispatcher strategyDispatcher,
        SendRedPacketMapper sendRedPacketMapper) {
        this.redissonClient = redissonClient;
        this.flexProperties = flexProperties;
        this.strategyDispatcher = strategyDispatcher;
        this.sendRedPacketMapper = sendRedPacketMapper;
    }

    @Override
    public Result<Long> sendRedPacket(Integer amount, Integer num, String redType, Long redId, Long groupId) {
        Result result = new Result();

        // 校验用户登录
        Long userId = UserRequestHandler.getUserId();
        Preconditions.checkArgument(userId != null, "未登录");

        // 入参合法性校验
        Preconditions.checkArgument(amount <= 200 * 100, "红包金额超限");
        Preconditions.checkArgument(Global.RED_TYPE_01.equals(redType) || Global.RED_TYPE_02.equals(redType), "红包类别有误");
        Preconditions.checkArgument(redId != null && redId > 0, "红包ID不合法");
        Preconditions.checkArgument(groupId != null && groupId > 0, "群ID不合法");

        // 最低金额校验
        Double c_money = Amount.div(Double.parseDouble(String.valueOf(amount)), Double.parseDouble(String.valueOf(num)));
        Preconditions.checkArgument(c_money > 1, "输入红包份数太大，最低金额不能低于0.01元");

        // 用户账户剩余金额是否充足校验（非此需求核心关注点 暂略）

        // 切分红包 策略模式支持多种算法
        strategyDispatcher.getLogicHandler(redType).divRedPacket(amount, num, redId);

        // 用户账户剩余金额变更操作 从用户账户扣除 money 并在账户交易表中插入数据 （非此需求核心关注点 暂略）

        Map paramIn = new HashMap();
        paramIn.put("sendRedPacketId", redId);
        paramIn.put("groupId", groupId);
        paramIn.put("userId", userId);
        paramIn.put("redType", redType);
        paramIn.put("copies", num);
        //数据库金额存分
        paramIn.put("money", amount);
        paramIn.put("status", Global.DEAL_W);
        paramIn.put("userOrMerchant", Global.U);
        paramIn.put("createDate", Util.getSimilerDatePath());
        paramIn.put("months", Util.getMonths());
        paramIn.put("days", Util.getDays());
        // 线程异步操作
        ThreadPoolUtils.getExecutorService(SEND_THREAD_POOL).execute(() -> {
            //保存数据库
            if (paramIn == null) {
                log.error("发红包时，数据保存参数为空");
                return;
            }
            int saveFlag = sendRedPacketMapper.saveSendPacket(paramIn);
            if (saveFlag < 1) {
                log.error("发红包时，数据保存数据失败");
            }

            //加入延时队列 剩余未抢完红包超时自动退回
            RBlockingQueue<RedPacketDto> blockingFairQueue = redissonClient.getBlockingQueue("delay_queue");
            RDelayedQueue<RedPacketDto> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
            delayedQueue.offer(new RedPacketDto(redId), flexProperties.getRedPacketTimeoutSec(), TimeUnit.SECONDS);
            log.info("delay input dto:{}", JSON.toJSONString(redId));
        });

        result.setMessage("准备红包成功");
        result.setCode(ResultCode.OK.getCode());
        return result;
    }
}
