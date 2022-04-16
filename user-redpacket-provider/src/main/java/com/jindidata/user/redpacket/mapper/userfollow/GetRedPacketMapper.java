package com.jindidata.user.redpacket.mapper.userfollow;

import com.jindidata.user.redpacket.pojo.po.GetRedPacket;
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
public interface GetRedPacketMapper {

    int saveGetPacket(Map map);

    List<GetRedPacket> findGetRedPackets(RedPacketQueryModel queryModel);

    Integer findGetRedPacketsCount(RedPacketQueryModel queryModel);

}
