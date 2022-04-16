package com.jindidata.user.redpacket.service;

import com.jindidata.common.pojo.Result;

/**
 *
 * @author： <a href="mailto:wangfei@tianyancha.com">wangfei</a>
 * @date: 2022/4/16
 * @version: 1.0.0
 */
public interface SendRedPacketService {

    Result<Long> sendRedPacket(Integer amount, Integer num, String redType, Long redId,Long groupId);
}
