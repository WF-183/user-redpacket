package com.jindidata.user.redpacket.provider.impl;

import com.jindidata.common.pojo.Result;
import com.jindidata.user.redpacket.provider.SendRedPacketProvider;
import com.jindidata.user.redpacket.service.SendRedPacketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author： <a href="mailto:wangfei@tianyancha.com">wangfei</a>
 * @date: 2022/4/16
 * @version: 1.0.0
 */
@Slf4j
@RestController
@RequestMapping(SendRedPacketProvider.PATH_PREFIX)
public class SendRedPacketProviderImpl implements SendRedPacketProvider {

    private final SendRedPacketService sendRedPacketService;

    public SendRedPacketProviderImpl(SendRedPacketService sendRedPacketService) {
        this.sendRedPacketService = sendRedPacketService;
    }

    @Override
    public Result<Long> sendRedPacket(Integer amount, Integer num, String redType, Long redId, Long groupId) {
        return sendRedPacketService.sendRedPacket(amount, num, redType, redId, groupId);
    }
}
