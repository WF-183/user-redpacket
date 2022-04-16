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
public interface GetRedPacketProvider {

    String PATH_PREFIX = "get";

    /**
     * 抢红包
     * @param redId   红包ID 分布式雪花算法ID
     * @param groupId 群ID 分布式雪花算法ID
     * @return
     */
    @GetMapping("redPacket")
    Result<Long> getRedPacket(@RequestParam("redId") Long redId, @RequestParam("groupId") Long groupId);

}
