package com.jindidata.user.redpacket.service.impl;

import com.jindidata.user.redpacket.mapper.userfollow.GetRedPacketMapper;
import com.jindidata.user.redpacket.mapper.userfollow.SendRedPacketMapper;
import com.jindidata.user.redpacket.pojo.po.GetRedPacket;
import com.jindidata.user.redpacket.pojo.po.SendRedPacket;
import com.jindidata.user.redpacket.pojo.query.RedPacketQueryModel;
import com.jindidata.user.redpacket.pojo.vo.PagerResultWrapper;
import com.jindidata.user.redpacket.service.QueryRedPacketService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author： <a href="mailto:wangfei@tianyancha.com">wangfei</a>
 * @date: 2022/4/16
 * @version: 1.0.0
 */
@Service
public class QueryRedPacketServiceImpl implements QueryRedPacketService {

    private final SendRedPacketMapper sendRedPacketMapper;
    private final GetRedPacketMapper getRedPacketMapper;

    public QueryRedPacketServiceImpl(SendRedPacketMapper sendRedPacketMapper, GetRedPacketMapper getRedPacketMapper) {
        this.sendRedPacketMapper = sendRedPacketMapper;
        this.getRedPacketMapper = getRedPacketMapper;
    }

    @Override
    public PagerResultWrapper<SendRedPacket> findSendRedPackets(RedPacketQueryModel queryModel) {
        PagerResultWrapper<SendRedPacket> pageWrapper = new PagerResultWrapper<>();
        List<SendRedPacket> items = sendRedPacketMapper.findSendRedPackets(queryModel);
        Integer count = sendRedPacketMapper.findSendRedPacketsCount(queryModel);
        pageWrapper.setItems(items);
        pageWrapper.setTotal(count);
        if (queryModel != null && queryModel.getPn() != null) {
            pageWrapper.setPageNum(queryModel.getPn());
        }
        return pageWrapper;
    }

    @Override
    public PagerResultWrapper<GetRedPacket> findGetRedPackets(RedPacketQueryModel queryModel) {
        PagerResultWrapper<GetRedPacket> pageWrapper = new PagerResultWrapper<>();
        List<GetRedPacket> items = getRedPacketMapper.findGetRedPackets(queryModel);
        Integer count = getRedPacketMapper.findGetRedPacketsCount(queryModel);
        pageWrapper.setItems(items);
        pageWrapper.setTotal(count);
        if (queryModel != null && queryModel.getPn() != null) {
            pageWrapper.setPageNum(queryModel.getPn());
        }
        return pageWrapper;
    }
}
