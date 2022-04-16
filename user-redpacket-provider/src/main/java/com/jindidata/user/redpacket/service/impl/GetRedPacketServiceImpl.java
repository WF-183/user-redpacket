package com.jindidata.user.redpacket.service.impl;

import com.google.common.base.Preconditions;
import com.jindidata.common.pojo.Result;
import com.jindidata.common.pojo.ResultCode;
import com.jindidata.common.user.UserRequestHandler;
import com.jindidata.user.redpacket.constant.Global;
import com.jindidata.user.redpacket.mapper.userfollow.GetRedPacketMapper;
import com.jindidata.user.redpacket.service.GetRedPacketService;
import com.jindidata.user.redpacket.utils.ThreadPoolUtils;
import com.jindidata.user.redpacket.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.jindidata.user.redpacket.utils.ThreadPoolUtils.GET_THREAD_POOL;

/**
 *
 * @author： <a href="mailto:wangfei@tianyancha.com">wangfei</a>
 * @date: 2022/4/16
 * @version: 1.0.0
 */
@Slf4j
@Service
public class GetRedPacketServiceImpl implements GetRedPacketService {

    private RedisTemplate redisTemplate;
    private GetRedPacketMapper getRedPacketMapper;

    public GetRedPacketServiceImpl(RedisTemplate redisTemplate, GetRedPacketMapper getRedPacketMapper) {
        this.redisTemplate = redisTemplate;
        this.getRedPacketMapper = getRedPacketMapper;
    }

    @Override
    public Result<Long> getRedPacket(Long redId, Long groupId) {
        Result result = new Result();

        // 校验用户登录
        Long userId = UserRequestHandler.getUserId();
        Preconditions.checkArgument(userId != null, "未登录");

        // 入参合法性校验
        Preconditions.checkArgument(redId != null && redId > 0, "红包ID不合法");
        Preconditions.checkArgument(groupId != null && groupId > 0, "群ID不合法");

        //同一用户重复点击
        if (redisTemplate.opsForHash().hasKey("redInfo:" + redId, "user:" + userId)) {
            result.setMessage("您已经抢过该红包了");
            result.setCode(ResultCode.OK.getCode());
            result.setData(null);
            return result;
        }

        //抢红包
        Double amount = (Double)redisTemplate.opsForList().leftPop("redId:" + redId);
        //抢到
        if (amount != null) {
            //redis记录已抢到红包用户
            Map getPacketInfo = new HashMap();
            getPacketInfo.put("sendRedPacketId", redId);
            getPacketInfo.put("groupId", groupId);
            getPacketInfo.put("userId", userId);
            //数据库金额存分
            Integer money = new BigDecimal(amount * 100 + "").setScale(0, BigDecimal.ROUND_HALF_EVEN).intValue();
            getPacketInfo.put("money", money);
            getPacketInfo.put("createDate", Util.getSimilerDatePath());
            getPacketInfo.put("months", Util.getMonths());
            getPacketInfo.put("days", Util.getDays());
            getPacketInfo.put("status", Global.DEAL_W);
            redisTemplate.opsForHash().put("redInfo:" + redId, "user:" + userId, getPacketInfo);
            log.info("用户id:" + userId + "抢到了" + Double.valueOf(amount) + "元");
            // 线程异步操作
            ThreadPoolUtils.getExecutorService(GET_THREAD_POOL).execute(() -> {
                if (getPacketInfo == null) {
                    log.error("发红包时，数据保存参数为空");
                    return;
                }
                int saveFlag = getRedPacketMapper.saveGetPacket(getPacketInfo);
                if (saveFlag < 1) {
                    log.error("发红包时，数据保存数据失败");
                }
            });
            result.setMessage("抢红包成功");
            result.setCode(ResultCode.OK.getCode());
            result.setData(money);
        } else {
            //抢空了 清空缓存记录
            redisTemplate.delete("redId:" + redId);
            redisTemplate.delete("redInfo:" + redId);
            result.setMessage("红包已被抢光");
            result.setCode(ResultCode.OK.getCode());
            result.setData(null);
        }
        return result;
    }
}
