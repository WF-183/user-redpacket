package com.jindidata.user.redpacket.provider.impl;

import com.google.common.base.Preconditions;
import com.jindidata.common.user.UserRequestHandler;
import com.jindidata.service.common.ResultWrapper;
import com.jindidata.user.redpacket.pojo.po.GetRedPacket;
import com.jindidata.user.redpacket.pojo.po.SendRedPacket;
import com.jindidata.user.redpacket.pojo.query.RedPacketQueryModel;
import com.jindidata.user.redpacket.pojo.vo.PagerResultWrapper;
import com.jindidata.user.redpacket.provider.QueryRedPacketProvider;
import com.jindidata.user.redpacket.service.QueryRedPacketService;
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
@RequestMapping(QueryRedPacketProvider.PATH_PREFIX)
public class QueryRedPacketProviderImpl implements QueryRedPacketProvider {

    private final QueryRedPacketService queryRedPacketService;

    public QueryRedPacketProviderImpl(QueryRedPacketService queryRedPacketService) {
        this.queryRedPacketService = queryRedPacketService;
    }

    @Override
    public ResultWrapper<PagerResultWrapper<SendRedPacket>> findSendRedPackets(RedPacketQueryModel queryModel) {
        queryModel.setUserId(UserRequestHandler.getUserId());
        Preconditions.checkArgument(queryModel.getUserId() != null, "未登录");

        ResultWrapper<PagerResultWrapper<SendRedPacket>> resultWrapper = new ResultWrapper<>();
        PagerResultWrapper<SendRedPacket> data = queryRedPacketService.findSendRedPackets(queryModel);
        resultWrapper.setData(data);
        return resultWrapper;
    }

    @Override
    public ResultWrapper<PagerResultWrapper<GetRedPacket>> findGetRedPackets(RedPacketQueryModel queryModel) {
        queryModel.setUserId(UserRequestHandler.getUserId());
        Preconditions.checkArgument(queryModel.getUserId() != null, "未登录");

        ResultWrapper<PagerResultWrapper<GetRedPacket>> resultWrapper = new ResultWrapper<>();
        PagerResultWrapper<GetRedPacket> data = queryRedPacketService.findGetRedPackets(queryModel);
        resultWrapper.setData(data);
        return resultWrapper;
    }
}
