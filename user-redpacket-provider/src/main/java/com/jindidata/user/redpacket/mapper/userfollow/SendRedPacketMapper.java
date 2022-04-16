package com.jindidata.user.redpacket.mapper.userfollow;

import com.jindidata.user.redpacket.pojo.po.SendRedPacket;
import com.jindidata.user.redpacket.pojo.query.RedPacketQueryModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 *
 * @author： <a href="mailto:wangfei@tianyancha.com">wangfei</a>
 * @date: 2022/4/16
 * @version: 1.0.0
 */
@Mapper
public interface SendRedPacketMapper {

    int saveSendPacket(Map map);

    int updateMoneyBack(Map map);

    int updateSendPacketStatus(Map map);

    List<SendRedPacket> findSendRedPackets(RedPacketQueryModel queryModel);

    Integer findSendRedPacketsCount(RedPacketQueryModel queryModel);


}
