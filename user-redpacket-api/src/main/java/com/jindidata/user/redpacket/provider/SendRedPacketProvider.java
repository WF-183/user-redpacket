package com.jindidata.user.redpacket.provider;

import com.jindidata.common.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author： <a href="mailto:wangfei@tianyancha.com">wangfei</a>
 * @date: 2022/4/16
 * @version: 1.0.0
 */
public interface SendRedPacketProvider {

    String PATH_PREFIX = "send";

    /**
     * 发送红包
     * @param amount 红包总金额 单位分
     * @param num 红包总个数
     * @param redType 红包类型
     * @param redId 红包ID 分布式雪花算法ID
     * @param groupId 群ID 分布式雪花算法ID
     * @return
     */
    @GetMapping("redPacket")
    Result<Long> sendRedPacket(@RequestParam("amount") Integer amount, @RequestParam("num") Integer num, @RequestParam("redType") String redType,
        @RequestParam("redId") Long redId, @RequestParam("groupId") Long groupId);

}
