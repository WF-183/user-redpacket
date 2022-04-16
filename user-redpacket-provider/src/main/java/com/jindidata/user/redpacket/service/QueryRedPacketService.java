package com.jindidata.user.redpacket.service;

import com.jindidata.user.redpacket.pojo.po.GetRedPacket;
import com.jindidata.user.redpacket.pojo.po.SendRedPacket;
import com.jindidata.user.redpacket.pojo.query.RedPacketQueryModel;
import com.jindidata.user.redpacket.pojo.vo.PagerResultWrapper;

/**
 *
 * @author： <a href="mailto:wangfei@tianyancha.com">wangfei</a>
 * @date: 2022/4/16
 * @version: 1.0.0
 */
public interface QueryRedPacketService {

    PagerResultWrapper<SendRedPacket> findSendRedPackets(RedPacketQueryModel queryModel);

    PagerResultWrapper<GetRedPacket> findGetRedPackets(RedPacketQueryModel queryModel);

}
