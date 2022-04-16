package com.jindidata.user.redpacket.pojo.po;

import lombok.Data;

import java.util.Date;

@Data
public class GetRedPacket {

    private Long id;
    private Long sendRedPacketId;
    private Long groupId;
    private Long userId;
    private Integer money;
    private String status;
    private Date createDate;
    private Integer months;
    private Integer days;

}
