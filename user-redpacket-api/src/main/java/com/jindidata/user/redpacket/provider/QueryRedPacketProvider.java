package com.jindidata.user.redpacket.provider;

import com.jindidata.service.common.ResultWrapper;
import com.jindidata.user.redpacket.pojo.po.GetRedPacket;
import com.jindidata.user.redpacket.pojo.po.SendRedPacket;
import com.jindidata.user.redpacket.pojo.query.RedPacketQueryModel;
import com.jindidata.user.redpacket.pojo.vo.PagerResultWrapper;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author： <a href="mailto:wangfei@tianyancha.com">wangfei</a>
 * @date: 2022/4/16
 * @version: 1.0.0
 */
public interface QueryRedPacketProvider {

    String PATH_PREFIX = "query";

    /**
     * 根据指定用户、指定群、指定时间段查询所有发出的红包记录（含退回金额明细信息）
     * @return
     */
    @GetMapping("send/redPacket")
    ResultWrapper<PagerResultWrapper<SendRedPacket>> findSendRedPackets(RedPacketQueryModel queryModel);

    /**
     * 根据指定用户、指定群、指定时间段查询所有收到的红包记录
     * @return
     */
    @GetMapping("get/redPacket")
    ResultWrapper<PagerResultWrapper<GetRedPacket>> findGetRedPackets(RedPacketQueryModel queryModel);

}
