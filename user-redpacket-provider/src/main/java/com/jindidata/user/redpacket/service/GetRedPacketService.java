package com.jindidata.user.redpacket.service;

import com.jindidata.common.pojo.Result;

/**
 *
 * @author： <a href="mailto:wangfei@tianyancha.com">wangfei</a>
 * @date: 2022/4/16
 * @version: 1.0.0
 */
public interface GetRedPacketService {

    public Result<Long> getRedPacket(Long redId, Long groupId);

}
