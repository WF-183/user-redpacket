package com.jindidata.user.redpacket.provider.impl;

import com.jindidata.common.pojo.Result;
import com.jindidata.user.redpacket.provider.GetRedPacketProvider;
import com.jindidata.user.redpacket.service.GetRedPacketService;
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
@RequestMapping(GetRedPacketProvider.PATH_PREFIX)
public class GetRedPacketProviderImpl implements GetRedPacketProvider {

    private GetRedPacketService getRedPacketService;

    public GetRedPacketProviderImpl(GetRedPacketService getRedPacketService) {
        this.getRedPacketService = getRedPacketService;
    }

    @Override
    public Result<Long> getRedPacket(Long redId, Long groupId) {
        return getRedPacketService.getRedPacket(redId, groupId);
    }
}
